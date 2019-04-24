package edu.boscotech.techlib.controllers;

import java.util.ArrayList;
import java.util.List;

import edu.boscotech.techlib.subsystems.BetterSubsystem;

public abstract class Controller {
    /**
     * Enum for indicating how a controller would like to be treated when it is
     * controlling a subsystem required by a controller that is trying to start.
     */
    public enum InterruptMode {
        /**
         * When the controller is interrupted, pause it. The paused controller
         * will resume once all newer controllers that have taken control of the
         * conflicting subsystems have ended.
         */
        kPauseOnInterrupt,
        /**
         * When the controller is interrupted, stop it. The stopped controller
         * can not and will not be resumed after the controllers controlling
         * the conflicting subsystems have stopped.
         */
        kStopOnInterrupt
    };


    /**
     * This is used to mimic the 'friend' system in C++. Since the constructor
     * is private, only Controller has a copy of this class. That means that
     * only a Controller can set the current Controller on a BetterSubsystem.
     */
    public class ControllerDelegate {
        private ControllerDelegate() { }
        public boolean check() { return true; }
    }

    /**
     * Keeps track of all controllers that are currently active. Once a
     * controler is paused or stopped, it gets taken off this list. Once a
     * controller is resumed, it gets put back on this list.
     */
    private static List<Controller> s_activeControllers = new ArrayList<>();
    private final ControllerDelegate m_DELEGATE = new ControllerDelegate();
    private List<BetterSubsystem> m_requirements = new ArrayList<>();
    private boolean m_started = false, m_paused = false, m_done = false;
    // This keeps track of the number of events that are causing this controller
    // to be paused. Every time a controller takes over one subsystem that this
    // controller is controlling, the pause count is increased by one. Every
    // time a controller stops controlling a subsystem that it took over from
    // this controller, the pause count is decreased by one. Once it reaches
    // zero, it means all controllers that were using any subsystems this
    // controller was using have ended, and this controller can resume.
    private int m_pauseCount = 0; 
    // List of all controllers that this controller had to pause when it
    // started. In other words, a list of controllers that were controlling
    // subsystems that this controller needed access to. Controllers that are
    // stopped (instead of paused) to release their subsystems are not stored
    // in this list. This list contains duplicate entries for controllers that
    // had multiple subsystems this controller needed. There is one copy of the
    // reference to the paused controller for every subsystem it was using that
    // was needed by this controller. This allows accurate decrementing of the
    // paused controllers' m_pauseCount when they get resumed by this
    // controller.
    private List<Controller> m_pausedControllers = new ArrayList<>();

    /**
     * Returns the preferred interrupt mode of this controller.
     * {@see InterruptMode}
     * @return the preferred interrupt mode of this controller.
     */
    protected abstract InterruptMode getInterruptedMode();

    /**
     * Called when the controller is started for the first time. Only called
     * once. This can be used to implement custom startup logic for commands.
     * There is no need to call super(), as this method is just called by a
     * wrapper that does some boilerplate logic.
     */
    protected void onStart() { }

    /**
     * Called when the controller is paused by another controller. This can be
     * used for tasks like pausing timers when the command is paused. There is 
     * no need to call super(), as this method is just called by a wrapper that 
     * does some boilerplate logic.
     */
    protected void onPause() { }

    /**
     * Called when the controller is resumed by another controller. This can be
     * used for tasks like resuming timers that were previously paused. There is 
     * no need to call super(), as this method is just called by a wrapper that 
     * does some boilerplate logic.
     */
    protected void onResume() { }

    /**
     * Called when the controller is stopped for the first time. Only called
     * once. This can be used to implement custom shutdown logic for commands.
     * This is called both when the controller completes its intended task (and
     * indicates that it is finished with {@link checkIfDone()}) or if the
     * controller is forcefully stopped by user code or another controller.
     * There is no need to call super(), as this method is just called by a
     * wrapper that does some boilerplate logic.
     */
    protected void onEnd() { }

    /**
     * Called periodically while this controller is active. Use this to
     * implement the bulk of your command's logic. There is no need to call 
     * super(), as this method is just called by a wrapper that does some
     * boilerplate logic.
     */
    protected abstract void periodic();

    /**
     * Called periodically to determine if the controller has completed its
     * intended task. For example, if the controller is designed to rotate a
     * servo motor to a particular position, then this method should return true
     * when the motor reaches that position. If this controller is not designed
     * to do any time-limited task, such as controlling the wheels based on
     * joystick input, then this method should always return false.
     * @return True if the controller is done performing its intended task.
     */
    protected abstract boolean checkIfDone();

    /**
     * Adds a subsystem to a list of subsystems that this controller requires
     * control over. When this controller is started, any other controllers
     * running on required subsystems will either be paused or stopped,
     * depending on the other controllers' respective interrupt modes.
     * @param subsystem The subsystem to add as a requirement.
     */
    protected final void addRequirement(BetterSubsystem subsystem) {
        m_requirements.add(subsystem);
    }

