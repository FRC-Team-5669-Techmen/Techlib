package edu.boscotech.techlib.util;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * Allows a Talon SRX (can-based) speed controller to be used as a regular
 * PID-controlled ESC. This is mainly used to take advantage of older code that
 * only accepts regular speed controllers.
 */
public class TalonSRXAdapter implements SpeedController {
  private TalonSRX talon;

  private void setup() {
    talon.setNeutralMode(NeutralMode.Brake);
  }

  public TalonSRXAdapter(TalonSRX base) {
    talon = base;
    setup();
  }

  public TalonSRXAdapter(int canID) {
    talon = new TalonSRX(canID);
    setup();
  }

  public void set(double speed) {
    talon.set(ControlMode.PercentOutput, speed);
  }

  public double get() {
    return talon.getMotorOutputPercent();
  }

  public void setInverted(boolean isInverted) {
    talon.setInverted(isInverted);
  }

  public boolean getInverted() {
    return talon.getInverted();
  }

  public void disable() {
    talon.neutralOutput();
  }

  public void stopMotor() {
    talon.set(ControlMode.PercentOutput, 0.0);
  }

  public void pidWrite(double output) {
    talon.set(ControlMode.PercentOutput, 0.0);
  }
}