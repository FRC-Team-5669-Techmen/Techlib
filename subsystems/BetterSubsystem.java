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

public class BetterSubsystem extends Subsystem 
  implements DefaultCommandCreator {
  private static Map<String, Integer> s_numInstances = new HashMap<>();
  private static List<BetterSubsystem> s_instances = new ArrayList<>();
  private SendableBuilderImpl m_networkData = new SendableBuilderImpl();
  private boolean m_networkTablesControlOn = false;
  private String m_cfgName;

  public BetterSubsystem(String hrName, String cfgName, Runnable safeState) {
    m_cfgName = cfgName;
    s_instances.add(this);

    int instances = s_numInstances.getOrDefault(cfgName, 0);
    instances++;
    if (instances > 1) {
      hrName += instances;
    }
    m_networkData.setTable(NetworkTableInstance.getDefault()
      .getTable("Subsystems").getSubTable(hrName));
    s_numInstances.put(cfgName, instances);
  }

  public static void updateAllSubsystemNetworkTables() {
    for (BetterSubsystem subsystem : s_instances) {
      subsystem.updateNetworkTable();
    }
  }

  private void updateNetworkTable() {
    m_networkData.updateTable();
  }

  public void startNetworkTablesControl() {
    if (m_networkTablesControlOn) return;
    m_networkData.startLiveWindowMode();
    m_networkData.updateTable();
    m_networkTablesControlOn = true;
  }

  public void stopNetworkTablesControl() {
    if (!m_networkTablesControlOn) return;
    m_networkData.stopLiveWindowMode();
    m_networkData.updateTable();
    m_networkTablesControlOn = true;
  }

  private String[] extendPath(String[] path) {
    String[] fullPath = new String[path.length + 2];
    fullPath[0] = "subsystems";
    fullPath[1] = m_cfgName;
    for (int i = 0; i < path.length; i++) {
      fullPath[i+2] = path[i];
    }
    return fullPath;
  }

  protected String getCfgString(String... path) {
    return Config.getInstance().getString(extendPath(path));
  }

  protected int getCfgInt(String... path) {
    return Config.getInstance().getInt(extendPath(path));
  }

  protected double getCfgDouble(String... path) {
    return Config.getInstance().getDouble(extendPath(path));
  }

  public AnalogControl getAnalogControl(String name) {
    return Controls.getInstance().getAnalogControl(m_cfgName, "controls", name);
  }

  public JoystickButton getDigitalControl(String name) {
    return Controls.getInstance()
      .getDigitalControl(m_cfgName, "controls", name);
  }

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