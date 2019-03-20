package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.boscotech.techlib.util.TalonSRXAdapter;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class ClawWheelsSubsystem extends BetterSubsystem {
    private SpeedController m_leftWheels, m_rightWheels;

    public ClawWheelsSubsystem() {
        super("Claw Wheels", "clawWheels", "ClawWheels");
    }

    public ClawWheelsSubsystem(String hrName, String cfgName) {
        super(hrName, cfgName, "ClawWheels");
    }

    @Override
    protected void setup() {
        m_leftWheels = new TalonSRXAdapter(getCfgInt("left"));
        m_leftWheels.setInverted(true);
        m_rightWheels = new TalonSRXAdapter(getCfgInt("right"));
    }

    public void spit() {
        m_leftWheels.set(-1.0);
        m_rightWheels.set(-1.0);
    }

    public void suck() {
        m_leftWheels.set(1.0);
        m_rightWheels.set(1.0);
    }

    public void stop() {
        m_leftWheels.stopMotor();
        m_rightWheels.stopMotor();
    }

    @Override
    protected void enterSafeState() {
        stop();
    }

    @Override
    public Command createDefaultTeleopCommand() {
        return null; // TODO:
    }

    @Override
    public Command createDefaultTestCommand() {
        return new GenericTestCommand(this);
    }
}