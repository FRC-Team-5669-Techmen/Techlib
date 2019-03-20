package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.ToggleClaw;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;

public class PneumaticClawSubsystem extends BetterSubsystem {
    private DoubleSolenoid m_clawActuator;

    public PneumaticClawSubsystem() {
        super("Pneumatic Claw", "claw", "PneumaticClaw");
    }

    public PneumaticClawSubsystem(String hrName, String cfgName) {
        super(hrName, cfgName, "PneumaticClaw");
    }

    @Override
    protected void setup() {
        m_clawActuator = new DoubleSolenoid(
            getCfgInt("open"), getCfgInt("close")
        );
    }

    public void openClaw() {
        m_clawActuator.set(Value.kForward);
    }

    public void closeClaw() {
        m_clawActuator.set(Value.kReverse);
    }

    public boolean isClawOpen() {
        return m_clawActuator.get() == Value.kForward;
    }

    public void setClawOpen(boolean open) {
        m_clawActuator.set(open ? Value.kForward : Value.kReverse);
    }

    @Override
    protected void enterSafeState() {
        m_clawActuator.set(Value.kOff);
    }

    @Override
    public Command createDefaultTeleopCommand() {
        return null;
    }
}