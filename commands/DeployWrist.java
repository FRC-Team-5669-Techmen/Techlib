package edu.boscotech.techlib.commands;

import edu.boscotech.techlib.subsystems.WristSubsystem;

public class DeployWrist extends LongActionCommand<WristSubsystem> {
  public DeployWrist(WristSubsystem wrist) {
    super(wrist);
  }

  @Override
  protected boolean doAction() {
    getTarget().deploy();
    return getTarget().isDeployed();
  }
}