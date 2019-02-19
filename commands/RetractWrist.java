package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.WristSubsystem;

public class RetractWrist extends LongActionCommand<WristSubsystem> {
  public RetractWrist(WristSubsystem wrist) {
    super(wrist);
  }

  @Override
  protected boolean doAction() {
    getTarget().retract();
    return getTarget().isRetracted();
  }
}