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
    private int m_range;

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

        m_encodedMotor.configMotionCruiseVelocity(getCfgInt(cfgPath, "cruiseSpeed"));
        m_encodedMotor.configMotionAcceleration(getCfgInt(cfgPath, "accelerateion"));
        m_encodedMotor.config_kF(0, getCfgDouble(cfgPath, "f"));
        m_encodedMotor.config_kP(0, getCfgDouble(cfgPath, "p"));
        m_encodedMotor.config_kI(0, getCfgDouble(cfgPath, "i"));
        m_encodedMotor.config_kD(0, getCfgDouble(cfgPath, "d"));
    }

    public double getPosition() {
        return m_encodedMotor.getSelectedSensorPosition() / ((double) m_range);
    }

    public void setPosition(double position) {
        m_encodedMotor.set(ControlMode.MotionMagic, position * m_range);
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
            goNeutral();
        } else if (isNLimitEngaged()) {
            m_range -= m_encodedMotor.getSelectedSensorPosition();
            m_encodedMotor.setSelectedSensorPosition(0);
            goNeutral();
        }
    }

    @Override
    protected void enterSafeState() {
        goNeutral();
    }
}