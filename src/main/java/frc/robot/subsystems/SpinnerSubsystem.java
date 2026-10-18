// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SpinnerSubsystem extends SubsystemBase {

  private final SparkMax spinMotor; 
  private final DigitalInput beamBreak; 
  private boolean isClockwise; 

  
  /** Creates a new SpinnerSubsystem. */
  public SpinnerSubsystem() {
    
    spinMotor = new SparkMax(5, MotorType.kBrushless);
    beamBreak = new DigitalInput(0);
    isClockwise = true; 

    SparkMaxConfig spinMotorConfig = new SparkMaxConfig();
    spinMotor.configure(spinMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  public void setSpeed(double speed){
    speed = (isClockwise) ? speed : -speed; 
    spinMotor.set(speed);
  }

  public void toggleDirection(){
    isClockwise = !isClockwise; 
  }

  public Boolean getBeamOutput(){
    return beamBreak.get(); 
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
