package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class TestMecanumDrive extends Command {
  private MecanumDriveSubsystem m_drive;
  
  public TestMecanumDrive(MecanumDriveSubsystem drive) {
    requires(drive);
    m_drive = drive;
  }

  @Override
  public synchronized void start() {
    m_drive.startNetworkTablesControl();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
  
  @Override
  protected void end() {
    m_drive.stopNetworkTablesControl();
  }
}