package edu.boscotech.techlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.boscotech.techlib.commands.ManualAuxDrive;
import edu.wpi.first.wpilibj.command.Command;

public class AuxMotorSubsystem extends BetterSubsystem {
    private TalonSRX m_talon;

    public AuxMotorSubsystem() {
        super("Auxiliary Motor", "auxMotor", "AuxMotor");
    }

    public AuxMotorSubsystem(String hrName, String cfgName) {
        super(hrName, cfgName, "AuxMotor");
    }

    @Override
    protected void setup() {
        m_talon = new TalonSRX(getCfgInt("id"));
    }

    public void set(double speed) {
        m_talon.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public Command createDefaultTeleopCommand() {
        return new ManualAuxDrive(this);
    }
}