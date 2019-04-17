package edu.boscotech.techlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Represents an encoded motor that starts at zero and can travel to an
 * arbitrary point. Call setupMotor() in the child's setup() method to configure
 * the motor.
 */
public abstract class EncodedMotorSubsystem extends BetterSubsystem {
    private TalonSRX m_encodedMotor;
    private int m_range, m_countsPerUnit;
    private double m_targetPosition;

    public EncodedMotorSubsystem(String hrName, String cfgName, String ntType) {
        super(hrName, cfgName, ntType);
    }

    protected final void setupMotor(int motorIndex, String cfgPath,
        boolean motorBackwards, boolean sensorBackwards) {
        m_encodedMotor = new TalonSRX(motorIndex);
        m_encodedMotor.setSensorPhase(sensorBackwards);
        m_encodedMotor.setInverted(motorBackwards);
        m_encodedMotor.configClearPositionOnLimitR(true, 100);
        m_range = getCfgInt(cfgPath, "range");
        m_countsPerUnit = getCfgInt(cfgPath, "countsPerUnit");

        m_encodedMotor.configMotionCruiseVelocity(getCfgInt(cfgPath, "cruiseSpeed"));
        m_encodedMotor.configMotionAcceleration(getCfgInt(cfgPath, "acceleration"));
        m_encodedMotor.config_kF(0, getCfgDouble(cfgPath, "f"));
        m_encodedMotor.config_kP(0, getCfgDouble(cfgPath, "p"));
        m_encodedMotor.config_kI(0, getCfgDouble(cfgPath, "i"));
        m_encodedMotor.config_kD(0, getCfgDouble(cfgPath, "d"));

        getNetworkData().addDoubleProperty("position", this::getPosition, 
            this::setPosition);
        getNetworkData().addDoubleProperty("speed", this::getSpeed, 
            this::setManualSpeed);
        getNetworkData().addBooleanProperty("minCalibrate", 
            () -> getSpeed() == -0.2, 
            (boolean b) -> { if(b) startNCalibration(); });
        getNetworkData().addBooleanProperty("maxCalibrate", 
            () -> getSpeed() == 0.2, 
            (boolean b) -> { if(b) startPCalibration(); });
        getNetworkData().addDoubleProperty("minPos", () -> 0.0, 
            (double d) -> { });
        getNetworkData().addDoubleProperty("maxPos", 
            () -> this.m_range / (double) this.m_countsPerUnit, 
            (double d) -> { });
        getNetworkData().addBooleanProperty("minLimit", this::isNLimitEngaged, 
            (boolean b) -> { });
        getNetworkData().addBooleanProperty("maxLimit", this::isPLimitEngaged, 
            (boolean b) -> { });
    }

    public double getPosition() {
        return m_encodedMotor.getSelectedSensorPosition() / ((double) m_countsPerUnit);
    }

    public double getTargetPosition() {
        return m_targetPosition;
    }

    public void setPosition(double units) {
        m_targetPosition = units;
        m_encodedMotor.set(ControlMode.MotionMagic, units * m_countsPerUnit);
    }

    public double getSpeed() {
        return m_encodedMotor.getMotorOutputPercent();
    }

    public void setManualSpeed(double speed) {
        m_encodedMotor.set(ControlMode.PercentOutput, speed);
    }

    public void goNeutral() {
        m_encodedMotor.neutralOutput();
    }

    public boolean isPLimitEngaged() {
        return m_encodedMotor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isNLimitEngaged() {
        return m_encodedMotor.getSensorCollection().isRevLimitSwitchClosed();
    }

    public void startPCalibration() {
        setManualSpeed(0.2);
    }

    public void startNCalibration() {
        setManualSpeed(-0.2);
    }

    public void printRange() {
        System.out.println("Motion profile distance: " + m_range);
    }

    @Override
    public void periodic() {
        super.periodic();
        if (isPLimitEngaged()) {
            m_range = m_encodedMotor.getSelectedSensorPosition();
        } else if (isNLimitEngaged()) {
            m_range -= m_encodedMotor.getSelectedSensorPosition();
            m_encodedMotor.setSelectedSensorPosition(0);
        }
    }

    @Override
    protected void enterSafeState() {
        goNeutral();
    }
}