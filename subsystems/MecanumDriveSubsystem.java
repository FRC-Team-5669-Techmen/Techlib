package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.boscotech.techlib.commands.ManualMecanumDrive;
import edu.boscotech.techlib.util.TalonSRXAdapter;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * An adapter class for {@link MecanumDrive} that also sets up its motor
 * controllers.
 */
public class MecanumDriveSubsystem extends BetterSubsystem {
  MecanumDrive m_drive;

  public MecanumDriveSubsystem() {
    super("Mecanum Drive", "mecanum", "MecanumDrive");
  }

  @Override
  protected void setup() {
    m_drive = new MecanumDrive(
      new TalonSRXAdapter(getCfgInt("frontLeft")),
      new TalonSRXAdapter(getCfgInt("rearLeft")),
      new TalonSRXAdapter(getCfgInt("frontRight")),
      new TalonSRXAdapter(getCfgInt("rearRight"))
    );
    m_drive.initSendable(getNetworkData());
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's 
   * speed is independent from its angle or rotation rate. If 
   * {@link startNetworkTablesControl} was previously called, this method will
   * turn network tables control back off.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Forward is 
   *                  positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Right is
   *                  positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
    stopNetworkTablesControl();
    // WPILib makes the X axis forward / backward for some reason.
    m_drive.driveCartesian(xSpeed, ySpeed, zRotation);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured counter-clockwise from straight ahead. The speed at
   * which the robot drives (translation) is independent from its angle or 
   * rotation rate. If {@link startNetworkTablesControl} was previously called,
   * this method will turn network tables control back off.
   *
   * @param magnitude The robot's speed at a given angle [-1.0..1.0]. Forward is
   *                  positive.
   * @param angle     The angle around the Z axis at which the robot drives in 
   *                  degrees [-180..180].
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void drivePolar(double magnitude, double angle, double zRotation) {
    stopNetworkTablesControl();
    m_drive.drivePolar(magnitude, angle, zRotation);
  }

  @Override
  protected void enterSafeState() {
    m_drive.driveCartesian(0.0, 0.0, 0.0);
  }

  @Override
  public Command createDefaultTeleopCommand() {
    return new ManualMecanumDrive(this);
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}