// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import frc.systems.Balance;

// System imports
import frc.systems.Drivetrain;

// Utilities imports
import frc.utilities.RoboRioPorts;
import frc.utilities.Gyroscope;

public class Robot extends TimedRobot {

  public static Joystick leftJoystick;
  public static Joystick rightJoystick;
  public static Joystick xboxJoystick;

  public static XboxController xboxController;

  Notifier driveRateGroup;
  public static Drivetrain mDrivetrain;

  public static Timer systemTimer;

  public static double initialPitch = 0;
  
  private static double counter = 0;
 
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    leftJoystick = new Joystick(0);
    rightJoystick = new Joystick(1);
    xboxJoystick = new Joystick(2);

    mDrivetrain = new Drivetrain(true, RoboRioPorts.CAN_DRIVE_L1, RoboRioPorts.CAN_DRIVE_L2, RoboRioPorts.CAN_DRIVE_L3,
    RoboRioPorts.CAN_DRIVE_R1, RoboRioPorts.CAN_DRIVE_R2, RoboRioPorts.CAN_DRIVE_R3,
    RoboRioPorts.DRIVE_DOUBLE_SOLENOID_FWD, RoboRioPorts.DRIVE_DOUBLE_SOLENOID_REV, RoboRioPorts.DIO_DRIVE_RIGHT_A,
    RoboRioPorts.DIO_DRIVE_RIGHT_B, RoboRioPorts.DIO_DRIVE_LEFT_A, RoboRioPorts.DIO_DRIVE_LEFT_B);

    // Drive train motor control is done on its own timer driven thread regardless of disabled/teleop/auto mode selection
    driveRateGroup = new Notifier(mDrivetrain::operatorDrive);
    driveRateGroup.startPeriodic(0.05);
    
    xboxController = new XboxController(2);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    double yaw = Gyroscope.GetYaw();
    SmartDashboard.putNumber("yaw", yaw);

    double rawPitch = Gyroscope.GetPitch();
    SmartDashboard.putNumber("pitch", rawPitch);
    
    double pitch = Gyroscope.GetCorrectPitch(rawPitch);
    SmartDashboard.putNumber("corrected pitch", pitch);

    double roll = Gyroscope.GetRoll();
    SmartDashboard.putNumber("roll", roll);


    if (counter == 1) {
      Balance.balance(pitch);
      counter = 0;
    }
    counter += 1;

    SmartDashboard.putNumber("initial pitch", initialPitch);
  }

  @Override
  public void autonomousInit() {}

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    Gyroscope.imu.reset();

    initialPitch = Gyroscope.GetPitch();
    
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
