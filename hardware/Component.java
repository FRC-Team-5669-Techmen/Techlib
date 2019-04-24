package edu.boscotech.techlib.hardware;

import edu.boscotech.techlib.configparser.ConfigElement;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public abstract class Component implements Sendable {
    private SendableBuilder mNetworkData;
    private String mSmartDashboardTypeName, mName, mSubsystem;

    protected Component(String smartDashboardTypeName) {
        mSmartDashboardTypeName = smartDashboardTypeName;
        LiveWindow.add(this);
    }

    /**
     * Called when the component is first set up. Use this to initialize any
     * member variables using values in configTree.
     * @param configTree The configuration for this component, read from the
     *                   robot's configuration file.
     */
    protected abstract void setup(ConfigElement configTree);

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

    public final void setupWrapper(ConfigElement configTree) {
        mName = configTree.getStringOrDefault(
            configTree.getName(), "name"
        );
        setup(configTree);
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