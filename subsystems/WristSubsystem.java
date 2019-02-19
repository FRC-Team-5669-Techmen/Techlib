package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.SetEncodedMotor;
import edu.boscotech.techlib.commands.CalibrateEncodedMotor;
import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.wpi.first.wpilibj.command.Command;

public class WristSubsystem extends EncodedMotorSubsystem {
  public WristSubsystem() {
    super("Wrist", "wrist", "Wrist");
  }

  public WristSubsystem(String hrName, String cfgName) {
    super(hrName, cfgName, "Wrist");
  }

  @Override
  protected void setup() {
    setupMotor(getCfgInt("motor"), "motionProfile", false, true);
  }

  public void deploy() {
    setPosition(0.0);
  }

  public boolean isDeployed() {
    return isNLimitEngaged();
  }

  public void retract() {
    setPosition(1.0);
  }

  public boolean isRetracted() {
    return isPLimitEngaged();
  }

  @Override
  public Command createDefaultTeleopCommand() {
    getDigitalControl("deploy").whenPressed(new SetEncodedMotor(this, 0.0));
    getDigitalControl("retract").whenPressed(new SetEncodedMotor(this, 1.0));
    getDigitalControl("calibrate").whenPressed(new CalibrateEncodedMotor(this));
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}