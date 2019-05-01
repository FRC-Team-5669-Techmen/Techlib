package edu.boscotech.techlib.hardware;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.configparser.RealConfiggable;
import edu.boscotech.techlib.validator.NumberValidator;
import edu.boscotech.techlib.validator.ObjectValidator;

@RealConfiggable("TalonSRX")
public class TalonSRXComponent extends MotorControllerComponent {
    private TalonSRX mTalon;

    public TalonSRXComponent() {
        super("MotorController");
    }

    @Override
    protected void extraSetup(TechlibRobot robot, ConfigElement config) {
        mTalon = new TalonSRX((int) config.getNumber("id"));
    }

    @Override
    protected ObjectValidator extraCreateValidator() {
        ObjectValidator validator = new ObjectValidator();
        validator.addField("id", new NumberValidator());
        return validator;
    }

    @Override
    protected void setRawMotorSpeed(double speed) {
        mTalon.set(ControlMode.PercentOutput, speed);
    }
}