package edu.boscotech.techlib.hardware;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.configparser.AbstractConfiggable;
import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.configparser.IConfiggable;
import edu.boscotech.techlib.validator.IElementValidator;
import edu.boscotech.techlib.validator.ObjectValidator;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@AbstractConfiggable
public abstract class Component implements IConfiggable, Sendable {
    private SendableBuilder mNetworkData;
    private String mSmartDashboardTypeName, mName, mSubsystem;

    protected Component(String smartDashboardTypeName) {
        mSmartDashboardTypeName = smartDashboardTypeName;
    }

    /**
     * Called when the component is first set up. Use this to initialize any
     * member variables using values in configTree.
     * @param configTree The configuration for this component, read from the
     *                   robot's configuration file.
     */
    protected abstract void setup(TechlibRobot robot, ConfigElement configTree);

    /**
     * Creates an instance of a class which implements IElementValidator. The
     * returned validator should be used to verify the syntax of a 
     * {@link ConfigElement} describing this component.
     * @return
     */
    protected abstract ObjectValidator createValidator();

    /**
     * Called regularly while the component is active. Use this to implement any
     * logic that needs to be run to keep the component operating properly. For
     * example, this could be used to set the output of a PWM port depending on
     * a value set by a different method.
     */
    protected abstract void periodic();

    /**
     * This will put the component in a safe state such that it will not move
     * suddenly or endanger any people around the robot. For example, if this
     * component is a motor, this method will stop the motor.
     */
    protected abstract void enterSafeState();

    /**
     * Called when the network table manager is ready to start sending data.
     * Override this method to add getters and setters so that information about
     * this component can be displayed on the smart dashboard.
     */
    protected abstract void addNetworkData(SendableBuilder builder);

    @Override
    public final void setupWrapper(TechlibRobot robot, 
        ConfigElement configTree) {
        mName = configTree.getStringOrDefault(
            configTree.getName(), "name"
        );
        LiveWindow.add(this);
        setup(robot, configTree);
    }

    @Override
    public IElementValidator createValidatorWrapper() {
        ObjectValidator validator = createValidator();
        validator.addOptionalField("name", null);
        return validator;
    }

    public final void periodicWrapper() {
        periodic();
    }

    public final void enterSafeStateWrapper() {
        enterSafeState();
    }

    @Override
    public final String getName() {
        return mName;
    }

    @Override
    public final void setName(String name) {
        throw new UnsupportedOperationException(
            "Names of Techlib components must be set in the robot.cfg file."
        );
    }

    @Override
    public final String getSubsystem() {
        return mSubsystem;
    }

    @Override
    public final void setSubsystem(String subsystem) {
        mSubsystem = subsystem;
    }

    @Override
    public final void initSendable(SendableBuilder builder) {
        mNetworkData = builder;
        mNetworkData.setSmartDashboardType(mSmartDashboardTypeName);
        addNetworkData(mNetworkData);
    }
}