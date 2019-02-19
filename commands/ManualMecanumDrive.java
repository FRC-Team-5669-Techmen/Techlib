package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMecanumDrive extends Command {
  private MecanumDriveSubsystem m_drive;
  private AnalogControl m_xControl, m_yControl, m_rControl, m_xyThrottle, 
    m_rThrottle;

  public ManualMecanumDrive(MecanumDriveSubsystem drive) {
    requires(drive);
    m_drive = drive;

    m_xControl = drive.getAnalogControl("x");
    m_yControl = drive.getAnalogControl("y");
    m_rControl = drive.getAnalogControl("r");
    m_xyThrottle = drive.getAnalogControl("xyThrottle");
    m_rThrottle = drive.getAnalogControl("rThrottle");
  }

  @Override
  protected void execute() {
    m_drive.driveCartesian(
      m_yControl.getValue() * m_xyThrottle.getValue(),
      m_xControl.getValue() * m_xyThrottle.getValue(),
      m_rControl.getValue() * m_rThrottle.getValue()
    );
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}