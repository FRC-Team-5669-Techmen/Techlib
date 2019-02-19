package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.LiftSubsystem;

public class SetLiftSpeed extends SingleActionCommand<LiftSubsystem> {
  double m_speed;

  public SetLiftSpeed(LiftSubsystem lift, double speed) {
    super(lift);
    m_speed = speed;
  }

  @Override
  protected void doAction() {
    getTarget().setManualSpeed(m_speed);
  }
}