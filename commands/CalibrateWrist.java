package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.WristSubsystem;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CalibrateWrist extends CommandGroup {
  public CalibrateWrist(WristSubsystem wrist) {
    addSequential(new CalibrateWristDown(wrist));
    addSequential(new CalibrateWristUp(wrist));
    addSequential(new Command(){
      @Override
      protected void execute() {
        wrist.printCalibrationInfo();
      }
    
      @Override
      protected boolean isFinished() {
        return true;
      }
    });
  }
}