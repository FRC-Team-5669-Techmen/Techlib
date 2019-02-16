package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.TestPneumaticRaiser;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;

public class PneumaticRaiserSubsystem extends BetterSubsystem {
  private DoubleSolenoid frontSolenoid, backSolenoid;

  public PneumaticRaiserSubsystem() {
    super("Pneumatic Raiser", "raiser", "PneumaticRaiser");
  }

  public PneumaticRaiserSubsystem(String hrName, String cfgName) {
    super(hrName, cfgName, "PneumaticRaiser");
  }

  @Override
  protected void setup() {
    frontSolenoid = new DoubleSolenoid(
      getCfgInt("extendFront"), getCfgInt("retractFront")
    );
    getNetworkData().addBooleanProperty("Front Cylinders", 
      this::isFrontCylindersExtended, this::setFrontCylindersExtended
    );
    backSolenoid = new DoubleSolenoid(
      getCfgInt("extendBack"), getCfgInt("retractBack")
    );
    getNetworkData().addBooleanProperty("Back Cylinders", 
      this::isBackCylindersExtended, this::setBackCylindersExtended
    );
  }

  public void extendFrontCylinders() {
    frontSolenoid.set(Value.kForward);
  }

  public void extendBackCylinders() {
    backSolenoid.set(Value.kForward);
  }

  public void extendAllCylinders() {
    extendFrontCylinders();
    extendBackCylinders();
  }

  public void retractFrontCylinders() {
    frontSolenoid.set(Value.kForward);
  }

  public void retractBackCylinders() {
    backSolenoid.set(Value.kForward);
  }

  public void retractAllCylinders() {
    retractFrontCylinders();
    retractBackCylinders();
  }

  public boolean isFrontCylindersExtended() {
    return frontSolenoid.get() == Value.kForward;
  }

  public void setFrontCylindersExtended(boolean extend) {
    frontSolenoid.set(extend ? Value.kForward : Value.kReverse);
  }

  public boolean isBackCylindersExtended() {
    return backSolenoid.get() == Value.kForward;
  }

  public void setBackCylindersExtended(boolean extend) {
    backSolenoid.set(extend ? Value.kForward : Value.kReverse);
  }

  @Override
  protected void enterSafeState() {
    retractAllCylinders();
  }

  @Override
  public Command createDefaultTestCommand() {
    return new TestPneumaticRaiser(this);
  }
}