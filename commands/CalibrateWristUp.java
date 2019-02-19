package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class CalibrateWristUp extends Command {
  protected WristSubsystem m_wrist;

  public CalibrateWristUp(WristSubsystem wrist) {
    m_wrist = wrist;
  }

  @Override
  protected void execute() {
    m_wrist.setManualSpeed(0.2);
  }

  @Override
  protected boolean isFinished() {
    return m_wrist.isRetracted();
  }

  @Override
  protected void end() {
    m_wrist.setManualSpeed(0.0);
  }
}