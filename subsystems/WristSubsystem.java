package edu.boscotech.techlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.wpi.first.wpilibj.command.Command;

public class WristSubsystem extends BetterSubsystem {
  private TalonSRX m_motor;
  private int m_deployedPosition, m_retractedPosition, m_threshold;

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
    return isAtPosition(m_deployedPosition);
  }

  public void retract() {
    m_motor.set(ControlMode.MotionMagic, m_retractedPosition);
  }

  public boolean isRetracted() {
    return isAtPosition(m_retractedPosition);
  }

  public void goNeutral() {
    m_motor.neutralOutput();
  }

  @Override
  protected void enterSafeState() {
    goNeutral();
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}