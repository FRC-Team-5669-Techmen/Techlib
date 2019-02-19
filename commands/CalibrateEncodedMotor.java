package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.EncodedMotorSubsystem;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CalibrateEncodedMotor extends CommandGroup {
    public CalibrateEncodedMotor(EncodedMotorSubsystem motor) {
        addSequential(new Command(){
            @Override
            public synchronized void start() {
                motor.startNCalibration();
            }
        
            @Override
            protected boolean isFinished() {
                return motor.isNLimitEngaged();
            }
        });

        addSequential(new Command(){
            @Override
            public synchronized void start() {
                motor.startPCalibration();
            }
        
            @Override
            protected boolean isFinished() {
                return motor.isPLimitEngaged();
            }
        });

        addSequential(new Command(){
            @Override
            public synchronized void start() {
                motor.printRange();
            }
        
            @Override
            protected boolean isFinished() {
                return true;
            }
        });
    }
}