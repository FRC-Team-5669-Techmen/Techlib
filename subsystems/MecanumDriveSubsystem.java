package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.ManualMecanumDrive;
import edu.boscotech.techlib.commands.TestMecanumDrive;
import edu.boscotech.techlib.util.DefaultCommandCreator;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

/**
 * An adapter class for {@link MecanumDrive} that also sets up its motor
 * controllers.
 */
public class MecanumDriveSubsystem extends Subsystem 
  implements DefaultCommandCreator {
  private SendableBuilderImpl networkData = new SendableBuilderImpl();
  private boolean networkTablesControlOn = false;
  MecanumDrive drive = new MecanumDrive(
    new Victor(1),
    new Victor(2),
    new Victor(3),
    new Victor(4)
  );

  public MecanumDriveSubsystem() {
    networkData.setTable(NetworkTableInstance.getDefault()
      .getTable("Subsystems").getSubTable("MecanumDrive"));
    drive.initSendable(networkData);
    networkData.updateTable();
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's 
   * speed is independent from its angle or rotation rate. If 
   * {@link startNetworkTablesControl} was previously called, this method will
   * turn network tables control back off.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is 
   *                  positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is
   *                  positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
    stopNetworkTablesControl();
    drive.driveCartesian(ySpeed, xSpeed, zRotation);
    networkData.updateTable();
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
    drive.drivePolar(magnitude, angle, zRotation);
    networkData.updateTable();
  }

  /**
   * Allows this subsystem to be driven manually via the NetworkTables api,
   * which can be accessed from most dashboard systems (usually Shuffleboard).
   * If control is already enabled, this method will exit early, making it safe
   * to call frequently.
   */
  public void startNetworkTablesControl() {
    if (networkTablesControlOn) return;
    drive.driveCartesian(0.0, 0.0, 0.0);
    networkData.startListeners();
    networkData.updateTable();
    networkTablesControlOn = true;
  }

  /**
   * Stops this subsystem from being driven manually via the NetworkTables api,
   * which is accessed by most dashboard systems (usually Shuffleboard). If
   * control is already disabled, this method will exit early, making it safe
   * to call frequently.
   */
  public void stopNetworkTablesControl() {
    if (!networkTablesControlOn) return;
    drive.driveCartesian(0.0, 0.0, 0.0);
    networkData.stopListeners();
    networkData.updateTable();
    networkTablesControlOn = false;
  }

  @Override
  public void initDefaultCommand() { }

  @Override
  public Command createDefaultTeleopCommand() {
    return new ManualMecanumDrive(this);
  }

  @Override
  public Command createDefaultTestCommand() {
    return new TestMecanumDrive(this);
  }
}