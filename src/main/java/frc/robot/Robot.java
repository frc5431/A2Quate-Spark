/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.cameraserver.CameraServer;

import com.ctre.phoenix.motorcontrol.can.*;

import frc.robot.Titan.LogitechExtreme3D;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxFrames.DataFrame;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Titan.Xbox driver; 
  private CANSparkMax frontLeft, backLeft, frontRight, backRight;
  private SpeedControllerGroup left, right, intake, arm, elevator;   
  private DifferentialDrive driveBase;
  private WPI_TalonSRX leftIntake, rightIntake, leftArm, rightArm, leftElevator, rightElevator;
  private LogitechExtreme3D operator;
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // controllers with ID
    frontLeft = new CANSparkMax(0, MotorType.kBrushless); 
    backLeft = new CANSparkMax(1, MotorType.kBrushless); 
    frontRight = new CANSparkMax(2, MotorType.kBrushless); 
    backRight = new CANSparkMax(3, MotorType.kBrushless); 

    leftIntake = new WPI_TalonSRX(10);
		rightIntake = new WPI_TalonSRX(5);
    rightIntake.setInverted(true);

    leftArm = new WPI_TalonSRX(6);
		rightArm = new WPI_TalonSRX(1);
    rightArm.setInverted(true);

    leftElevator = new WPI_TalonSRX(3);
    rightElevator = new WPI_TalonSRX(8);

    // controller groups + driveBase
    intake = new SpeedControllerGroup(leftIntake, rightIntake);
    arm = new SpeedControllerGroup(leftArm, rightArm);
    elevator = new SpeedControllerGroup(leftElevator, rightElevator);
    left = new SpeedControllerGroup(frontLeft, backLeft);
    right = new SpeedControllerGroup(frontRight, backRight); 
    driveBase = new DifferentialDrive(left,right); 

    driver = new Titan.Xbox(0); 
    operator = new Titan.LogitechExtreme3D(1);
    driver.setDeadzone(.15);
    operator.setDeadzone(.15);

    CameraServer.getInstance().startAutomaticCapture("FrontCamera", 1);
    CameraServer.getInstance().startAutomaticCapture("BackCamera", 0);
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
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    }
  

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double leftY = driver.getRawAxis(Titan.Xbox.Axis.LEFT_Y);
		double leftX = driver.getRawAxis(Titan.Xbox.Axis.LEFT_X);
		double drivespeed = 1.0;
    driveBase.tankDrive(leftY - leftX*drivespeed, leftY + leftX*drivespeed);

    if (operator.getRawButton(Titan.LogitechExtreme3D.Button.TRIGGER)) {
			intake.set(0.75);
		} else if (operator.getRawButton(Titan.LogitechExtreme3D.Button.TEN) || operator.getRawButton(Titan.LogitechExtreme3D.Button.NINE)) {
			intake.set(-1);
		} else {
			intake.set(0);	
    }
    
     arm.set(operator.getRawAxis(Titan.LogitechExtreme3D.Axis.Y)/2);

     double throttle = 1-((operator.getRawAxis(Titan.LogitechExtreme3D.Axis.SLIDER)+1)/2);

     if (operator.getPOV() == 0) {
			elevator.set(throttle);
			// hat is being pushed forward, extend the elevator
		} else if (operator.getPOV() == 180) {
			elevator.set(-throttle);
			// hat is being pushed backward, retract the elevator
		} else {
			elevator.set(0);
			// hat is in it's default position, stop the elevator motors
}
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}