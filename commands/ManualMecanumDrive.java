package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.subsystems.MecanumDriveSubsystem;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMecanumDrive extends Command {
  private MecanumDriveSubsystem m_drive;
  private AnalogControl m_xControl, m_yControl, m_rControl;
  private JoystickButton m_0nudge, m_1nudge, m_2nudge, m_3nudge;
  private JoystickButton m_xySpeedToggle, m_rSpeedToggle;
  // o = right, 1 = up, 2 = left, 3 = down;
  private int m_nudgeDirection = 0;
  private long m_nudgeStart;
  private static final int 
    NUDGE_TIME = (int) (Config.getInstance().getDouble("mecanum", "nudgeTime") * 1000.0),
    NUDGE_END_TIME = (int) (Config.getInstance().getDouble("mecanum", "nudgeEndTime") * 1000.0);
  private static final double
    LOW_XY_SPEED = Config.getInstance().getDouble("mecanum", "lowXYSpeed"),
    LOW_R_SPEED = Config.getInstance().getDouble("mecanum", "lowRSpeed"),
    HIGH_XY_SPEED = Config.getInstance().getDouble("mecanum", "highXYSpeed"),
    HIGH_R_SPEED = Config.getInstance().getDouble("mecanum", "highRSpeed"),
    NUDGE_POWER = Config.getInstance().getDouble("mecanum", "nudgePower"),
    NUDGE_END_POWER = Config.getInstance().getDouble("mecanum", "nudgeEndPower");
  private enum NudgeMode { kNone, kMain, kEnd };
  private NudgeMode m_nudgeMode = NudgeMode.kNone;
  private boolean m_nudgeDebounce = false;

  public ManualMecanumDrive(MecanumDriveSubsystem drive) {
    requires(drive);
    m_drive = drive;

    m_xControl = drive.getAnalogControl("x");
    m_yControl = drive.getAnalogControl("y");
    m_rControl = drive.getAnalogControl("r");

    m_0nudge = drive.getDigitalControl("nudgeLeft");
    m_1nudge = drive.getDigitalControl("nudgeForward");
    m_2nudge = drive.getDigitalControl("nudgeRight");
    m_3nudge = drive.getDigitalControl("nudgeBack");
    m_xySpeedToggle = drive.getDigitalControl("xySpeedToggle");
    m_rSpeedToggle = drive.getDigitalControl("rSpeedToggle");
  }

  private void startNudge(int direction) {
    m_nudgeDebounce = true;
    m_nudgeDirection = direction;
    m_nudgeStart = System.currentTimeMillis();
  }

  @Override
  protected void execute() {
    if (m_nudgeDebounce) { 
      m_nudgeDebounce = m_0nudge.get() || m_1nudge.get() || m_2nudge.get() || m_3nudge.get();
    } else if (m_0nudge.get()) {
      startNudge(0);
    } else if (m_1nudge.get()) {
      startNudge(1);
    } else if (m_2nudge.get()) {
      startNudge(2);
    } else if (m_3nudge.get()) {
      startNudge(3);
    }
    long delta = System.currentTimeMillis() - m_nudgeStart;
    if (m_nudgeMode == NudgeMode.kMain && delta > NUDGE_TIME) {
      m_nudgeStart += NUDGE_TIME;
      m_nudgeMode = NudgeMode.kEnd;
      // Rotate nudge 180deg to drive in the opposite direction.
      m_nudgeDirection = (m_nudgeDirection + 2) % 4;
    } else if (m_nudgeMode == NudgeMode.kEnd && delta > NUDGE_END_TIME) {
      m_nudgeStart = 0;
      m_nudgeMode = NudgeMode.kNone;
    }
    if (m_nudgeMode != NudgeMode.kNone) {
      double power = m_nudgeMode == NudgeMode.kEnd ? NUDGE_END_POWER : NUDGE_POWER;
      switch (m_nudgeDirection) {
        case 0:
        m_drive.driveCartesian(0.0, power, 0.0);
        break;
        case 1:
        m_drive.driveCartesian(power, 0.0, 0.0);
        break;
        case 2:
        m_drive.driveCartesian(0.0, -power, 0.0);
        break;
        case 3:
        m_drive.driveCartesian(-power, 0.0, 0.0);
        break;
      }
    } else {
      double xyThrottle = m_xySpeedToggle.get() ? HIGH_XY_SPEED : LOW_XY_SPEED;
      double rThrottle = m_rSpeedToggle.get() ? HIGH_R_SPEED : LOW_R_SPEED;
      m_drive.driveCartesian(
        m_yControl.getValue() * xyThrottle,
        m_xControl.getValue() * xyThrottle,
        m_rControl.getValue() * rThrottle
      );
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}