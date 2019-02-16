package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class SetLiftToBottom extends Command {
  private LiftSubsystem m_lift;

  public SetLiftToBottom(LiftSubsystem lift) {
    m_lift = lift;
  }

  @Override
  public synchronized void start() {
    m_lift.goCompletelyDown();
  }

  @Override
  protected boolean isFinished() {
    return m_lift.isCompletelyDown();
  }

  @Override
  protected void end() { }
}