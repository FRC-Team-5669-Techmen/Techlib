package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class CalibrateWristDown extends Command {
  protected WristSubsystem m_wrist;

  public CalibrateWristDown(WristSubsystem wrist) {
    m_wrist = wrist;
  }

  @Override
  protected void execute() {
    m_wrist.setManualSpeed(-0.2);
  }

  @Override
  protected boolean isFinished() {
    return m_wrist.isDeployed();
  }

  @Override
  protected void end() {
    m_wrist.setManualSpeed(0.0);
  }
}