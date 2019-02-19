package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.EncodedMotorSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class DriveEncodedMotor extends Command {
    private EncodedMotorSubsystem m_motor;
    private double m_speed;

    public DriveEncodedMotor(EncodedMotorSubsystem motor, double speed) {
        m_motor = motor;
        m_speed = speed;
    }

    @Override
    protected void execute() {
        m_motor.setManualSpeed(m_speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        m_motor.goNeutral();
    }
}