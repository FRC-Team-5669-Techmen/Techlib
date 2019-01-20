package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.config.Controls;
import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMecanumDrive extends Command {
  private MecanumDriveSubsystem m_drive;
  private AnalogControl m_xControl, m_yControl, m_rControl, m_xyThrottle, 
    m_rThrottle;

  public ManualMecanumDrive(MecanumDriveSubsystem drive) {
    requires(drive);
    m_drive = drive;

    Controls con = Controls.getInstance();
    m_xControl = con.getAnalogControl("mecanum", "x");
    m_yControl = con.getAnalogControl("mecanum", "y");
    m_rControl = con.getAnalogControl("mecanum", "r");
    m_xyThrottle = con.getAnalogControl("mecanum", "xyThrottle");
    m_rThrottle = con.getAnalogControl("mecanum", "rThrottle");
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