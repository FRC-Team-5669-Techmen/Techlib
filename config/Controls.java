package edu.boscotech.techlib.config;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Controls {
  private static Controls s_instance;
  private Map<String, AnalogControl> analogControls = new HashMap<>();
  private Map<String, JoystickButton> digitalControls = new HashMap<>();

  private Controls() { }

  public static Controls getInstance() {
    if (s_instance == null) {
      s_instance = new Controls();
    }
    return s_instance;
  }

  public AnalogControl getAnalogControl(String... path) {
    String catPath = "";
    for (String part : path) {
      catPath += part;
      catPath += ":";
    }

    if (analogControls.get(catPath) == null) {
      // Make a new String array with one extra element on either end.
      String[] realpath = new String[path.length + 1];
      System.arraycopy(path, 0, realpath, 0, path.length);

      Config cfg = Config.getInstance();
      realpath[path.length] = "joystick";
      Joystick stick = new Joystick(cfg.getInt(realpath));
      realpath[path.length] = "axis";
      int axis = cfg.getInt(realpath);
      realpath[path.length] = "curve";
      double curve = cfg.getDouble(realpath);
      realpath[path.length] = "scale";
      double scale = cfg.getDouble(realpath);
      realpath[path.length] = "offset";
      double offset = cfg.getDouble(realpath);
      analogControls.put(catPath, new AnalogControl(stick, axis, curve, scale, offset));
    }

    return analogControls.get(catPath);
  }
  
  public JoystickButton getDigitalControl(String... path) {
    String catPath = "";
    for (String part : path) {
      catPath += part;
      catPath += ":";
    }

    if (digitalControls.get(catPath) == null) {
      // Make a new String array with one extra element on either end.
      String[] realpath = new String[path.length + 1];
      System.arraycopy(path, 0, realpath, 0, path.length);

      Config cfg = Config.getInstance();
      realpath[path.length] = "joystick";
      Joystick stick = new Joystick(cfg.getInt(realpath));
      realpath[path.length] = "button";
      int button = cfg.getInt(realpath);
      digitalControls.put(catPath, new JoystickButton(stick, button));
    }

    return digitalControls.get(catPath);
  }
}