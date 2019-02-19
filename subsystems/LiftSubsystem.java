package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.DriveEncodedMotor;
import edu.boscotech.techlib.commands.GenericTestCommand;
import edu.wpi.first.wpilibj.command.Command;

public class LiftSubsystem extends EncodedMotorSubsystem {
  public LiftSubsystem() {
    super("Lift", "lift", "Lift");
  }

  public LiftSubsystem(String hrName, String cfgName) {
    super(hrName, cfgName, "Lift");
  }

  @Override
  protected void setup() {
    setupMotor(getCfgInt("motor1"), "motionProfile", true, true);
  }

  public void goCompletelyDown() {
    setPosition(0.0);
  }

  public boolean isCompletelyDown() {
    return getPosition() < 0.01;
  }

  public void goCompletelyUp() {
    setPosition(1.0);
  }

  public boolean isCompletelyUp() {
    return getPosition() > 0.99;
  }

  @Override
  public Command createDefaultTeleopCommand() {
    getDigitalControl("up").whenPressed(new DriveEncodedMotor(this, 0.8));
    getDigitalControl("up").whenReleased(new DriveEncodedMotor(this, 0.0));
    getDigitalControl("down").whenPressed(new DriveEncodedMotor(this, -0.8));
    getDigitalControl("down").whenReleased(new DriveEncodedMotor(this, 0.0));
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return new GenericTestCommand(this);
  }
}