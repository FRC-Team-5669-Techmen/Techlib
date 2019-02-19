package edu.boscotech.techlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.boscotech.techlib.commands.RetractWrist;
import edu.boscotech.techlib.commands.CalibrateWrist;
import edu.boscotech.techlib.commands.DeployWrist;
import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.wpi.first.wpilibj.command.Command;

public class WristSubsystem extends BetterSubsystem {
  private TalonSRX m_motor;
  private int m_deployedPosition, m_retractedPosition, m_threshold = 50000;

  public WristSubsystem() {
    super("Wrist", "wrist", "Wrist");
  }

  public WristSubsystem(String hrName, String cfgName) {
    super(hrName, cfgName, "Wrist");
  }

  @Override
  protected void setup() {
    m_motor = new TalonSRX(getCfgInt("motor"));
    getNetworkData().addDoubleProperty("Position", 
      this::getPosition, this::setPosition);
    
    m_motor.setSensorPhase(true); // Sensor is backwards.
    m_motor.configMotionCruiseVelocity(getCfgInt("motionProfile", "cruiseSpeed"));
    m_motor.configMotionAcceleration(getCfgInt("motionProfile", "acceleration"));
    m_motor.config_kF(0, getCfgDouble("motionProfile", "f"));
    m_motor.config_kP(0, getCfgDouble("motionProfile", "p"));
    m_motor.config_kI(0, getCfgDouble("motionProfile", "i"));
    m_motor.config_kD(0, getCfgDouble("motionProfile", "d"));

    m_motor.configClearPositionOnLimitR(true, 100);
    m_deployedPosition = 0;
    m_retractedPosition = getCfgInt("motionProfile", "distance");
  }

  private boolean isAtPosition(int sensorUnits) {
    return Math.abs(
      m_motor.getSelectedSensorPosition() - sensorUnits
    ) <= m_threshold;
  }

  private double getPosition() {
    return m_motor.getSelectedSensorPosition();
  }

  private void setPosition(double position) {
    m_motor.set(ControlMode.MotionMagic, (int) position);
  }

  public void deploy() {
    m_motor.set(ControlMode.MotionMagic, m_deployedPosition);
  }

  public boolean isDeployed() {
    return m_motor.getSensorCollection().isRevLimitSwitchClosed();
  }

  public void retract() {
    m_motor.set(ControlMode.MotionMagic, m_retractedPosition);
  }

  public boolean isRetracted() {
    return m_motor.getSensorCollection().isFwdLimitSwitchClosed();
  }

  public void goNeutral() {
    m_motor.neutralOutput();
  }

  public void setManualSpeed(double speed) {
    m_motor.set(ControlMode.PercentOutput, speed);
  }

  public void printCalibrationInfo() {
    System.out.println("Motion profile distance: " + m_retractedPosition);
  }

  @Override
  public void periodic() {
    super.periodic();
    if (m_motor.getSensorCollection().isFwdLimitSwitchClosed()) {
      m_retractedPosition = m_motor.getSelectedSensorPosition();
    } else if (m_motor.getSensorCollection().isRevLimitSwitchClosed()) {
      m_retractedPosition -= m_motor.getSelectedSensorPosition();
      m_motor.setSelectedSensorPosition(0);
    }
  }

  @Override
  protected void enterSafeState() {
    goNeutral();
  }

  @Override
  public Command createDefaultTeleopCommand() {
    getDigitalControl("deploy").whenPressed(new DeployWrist(this));
    getDigitalControl("retract").whenPressed(new RetractWrist(this));
    getDigitalControl("calibrate").whenPressed(new CalibrateWrist(this));
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}