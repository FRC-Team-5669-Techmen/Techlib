package edu.boscotech.techlib.subsystems;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class LightSensorArraySubsystem extends BetterSubsystem {
    private I2C m_sensorConnection;
    private long m_lastUpdate = 0;
    private int m_cachedValue;
    private int m_register, m_range;

    public LightSensorArraySubsystem() {
        super("Light Sensor Array", "lightSensorArray", "LightSensorArray");
    }

    public LightSensorArraySubsystem(String hrName, String cfgName) {
        super(hrName, cfgName, "LightSensorArray");
    }

    @Override
    protected void setup() {
        m_sensorConnection = new I2C(Port.kOnboard, getCfgInt("i2cAddress"));
        m_register = getCfgInt("register");
        m_range = getCfgInt("range");
    }

    /**
     * 
     * @return -1.0 for left, 1.0 for right, 0.0 for middle.
     */
    public double getValue() {
        if (System.currentTimeMillis() > m_lastUpdate) {
            m_lastUpdate = System.currentTimeMillis();
            ByteBuffer buf = ByteBuffer.allocate(1);
            m_sensorConnection.read(m_register, 1, buf);
            m_cachedValue = buf.getInt();
        }
        return (m_cachedValue / ((double) m_range)) * 2.0 - 1.0;
    }
}