package edu.boscotech.techlib.subsystems;

import java.util.List;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.configparser.AbstractConfiggable;
import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.configparser.IConfiggable;
import edu.boscotech.techlib.subsystems.Controller;
import edu.boscotech.techlib.validator.IElementValidator;
import edu.boscotech.techlib.validator.ObjectValidator;
import edu.boscotech.techlib.hardware.Component;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@AbstractConfiggable
public abstract class BetterSubsystem implements IConfiggable, Sendable {
    private SendableBuilder mNetworkData;
    private String mSmartDashboardTypeName, mName;
    private List<Component> mComponents;
    private Controller mCurrentController;

    /**
     * Constructor for BetterSubsystem.
     * @param smartDashboardTypeName The type name to send when transferring
     *                               data to the smart dashboard.
     */
    protected BetterSubsystem(String smartDashboardTypeName) {
        mSmartDashboardTypeName = smartDashboardTypeName;
        LiveWindow.add(this);
    }

    /**
     * This method must be overridden to set up this subsystem based on data
     * found in a {@link ConfigElement} read from the robot's config file and
     * passed in as an argument.
     * @param configTree The configuration data read from the robot's config file.
     */
    protected abstract void setup(TechlibRobot robot, ConfigElement configTree);

    protected abstract ObjectValidator createValidator();

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
        childComponent.setSubsystem(mName);
        mComponents.add(childComponent);
    }

    @Override
    public final void setupWrapper(TechlibRobot robot, 
        ConfigElement configTree) {
        mName = configTree.getStringOrDefault(
            configTree.getName(), "name"
        );
        mNetworkData.setSmartDashboardType(mSmartDashboardTypeName);
        setup(robot, configTree);
    }

    @Override
    public IElementValidator createValidatorWrapper() {
        ObjectValidator validator = createValidator();
        validator.addOptionalField("name", null);
        return validator;
    }

    public final void periodicWrapper() {
        for (Component component : mComponents) {
            component.periodicWrapper();
        }
        periodic();
    }

    protected final void setCurrentController(Controller controller) {
        mCurrentController = controller;
    }

    public final Controller getCurrentController() {
        return mCurrentController;
    }

    public final void enterSafeStateWrapper() {
        enterSafeState();
        for (Component component : mComponents) {
            component.enterSafeStateWrapper();
        }
    }

    @Override
    public final String getName() {
        return mName;
    }

    @Override
    public final void setName(String name) {
        throw new UnsupportedOperationException(
            "Names of Techlib subsystems must be set in the robot.cfg file."
        );
    }

    @Override
    public final String getSubsystem() {
        return mName;
    }

    @Override
    public final void setSubsystem(String subsystem) {
        throw new UnsupportedOperationException(
            "Names of Techlib subsystems must be set in the robot.cfg file."
        );
    }

    @Override
    public final void initSendable(SendableBuilder builder) {
        mNetworkData = builder;
    }
}