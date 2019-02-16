package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.commands.TestPneumaticRaiser;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.util.DefaultCommandCreator;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

public class PneumaticRaiserSubsystem extends Subsystem
  implements DefaultCommandCreator {
  private static Config cfg = Config.getInstance();
  private SendableBuilderImpl networkData = new SendableBuilderImpl();
  private boolean networkTablesControlOn = false;
  private DoubleSolenoid frontSolenoid = new DoubleSolenoid(
    cfg.getInt("subsystems", "raiser", "extendFront"),
    cfg.getInt("subsystems", "raiser", "retractFront")
  ), backSolenoid = new DoubleSolenoid(
    cfg.getInt("subsystems", "raiser", "extendBack"),
    cfg.getInt("subsystems", "raiser", "retractBack")
  );

  public PneumaticRaiserSubsystem() {
    networkData.setTable(NetworkTableInstance.getDefault()
      .getTable("Subsystems").getSubTable("PneumaticRaiser"));
    networkData.setSmartDashboardType("PneumaticRaiser");
    networkData.setActuator(true);
    networkData.setSafeState(this::retractAllCylinders);
    networkData.addBooleanProperty("Front Cylinders", 
      this::isFrontCylindersExtended, this::setFrontCylindersExtended);
    networkData.addBooleanProperty("Back Cylinders", 
      this::isBackCylindersExtended, this::setBackCylindersExtended);
  }

  /**
   * Allows this subsystem to be driven manually via the NetworkTables api,
   * which can be accessed from most dashboard systems (usually Shuffleboard).
   * If control is already enabled, this method will exit early, making it safe
   * to call frequently.
   */
  public void startNetworkTablesControl() {
    if (networkTablesControlOn) return;
    retractAllCylinders();
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
    retractAllCylinders();
    networkData.stopListeners();
    networkData.updateTable();
    networkTablesControlOn = false;
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
  public void initDefaultCommand() { }

  @Override
  public Command createDefaultTeleopCommand() {
    return null;
  }

  @Override
  public Command createDefaultTestCommand() {
    return new TestPneumaticRaiser(this);
  }
}