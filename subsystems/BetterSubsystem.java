package edu.boscotech.techlib.subsystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.boscotech.techlib.config.AnalogControl;
import edu.boscotech.techlib.config.Config;
import edu.boscotech.techlib.config.Controls;
import edu.boscotech.techlib.util.DefaultCommandCreator;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

public abstract class BetterSubsystem extends Subsystem 
  implements DefaultCommandCreator {
  private static Map<String, Integer> s_numInstances = new HashMap<>();
  private static List<BetterSubsystem> s_instances = new ArrayList<>();
  private SendableBuilderImpl m_networkData = new SendableBuilderImpl();
  private boolean m_networkTablesControlOn = false;
  private String m_cfgName;

  public BetterSubsystem(String hrName, String cfgName, String ntType) {
    m_cfgName = cfgName;
    s_instances.add(this);

    int instances = s_numInstances.getOrDefault(cfgName, 0);
    instances++;
    if (instances > 1) {
      hrName += instances;
    }
    s_numInstances.put(cfgName, instances);

    m_networkData.setTable(NetworkTableInstance.getDefault()
      .getTable("Subsystems").getSubTable(hrName));
    m_networkData.setSmartDashboardType(ntType);
    m_networkData.setActuator(true);
    m_networkData.setSafeState(this::enterSafeState);

    setup();
  }

  /**
   * Implement all your setup logic here, rather than in the constructor. This
   * allows you to easily have multiple constructors for different naming
   * conventions while using the same setup code in all of them. This function
   * is called automatically at the end of the superclass constructor.
   */
  protected abstract void setup();

  /**
   * Force all constructed instances of this class to update their network
   * table data. This is reccomended to be called at the end of each update
   * loop.
   */
  public static void updateAllSubsystemNetworkTables() {
    for (BetterSubsystem subsystem : s_instances) {
      subsystem.updateNetworkTable();
    }
  }

  private void updateNetworkTable() {
    m_networkData.updateTable();
  }

  /**
   * Allows this subsystem to be driven manually via the NetworkTables api,
   * which can be accessed from most dashboard systems (usually Shuffleboard).
   * If control is already enabled, this method will exit early, making it safe
   * to call frequently.
   */
  public void startNetworkTablesControl() {
    if (m_networkTablesControlOn) return;
    m_networkData.startLiveWindowMode();
    m_networkData.updateTable();
    m_networkTablesControlOn = true;
  }

  /**
   * Stops this subsystem from being driven manually via the NetworkTables api,
   * which is accessed by most dashboard systems (usually Shuffleboard). If
   * control is already disabled, this method will exit early, making it safe
   * to call frequently.
   */
  public void stopNetworkTablesControl() {
    if (!m_networkTablesControlOn) return;
    m_networkData.stopLiveWindowMode();
    m_networkData.updateTable();
    m_networkTablesControlOn = true;
  }

  private String[] extendPath(String[] path) {
    String[] fullPath = new String[path.length + 1];
    fullPath[0] = m_cfgName;
    for (int i = 0; i < path.length; i++) {
      fullPath[i+1] = path[i];
    }
    return fullPath;
  }

  /**
   * Get a string value from the configuration file. The configuration name of
   * this subsystem (provided to the constructor) is automatically prepended
   * to the path.
   * @param path The path to the value inside of the subsystem's section of the
   *             config file.
   * @return The contents of the value if found.
   */
  protected String getCfgString(String... path) {
    return Config.getInstance().getString(extendPath(path));
  }

  /**
   * Get an int value from the configuration file. The configuration name of
   * this subsystem (provided to the constructor) is automatically prepended
   * to the path.
   * @param path The path to the value inside of the subsystem's section of the
   *             config file.
   * @return The contents of the value if found.
   */
  protected int getCfgInt(String... path) {
    return Config.getInstance().getInt(extendPath(path));
  }

  /**
   * Get an double value from the configuration file. The configuration name of
   * this subsystem (provided to the constructor) is automatically prepended
   * to the path.
   * @param path The path to the value inside of the subsystem's section of the
   *             config file.
   * @return The contents of the value if found.
   */
  protected double getCfgDouble(String... path) {
    return Config.getInstance().getDouble(extendPath(path));
  }

  /**
   * Gets the network table that was automatically generated for this subsystem.
   * @return The network table.
   */
  protected SendableBuilderImpl getNetworkData() {
    return m_networkData;
  }

  /**
   * Gets an analog control associated with this subsystem.
   * @param name The name of the control, as listed in the config file.
   * @return The control described in the config file, if found.
   */
  public AnalogControl getAnalogControl(String name) {
    return Controls.getInstance().getAnalogControl(m_cfgName, "controls", name);
  }

  /**
   * Gets a digital control (button) associated with this subsystem.
   * @param name The name of the control, as listed in the config file.
   * @return The control described in the config file, if found.
   */
  public JoystickButton getDigitalControl(String name) {
    return Controls.getInstance()
      .getDigitalControl(m_cfgName, "controls", name);
  }

  /**
   * Put code to put the subsystem into a safe state here. This will be
   * called whenever network table control starts or stops.
   */
  protected void enterSafeState() { }

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