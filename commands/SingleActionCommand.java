package edu.boscotech.techlib.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SingleActionCommand<T extends Subsystem> extends Command {
  private T m_target;
  private boolean m_done = false;

  public SingleActionCommand(T target) {
    requires(target);
    m_target = target;
  }

  protected final T getTarget() {
    return m_target;
  }

  protected abstract void doAction();

  @Override
  protected final void execute() {
    doAction();
    m_done = true;
  }

  @Override
  protected final boolean isFinished() {
    return m_done;
  }
}