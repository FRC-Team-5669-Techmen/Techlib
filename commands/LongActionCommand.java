package edu.boscotech.techlib.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class LongActionCommand<T extends Subsystem> extends Command {
  private T m_target;
  private boolean m_done = false;

  public LongActionCommand(T target) {
    m_target = target;
  }

  protected final T getTarget() {
    return m_target;
  }

  /**
   * Start executing the action that will complete sometime in the future.
   * @return True if the action finished on this cycle.
   */
  protected abstract boolean doAction();

  @Override
  protected final void execute() {
    m_done = doAction();
  }

  @Override
  protected boolean isFinished() {
    return m_done;
  }
}