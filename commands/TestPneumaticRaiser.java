package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.PneumaticRaiserSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class TestPneumaticRaiser extends Command {
  private PneumaticRaiserSubsystem m_rasor; // Oh no he's infiltrated again

  public TestPneumaticRaiser(PneumaticRaiserSubsystem raiser) {
    m_rasor = raiser;
  }

  @Override
  public synchronized void start() {
    m_rasor.startNetworkTablesControl();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    m_rasor.stopNetworkTablesControl();
  }
}