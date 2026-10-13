// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DrivebaseSubsystem extends SubsystemBase {
  
  private final SparkMax leftLeadSpark;
  private final SparkMax leftFollowSpark;
  private final SparkMax rightLeadSpark; 
  private final SparkMax rightFollowSpark; 

  private final DifferentialDrive drive; 
  
  /** Creates a new DrivebaseSubsystem. */
  public DrivebaseSubsystem() {

    leftLeadSpark = new SparkMax(Constants.DrivebaseConstants.LeftLeadSparkID, MotorType.kBrushless);
    leftFollowSpark = new SparkMax(Constants.DrivebaseConstants.LeftFollowSparkID, MotorType.kBrushless);
    rightLeadSpark = new SparkMax(Constants.DrivebaseConstants.RightLeadSparkID, MotorType.kBrushless);
    rightFollowSpark = new SparkMax(Constants.DrivebaseConstants.RightFollowSparkID, MotorType.kBrushless);

    drive = new DifferentialDrive(leftFollowSpark :: set, rightLeadSpark :: set);

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

  public void arcadeDrive(double forwardSpeed, double turnSpeed){
    drive.arcadeDrive(forwardSpeed, turnSpeed);
  }

  public void stopDrive(){
    drive.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
