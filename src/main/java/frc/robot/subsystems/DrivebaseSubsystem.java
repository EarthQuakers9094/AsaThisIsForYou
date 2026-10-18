// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.DrivebaseConstants;
import frc.robot.Constants.DrivebaseConstants.DriveMode;

public class DrivebaseSubsystem extends SubsystemBase {
  
  private final SparkMax leftLeadSpark;
  private final SparkMax leftFollowSpark;
  private final SparkMax rightLeadSpark; 
  private final SparkMax rightFollowSpark; 

  private final DifferentialDrive drive; 
  private DriveMode currentMode;  
    
    /** Creates a new DrivebaseSubsystem. */
    public DrivebaseSubsystem() {

    currentMode = DriveMode.NORMAL;

    leftLeadSpark = new SparkMax(DrivebaseConstants.LeftLeadSparkID, MotorType.kBrushless);
    leftFollowSpark = new SparkMax(DrivebaseConstants.LeftFollowSparkID, MotorType.kBrushless);
    rightLeadSpark = new SparkMax(DrivebaseConstants.RightLeadSparkID, MotorType.kBrushless);
    rightFollowSpark = new SparkMax(DrivebaseConstants.RightFollowSparkID, MotorType.kBrushless);

    drive = new DifferentialDrive(leftLeadSpark :: set, rightLeadSpark :: set);

    //Global config, voltage compensation smooths driving during battery discharge
    final SparkMaxConfig globalConfig = new SparkMaxConfig(); 
    globalConfig
        .smartCurrentLimit(50)
        .voltageCompensation(12)
        .idleMode(IdleMode.kBrake);

    // Apply the global config and invert since it is on the opposite side
    final SparkMaxConfig rightLeaderConfig = new SparkMaxConfig();
    rightLeaderConfig
        .apply(globalConfig)
        .inverted(true);

    // Apply the global config and set the leader SPARK for follower mode
    final SparkMaxConfig leftFollowerConfig = new SparkMaxConfig();
    leftFollowerConfig
        .apply(globalConfig)
        .follow(leftLeadSpark);

    // Apply the global config and set the leader SPARK for follower mode
    final SparkMaxConfig rightFollowerConfig = new SparkMaxConfig(); 
    rightFollowerConfig
        .apply(globalConfig)
        .follow(rightLeadSpark);

    /*
     * Apply the configuration to the SPARKs.
     *
     * kResetSafeParameters is used to get the SPARK MAX to a known state. This
     * is useful in case the SPARK MAX is replaced.
     *
     * kPersistParameters is used to ensure the configuration is not lost when
     * the SPARK MAX loses power. This is useful for power cycles that may occur
     * mid-operation.
     */
    leftLeadSpark.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftFollowSpark.configure(leftFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightLeadSpark.configure(rightLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightFollowSpark.configure(rightFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  }

  //Command Style #1: Writing a void method to be used in a separate Command class
  public void arcadeDrive(double forwardSpeed, double turnSpeed){
    drive.arcadeDrive(forwardSpeed*currentMode.getScale(), turnSpeed * 0.8);
  }

  //Command Style #2: Writing a Command method which returns a command to be called 
  //inside RobotContainer; Lets you skip creating a DriveCommand class 
  public Command arcadeDriveCommand(DoubleSupplier forwardSpeed, DoubleSupplier turnSpeed){
    return Commands.run(() -> 
        drive.arcadeDrive(currentMode.getScale() * forwardSpeed.getAsDouble(), 
        0.8 * turnSpeed.getAsDouble()), this); 
  }

  //Command Style #3: Constructing a command in Robot Container
  //Good for simple commands
  public void setDriveMode(DriveMode driveMode){
    this.currentMode = driveMode;
  }

  //Command Style #2: Command method to be called in RobotContainer 
  public Command setDriveModeCommand(DriveMode driveMode){
    return Commands.runOnce(() -> currentMode = driveMode, this); 
  }

  public void stopDrive(){
    drive.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
