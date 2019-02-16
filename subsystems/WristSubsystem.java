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
    // TODO: Config stuff for motion profile.
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