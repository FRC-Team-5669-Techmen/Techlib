package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.PneumaticClawSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ToggleClaw extends Command {
    private PneumaticClawSubsystem m_claw;

    public ToggleClaw(PneumaticClawSubsystem claw) {
        m_claw = claw;
    }

    @Override
    protected void execute() {
        m_claw.setClawOpen(!m_claw.isClawOpen());
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}