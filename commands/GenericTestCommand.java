package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.BetterSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class GenericTestCommand extends Command {
  private BetterSubsystem m_subsystem;

  public GenericTestCommand(BetterSubsystem subsystem) {
    requires(subsystem);
    m_subsystem = subsystem;
  }

  @Override
  public synchronized void start() {
    m_subsystem.startNetworkTablesControl();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    m_subsystem.stopNetworkTablesControl();
  }
}