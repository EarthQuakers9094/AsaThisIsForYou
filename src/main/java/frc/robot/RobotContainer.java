// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.DrivebaseConstants.DriveMode;
import frc.robot.commands.ArcadeDrive;
import frc.robot.subsystems.DrivebaseSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  DrivebaseSubsystem drivebaseSubsystem = new DrivebaseSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController driveController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  private final CommandXboxController operatorController = 
      new CommandXboxController(OperatorConstants.kOperatorControllerPort); 

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    //Command Style #1: Calling the ArcadeDrive command class in commands folder 
    drivebaseSubsystem.setDefaultCommand(
      new ArcadeDrive(drivebaseSubsystem, () -> driveController.getLeftY(), () -> driveController.getLeftX()));

    //Command Style #2 creating an inline Command in robot container 
    driveController.x().toggleOnTrue(Commands.runOnce(() -> drivebaseSubsystem.setDriveMode(DriveMode.SLOW)));

    //Command Style #3: Using Command methods from subsystem 
    driveController.a().toggleOnTrue(drivebaseSubsystem.setDriveModeCommand(DriveMode.FAST));
    driveController.b().toggleOnTrue(drivebaseSubsystem.setDriveModeCommand(DriveMode.NORMAL));
    

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
