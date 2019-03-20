/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.boscotech.techlib;

import edu.boscotech.techlib.util.DefaultCommandCreator;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import java.util.ArrayList;
import java.util.List;

public abstract class Robot extends TimedRobot {
  List<Command> m_currentCommands = new ArrayList<>();
  List<DefaultCommandCreator> m_defaultSubsystems = new ArrayList<>();

  /**
   * Starts one or more commands that will automatically be stopped once the 
   * robot stops being in its current mode. For example, if several commands are
   * added during teleop to control the robot from the joysticks, they will be
   * automatically interrupted once the teleop period ends.
   * @param commands The commands to be added.
   */
  protected final void useCommands(Command... commands) {
    for (Command command : commands) {
      if (command == null) continue;
      command.start();
      m_currentCommands.add(command);
    }
  }

  /**
   * Add a subsystem that, whenever teleop or test mode is started, its default
   * command for that mode will be automatically started as well.
   * @param subsystem
   */
  protected final void useDefaultCommandsFrom(DefaultCommandCreator... subsystems) {
    for (DefaultCommandCreator subsystem : subsystems) {
      m_defaultSubsystems.add(subsystem);
    }
  }

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public final void robotInit() {
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public final void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public final void disabledInit() {
    for (Command command : m_currentCommands) {
      command.cancel();
    }
    m_currentCommands.clear();
  }

  @Override
  public final void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public final void autonomousInit() {
    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */
    setupAutonomous();
    // TODO: Disable this. Temporary hack for 2019 season.
    teleopInit();
  }

  protected void setupAutonomous() { }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public final void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public final void teleopInit() {
    for (DefaultCommandCreator creator : m_defaultSubsystems) {
      useCommands(creator.createDefaultTeleopCommand());
    }
    setupTeleop();
  }

  protected void setupTeleop() { }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public final void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public final void testInit() {
    for (DefaultCommandCreator creator : m_defaultSubsystems) {
      //useCommands(creator.createDefaultTestCommand());
    }
    setupTest();
  }

  protected void setupTest() { }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public final void testPeriodic() {
    Scheduler.getInstance().run();
  }
}
