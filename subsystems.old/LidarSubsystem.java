package edu.boscotech.techlib.subsystems;

import edu.boscotech.techlib.components.LidarLite3;

public class LidarSubsystem extends BetterSubsystem {
  private LidarLite3 sensor = new LidarLite3();

  public LidarSubsystem() {
    super("Lidar", "lidar", "Lidar");
  }

  public LidarSubsystem(String hrName, String cfgName) {
    super(hrName, cfgName, "Lidar");
  }

  @Override
  protected void setup() {
    sensor.useFreeRunningMode();
  }

  /**
   * Gets a measurement from the sensor (non-blocking).
   * @return The distance read by the sensor, in centimeters.
   */
  public int getDistance() {
    return sensor.getLastMeasurement();
  }
}