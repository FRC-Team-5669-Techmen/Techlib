package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.subsystems.AuxMotorSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ManualAuxDrive extends Command {
    private AuxMotorSubsystem m_auxMotor;
    private AnalogControl m_control;

    public ManualAuxDrive(AuxMotorSubsystem auxMotor) {
        m_auxMotor = auxMotor;
        m_control = auxMotor.getAnalogControl("control");
    }

    @Override
    protected void execute() {
        m_auxMotor.set(m_control.getValue());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        m_auxMotor.set(0.0);
    }
}