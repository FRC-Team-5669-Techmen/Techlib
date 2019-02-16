package edu.boscotech.techlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.wpi.first.wpilibj.command.Command;

public class LiftSubsystem extends BetterSubsystem {
  private TalonSRX m_motor;
  private int m_minValue, m_maxValue, m_tolerance;
  private double m_sensorUnitsPerInch;

  public LiftSubsystem() {
    super("Lift", "lift", "Lift");
  }

  public LiftSubsystem(String hrName, String cfgName) {
    super(hrName, cfgName, "Lift");
  }

  @Override
  protected void setup() {
    getNetworkData().addDoubleProperty("Height", this::getPosition, 
      this::goToPosition);
    m_motor = new TalonSRX(getCfgInt("motor1"));
    // TODO: Config stuff for motion profile.
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
  protected void enterSafeState() {
    goNeutral();
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}