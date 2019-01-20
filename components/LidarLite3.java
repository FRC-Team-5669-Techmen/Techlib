package edu.boscotech.techlib.components;

import edu.wpi.first.wpilibj.I2C;

/**
 * Communicate with a <a href="https://www.andymark.com/products/lidar-lite-3">
 * Lidar Lite 3 sensor</a> over I2C. It must be attached to the regular I2C
 * port on the RoboRIO.
 */
public class LidarLite3 {
  private I2C port;

  /**
   * Creates a LidarLite3 with the default I2C address (0x62). This is normally
   * the constructor you would want to use.
   * @see {@link #LidarLite3(int)}
   */
  public LidarLite3() {
    port = new I2C(I2C.Port.kOnboard, 0x62);
  }

  /**
   * Creates a LidarLite3 with the given I2C address.
   * @param address The numerical I2C address the sensor has.
   */
  public LidarLite3(int address) {
    port = new I2C(I2C.Port.kOnboard, address);
  }

  private int readTwoByteValue(int address) {
    byte[] buffer = new byte[2];
    port.read(address | 0x80, 2, buffer);
    // first byte contains high bits, second byte contains low bits.
    return ((buffer[0] & 0xFF) << 8) + (buffer[1] & 0xFF);
  }

  public void reset() {
    port.write(0x00, 0x00);
  }

  private void requestMeasurementWithoutFilter() {
    port.write(0x00, 0x03);
  }

  private void requestMeasurement() {
    port.write(0x00, 0x04);
  }

  private boolean getStatusBit(int bit) {
    byte[] buffer = new byte[1];
    port.read(0x01, 1, buffer);
    return (buffer[0] & (0x01 << bit)) != 0;
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if the device is currently recording a measurement.
   */
  public boolean isBusy() {
    return getStatusBit(0);
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if reference data in sensor's internal buffer has overflown.
   */
  public boolean isReferenceOverflown() {
    return getStatusBit(1);
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if the sensor's internal signal buffer has overflown.
   */
  public boolean isSignalOverflown() {
    return getStatusBit(2);
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if the last measurement was not recorded correctly due to the
   *         sensor not detecting a return pulse.
   */
  public boolean isNoReturnDetected() {
    return getStatusBit(3);
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if the last measurement may have been an error due to a second
   *         return pulse being detected.
   */
  public boolean isSecondaryReturnDetected() {
    return getStatusBit(4);
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if the sensor reports its hardware as operating nominally.
   */
  public boolean isSystemHealthy() {
    return !getStatusBit(5);
  }

  /**
   * Polls the status register of the sensor and returns the value of one of the
   * flags in the buffer.
   * @return True if the sensor reports an internal error in its logic.
   */
  public boolean isSystemErrorDetected() {
    return getStatusBit(6);
  }

  /**
   * Different modes the sensor can be set to involving the PWM output. More
   * detail about each mode can be found on page 8 of <a href="https://static.garmin.com/pumac/LIDAR_Lite_v3_Operation_Manual_and_Technical_Specifications.pdf#page=7&zoom=100,0,357">
   * the sensor's datasheet.</a>
   */
  public enum PWMMode {
    INPUT_TRIGGER,
    OUTPUT_STATUS,
    CONSTANT_MEASUREMENT,
    OSCILLATOR
  };

  /**
   * Sets the PWM mode of the sensor.
   * @param mode The mode to set it to.
   * @see {@link PWMMode}
   */
  public void setPWMMode(PWMMode mode) {
    byte value = 0;
    switch (mode) {
    case INPUT_TRIGGER:
      value = 0x0;
      break;
    case OUTPUT_STATUS:
      value = 0x1;
      break;
    case CONSTANT_MEASUREMENT:
      value = 0x2;
      break;
    case OSCILLATOR:
      value = 0x3;
      break;
    }
    byte[] buffer = new byte[1];
    port.read(0x04, 1, buffer);
    buffer[0] = (byte) (buffer[0] & 0b11111100 | value);
  }

  /**
   * Sets the sensor to one-shot mode. Once a measurement is requested, a single
   * measurement will be taken.
   */
  public void useOneShotMode() {
    port.write(0x11, 0x00);
  }

  /**
   * Sets the sensor to burst mode. Once a measurement is requested, multiple
   * will be taken.
   * @param burstCount The number of measurements to be taken. [2-254]
   */
  public void useBurstMode(int burstCount) {
    port.write(0x11, burstCount);
  }

  public void useFreeRunningMode() {
    port.write(0x11, 0xFF);
    requestMeasurement();
  }

  /**
   * Sets the sensor to 
   */

  /**
   * Sleeps the current thread in increments of 1ms until the sensor reports
   * no longer being busy.
   * @see {@link #isBusy()}
   */
  public void waitUntilNotBusy() {
    while (isBusy()) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Retrieves the last measurement the sensor has stored in its registers. This
   * method is non-blocking, unlike {@link #takeMeasurementNow()}.
   * @return The distance in centimeters.
   */
  public int getLastMeasurement() {
    return readTwoByteValue(0x0f);
  }

  /**
   * Asks the sensor to take a measurement and then waits for the measurement to
   * complete before returning the measured value. This means that the returned
   * distance value will be fresh, but it may take a few ms before the value is
   * returned. If you want a non-blocking method, use 
   * {@link #getLastMeasurement()} instead.
   * @return The distance in centimeters.
   */
  public int takeMeasurementNow() {
    requestMeasurement();
    waitUntilNotBusy();
    return readTwoByteValue(0x0f);
  }

  /**
   * Gets the unique serial number burned into the sensor's logic board.
   * @return The sensor's serial number.
   */
  public int getSerialNumber() {
    // TODO: For some reason this is always returning 0 on the sensor we have.
    // According to the doc, this is the right address, maybe I need to do
    // something extra to actually retrieve the value sucessfully?
    return readTwoByteValue(0x16);
  }
}