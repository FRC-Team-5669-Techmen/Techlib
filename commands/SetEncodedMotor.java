package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.EncodedMotorSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class SetEncodedMotor extends Command {
    EncodedMotorSubsystem m_motor;
    boolean m_forward;
    double m_target;

    public SetEncodedMotor(EncodedMotorSubsystem motor, double target) {
        m_motor = motor;
        m_target = target;
        m_forward = m_target > m_motor.getPosition();
    }

    @Override
    public synchronized void start() {
        m_motor.setPosition(m_target);
    }

    @Override
    protected boolean isFinished() {
        return m_forward 
            ? m_motor.getPosition() >= m_target 
            : m_motor.getPosition() <= m_target;
    }

    @Override
    protected void end() {
        m_motor.goNeutral();
    }
}