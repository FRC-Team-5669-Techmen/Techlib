package edu.boscotech.techlib.config;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Represents an analog (slidable) control. It handles retrieving values from
 * the joystick and doing math on them. The formula used is:
 * scale * pow(rawValue, curve)
 */
public class AnalogControl {
  private Joystick m_joystick;
  private int m_axisIndex;
  private double m_scale, m_curve;

  /**
   * Constructs an AnalogControl.
   * @param joystick The joystick that should be polled for information.
   * @param axisIndex The index of the axis that should be read from the stick.
   * @param curve A value used to make the control logarithmic. The formula used
   *              internally is pow(joystickValue, curve). A value of 1.0 will
   *              give a linear curve, e.g. if joystickValue is 0.5, output will
   *              be 0.5. Values greater than one allow for more precision, e.g.
   *              a curve of 2.0 will make a joystick value of 0.5 become 0.25.
   * @param scale A multiplier applied to the value after being curved.
   */
  public AnalogControl(Joystick joystick, int axisIndex, double curve, 
    double scale) {
    m_joystick = joystick;
    m_axisIndex = axisIndex;
    m_curve = curve;
    m_scale = scale;
  }

  public double getValue() {
    double value = m_joystick.getRawAxis(m_axisIndex);
    // Abs and multiply by sign so that we get the correct sign, even if the
    // power is e.g. 2, which would make everything positive.
    value = Math.abs(Math.pow(value, m_curve)) * (value > 0.0 ? 1 : -1);
    return m_scale * value;
  }
}