package edu.boscotech.techlib.hardware;

import edu.boscotech.techlib.configparser.ConfigElement;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public abstract class Component implements Sendable {
    private SendableBuilder m_networkData;
    private String m_smartDashboardTypeName, m_name, m_subsystem;

    protected Component(String smartDashboardTypeName) {
        m_smartDashboardTypeName = smartDashboardTypeName;
        LiveWindow.add(this);
    }

    protected abstract void setup(ConfigElement configTree);
    protected abstract void periodic();
    public abstract void enterSafeState();

    public final void setupWrapper(ConfigElement configTree) {
        m_name = configTree.getStringOrDefault(
            configTree.getName(), "name"
        );
        m_networkData.setSmartDashboardType(m_smartDashboardTypeName);
        setup(configTree);
    }

    public final void periodicWrapper() {
        periodic();
    }

    @Override
    public final String getName() {
        return m_name;
    }

    @Override
    public final void setName(String name) {
        throw new UnsupportedOperationException(
            "Names of Techlib components must be set in the robot.cfg file."
        );
    }

    @Override
    public final String getSubsystem() {
        return m_subsystem;
    }

    @Override
    public final void setSubsystem(String subsystem) {
        m_subsystem = subsystem;
    }

    @Override
    public final void initSendable(SendableBuilder builder) {
        m_networkData = builder;
    }
}