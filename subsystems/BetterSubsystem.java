package edu.boscotech.techlib.subsystems;

import java.util.List;

import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.controllers.Controller;
import edu.boscotech.techlib.controllers.Controller.ControllerDelegate;
import edu.boscotech.techlib.hardware.Component;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public abstract class BetterSubsystem implements Sendable {
    private SendableBuilder m_networkData;
    private String m_smartDashboardTypeName, m_name;
    private List<Component> m_components;
    private Controller m_currentController;

    /**
     * Constructor for BetterSubsystem.
     * @param smartDashboardTypeName The type name to send when transferring
     *                               data to the smart dashboard.
     */
    protected BetterSubsystem(String smartDashboardTypeName) {
        m_smartDashboardTypeName = smartDashboardTypeName;
        LiveWindow.add(this);
    }

    /**
     * This method must be overridden to set up this subsystem based on data
     * found in a {@link ConfigElement} read from the robot's config file and
     * passed in as an argument.
     * @param configTree The configuration data read from the robot's config file.
     */
    protected abstract void setup(ConfigElement configTree);

    /**
     * Called periodically while this subsystem is active. Override this method
     * to do any logic your subsystem needs to perform on a regular basis. 
     * DO NOT use this method to check input devices like joysticks! It is the
     * responsibility of a {@link Controller} to control subsystems to perform
     * more complex actions. The periodic method should only contain basic logic
     * necessary for the subsystem to function properly.
     */
    protected abstract void periodic();

    /**
     * Called to put the subsystem in a state where it will be safe to work on.
     * For example, motors should stop spinning, pistons should keep their
     * current state, etc.
     */
    protected abstract void enterSafeState();

    /**
     * Adds a component as part of this subsystem. 
     */
    protected final void addComponent(Component childComponent) {
        childComponent.setSubsystem(m_name);
        m_components.add(childComponent);
    }

    public final void setupWrapper(ConfigElement configTree) {
        m_name = configTree.getStringOrDefault(
            configTree.getName(), "name"
        );
        m_networkData.setSmartDashboardType(m_smartDashboardTypeName);
        setup(configTree);
    }

    public final void periodicWrapper() {
        for (Component component : m_components) {
            component.periodicWrapper();
        }
        periodic();
    }

    public final void setCurrentController(ControllerDelegate delegate, Controller controller) {
        delegate.check();
        m_currentController = controller;
    }

    public final Controller getCurrentController() {
        return m_currentController;
    }

    public final void enterSafeStateWrapper() {
        enterSafeState();
        for (Component component : m_components) {
            component.enterSafeStateWrapper();
        }
    }

    @Override
    public final String getName() {
        return m_name;
    }

    @Override
    public final void setName(String name) {
        throw new UnsupportedOperationException(
            "Names of Techlib subsystems must be set in the robot.cfg file."
        );
    }

    @Override
    public final String getSubsystem() {
        return m_name;
    }

    @Override
    public final void setSubsystem(String subsystem) {
        throw new UnsupportedOperationException(
            "Names of Techlib subsystems must be set in the robot.cfg file."
        );
    }

    @Override
    public final void initSendable(SendableBuilder builder) {
        m_networkData = builder;
    }
}