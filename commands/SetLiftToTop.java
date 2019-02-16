package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class SetLiftToTop extends Command {
  private LiftSubsystem m_lift;

  public SetLiftToTop(LiftSubsystem lift) {
    m_lift = lift;
  }

  @Override
  public synchronized void start() {
    m_lift.goCompletelyUp();
  }

  @Override
  protected boolean isFinished() {
    return m_lift.isCompletelyUp();
  }

  @Override
  protected void end() { }
}