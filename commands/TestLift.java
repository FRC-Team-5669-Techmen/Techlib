package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.LiftSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class TestLift extends Command {
  private LiftSubsystem m_lift;

  public TestLift(LiftSubsystem lift) {
    m_lift = lift;
  }

  @Override
  public synchronized void start() {
    m_lift.startNetworkTablesControl();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    m_lift.stopNetworkTablesControl();
  }
}