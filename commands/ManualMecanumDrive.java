package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMecanumDrive extends Command {
  private MecanumDriveSubsystem m_drive;
  private AnalogControl m_xControl, m_yControl, m_rControl, m_xyThrottle, 
    m_rThrottle;
  private JoystickButton m_0nudge, m_1nudge, m_2nudge, m_3nudge;
  // o = right, 1 = up, 2 = left, 3 = down;
  private int m_nudgeCounter = 0, m_nudgeDirection = 0;
  private long m_nudgeStart;
  private static final int NUDGE_TIME = 100;
  private static final double NUDGE_SPEED = 0.5;
  private boolean m_nudgeFlag = false;

  public ManualMecanumDrive(MecanumDriveSubsystem drive) {
    requires(drive);
    m_drive = drive;

    m_xControl = drive.getAnalogControl("x");
    m_yControl = drive.getAnalogControl("y");
    m_rControl = drive.getAnalogControl("r");
    m_xyThrottle = drive.getAnalogControl("xyThrottle");
    m_rThrottle = drive.getAnalogControl("rThrottle");

    m_0nudge = drive.getDigitalControl("nudgeLeft");
    m_1nudge = drive.getDigitalControl("nudgeForward");
    m_2nudge = drive.getDigitalControl("nudgeRight");
    m_3nudge = drive.getDigitalControl("nudgeBack");
  }

  private void startNudge(int direction) {
    m_nudgeFlag = true;
    m_nudgeDirection = direction;
    m_nudgeStart = System.currentTimeMillis();
  }

  @Override
  protected void execute() {
    if (m_nudgeFlag) { 
      m_nudgeFlag = m_0nudge.get() || m_1nudge.get() || m_2nudge.get() || m_3nudge.get();
    } else if (m_0nudge.get()) {
      startNudge(0);
    } else if (m_1nudge.get()) {
      startNudge(1);
    } else if (m_2nudge.get()) {
      startNudge(2);
    } else if (m_3nudge.get()) {
      startNudge(3);
    }
    if ((System.currentTimeMillis() - m_nudgeStart) < NUDGE_TIME) {
      switch (m_nudgeDirection) {
        case 0:
        m_drive.driveCartesian(0.0, NUDGE_SPEED, 0.0);
        break;
        case 1:
        m_drive.driveCartesian(NUDGE_SPEED, 0.0, 0.0);
        break;
        case 2:
        m_drive.driveCartesian(0.0, -NUDGE_SPEED, 0.0);
        break;
        case 3:
        m_drive.driveCartesian(-NUDGE_SPEED, 0.0, 0.0);
        break;
      }
    } else {
      m_drive.driveCartesian(
        m_yControl.getValue() * m_xyThrottle.getValue(),
        m_xControl.getValue() * m_xyThrottle.getValue(),
        m_rControl.getValue() * m_rThrottle.getValue()
      );
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}