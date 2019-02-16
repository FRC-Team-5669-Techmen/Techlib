package edu.boscotech.techlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.util.DefaultCommandCreator;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

public class LiftSubsystem extends Subsystem implements DefaultCommandCreator {
  private TalonSRX m_motor;
  private int m_minValue, m_maxValue, m_tolerance;
  private double m_sensorUnitsPerInch;
  private SendableBuilderImpl networkData = new SendableBuilderImpl();
  private boolean networkTablesControlOn = false;

  public LiftSubsystem() {
    networkData.setTable(NetworkTableInstance.getDefault()
      .getTable("Subsystems").getSubTable("Lift"));
    networkData.setSmartDashboardType("Lift");
    networkData.setActuator(true);
    networkData.setSafeState(this::goNeutral);
    networkData.addDoubleProperty("Height", this::getPosition, 
      this::goToPosition);
    Config cfg = Config.getInstance();
    m_motor = new TalonSRX(cfg.getInt("subsystems", "lift", "motor1"));
    // TODO: Config stuff for motion profile.
  }

  /**
   * Allows this subsystem to be driven manually via the NetworkTables api,
   * which can be accessed from most dashboard systems (usually Shuffleboard).
   * If control is already enabled, this method will exit early, making it safe
   * to call frequently.
   */
  public void startNetworkTablesControl() {
    if (networkTablesControlOn) return;
    goNeutral();
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
    goNeutral();
    networkData.stopListeners();
    networkData.updateTable();
    networkTablesControlOn = false;
  }

  public void goCompletelyDown() {
    m_motor.set(ControlMode.MotionMagic, m_minValue);
  }

  public boolean isCompletelyDown() {
    return m_motor.getSelectedSensorPosition() <= m_minValue + m_tolerance;
  }

  public void goCompletelyUp() {
    m_motor.set(ControlMode.MotionMagic, m_maxValue);
  }

  public boolean isCompletelyUp() {
    return m_motor.getSelectedSensorPosition() >= m_maxValue - m_tolerance;
  }

  public void goToPosition(double inches) {
    m_motor.set(ControlMode.MotionMagic, inches * m_sensorUnitsPerInch);
  }

  public double getPosition() {
    return m_motor.getSelectedSensorPosition() / m_sensorUnitsPerInch;
  }

  public boolean isAtPosition(double inches) {
    return Math.abs(
      m_motor.getSelectedSensorPosition() - inches * m_sensorUnitsPerInch
    ) < m_tolerance;
  }

  public void goNeutral() {
    m_motor.neutralOutput();
  }

  @Override
  protected void initDefaultCommand() { }

  @Override
  public Command createDefaultTeleopCommand() {
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return null;
  }
}