    /**
     * @return the list of {@link BetterSubsystem}s this {@link Controller}
     * uses while it is running.
     */
    public final List<BetterSubsystem> getRequirements() {
        return m_requirements;
    }

    /**
     * @return true if this controller has been started. Still true even if the
     * command has ended or has been paused.
     */
    public final boolean wasStarted() {
        return m_started;
    }

    /**
     * @return true if this controller is finished controlling its targets.
     */
    public final boolean isDone() {
        return m_done;
    }

    /**
     * @return true if this controller is currently paused.
     */
    public final boolean isPaused() {
        return m_paused;
    }

    /**
     * @return true if this controller was started and is not paused and is not done.
     */
    public final boolean isActive() {
        return wasStarted() && !isDone() && !isPaused();
    }

    public final void start() {
        if (m_started) return;
        // Iterate over all subsystems this controller has control over.
        for (BetterSubsystem subsystem : getRequirements()) {
            // Store the old controller.
            Controller currentController = subsystem.getCurrentController();
            // Set *this* as the new controller of the subsystem.
            subsystem.setCurrentController(m_DELEGATE, this);
            // If the 'old controller' was null, it means there was nothing
            // controlling that subsystem so we can continue to the next
            // subsystem without doing anything.
            if (currentController == null) continue;
            // If the subsystem we are taking over has an active controller...
            if (currentController.isActive()) {
                // If the controller is set to be stopped when interrupted, then
                // stop it.
                if (currentController.getInterruptedMode() == InterruptMode.kStopOnInterrupt) {
                    currentController.stop();
                // If not, it means the controller is set to pause on interrupt,
                // so we pause it. Add one to the pauseCount of the controller
                // (this should always set it to 1 because if the controller was
                // active, it means it has not been paused by anthing else.)
                } else {
                    currentController.pause();
                    currentController.m_pauseCount++;
                    m_pausedControllers.add(currentController);
                }
            // If we took over an already paused controller, add one to the
            // pause count because now an extra controller is keeping it paused.
            } else if (currentController.isPaused()) {
                currentController.m_pauseCount++;
                m_pausedControllers.add(currentController);
            }
        }
        s_activeControllers.add(this);
        // Mark this controller as started so it cannot be started again and so
        // other methods will work.
        m_started = true;
        // Allow child classes to implement custom logic.
        onStart();
    }

    private final void pause() {
        // If it is already paused, was never started, or already finished, then
        // this controller cannot be paused.
        if (m_paused || !m_started || m_done) return;
        s_activeControllers.remove(this);
        m_paused = true;
        onPause();
    }

    private final void resume() {
        // If it is already unpaused, was never started, or already finished, 
        // then this controller cannot be resumed.
        if (!m_paused || !m_started || m_done) return;
        s_activeControllers.add(this);
        m_paused = false;
        onResume();
    }

    public final void stop() {
        // If this controller was never started or already finished, then it
        // cannot be stopped.
        if (!m_started || m_done) return;
        // Set the current controller of every controlled subsystem to null.
        for (BetterSubsystem subsystem : getRequirements()) {
            subsystem.setCurrentController(null, null);
        }
        // Unpause any controllers this controller paused when it was started.
        for (Controller pausedController : m_pausedControllers) {
            pausedController.m_pauseCount--;
            // If this controller was the last controller keeping the paused 
            // controller paused, then resume the paused controller.
            if (pausedController.m_pauseCount == 0) {
                // Find the subsystems that this controller took over from the
                // paused controller, and set the previously paused controller
                // as the subsystem's current controller.
                for (BetterSubsystem subsystem : getRequirements()) {
                    if (pausedController.getRequirements().contains(subsystem)) {
                        subsystem.setCurrentController(null, pausedController);
                    }
                }
                pausedController.resume();
            }
        }
        s_activeControllers.remove(this);
        // Mark this controller as done, so it will not be re-activated again in
        // the future.
        m_done = true;
        // Call the custom onEnd method so child classes can add their own end
        // behavior.
        onEnd();
    }

    private final void periodicWrapper() {
        // Don't do any logic if this controller isn't active.
        if (!isActive()) return;
        periodic();
        if (checkIfDone()) {
            stop();
        }
    }

    public static void updateAllActiveContollers() {
        // Copy the list of active controllers so that active controllers can
        // change while updating everything without causing a concurrent
        // modification exception.
        List<Controller> toUpdate = new ArrayList<>();
        for (Controller controller : s_activeControllers) {
            toUpdate.add(controller);
        }
        for (Controller controller : toUpdate) {
            controller.periodicWrapper();
        }
    }
}