// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Seconds;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;

public class OuttakeSubsystem extends SubsystemBase {

  // Upper Shooter Motor Config
  private SmartMotorControllerConfig shooterConfig = new SmartMotorControllerConfig(this)
      .withControlMode(ControlMode.CLOSED_LOOP)
      // PID Constants for velocity control
      .withClosedLoopController(0.1, 0, 0)
      .withSimClosedLoopController(0.1, 0, 0)
      // Feedforward Constants - helps track changing RPM goals
      .withFeedforward(new SimpleMotorFeedforward(0.1, 0.12, 0))
      .withSimFeedforward(new SimpleMotorFeedforward(0.1, 0.12, 0))
      // Telemetry name and verbosity level
      .withTelemetry("Shooter", TelemetryVerbosity.HIGH)
      // Gearing from the motor rotor to final shaft (3:1 reduction)
      .withGearing(new MechanismGearing(GearBox.fromReductionStages(3)))
      // Motor properties
      .withMotorInverted(false)
      .withIdleMode(MotorMode.COAST)
      .withStatorCurrentLimit(Amps.of(40))
      .withClosedLoopRampRate(Seconds.of(0.25))
      .withOpenLoopRampRate(Seconds.of(0.25));

  // Lower Indexer Motor Config  
  private SmartMotorControllerConfig indexerConfig = new SmartMotorControllerConfig(this)
      .withControlMode(ControlMode.CLOSED_LOOP)
      .withClosedLoopController(0.1, 0, 0)
      .withSimClosedLoopController(0.1, 0, 0)
      .withFeedforward(new SimpleMotorFeedforward(0.1, 0.12, 0))
      .withSimFeedforward(new SimpleMotorFeedforward(0.1, 0.12, 0))
      .withTelemetry("Indexer", TelemetryVerbosity.HIGH)
      .withGearing(new MechanismGearing(GearBox.fromReductionStages(3)))
      .withMotorInverted(true) // Often inverted relative to upper for proper game piece direction
      .withIdleMode(MotorMode.COAST)
      .withStatorCurrentLimit(Amps.of(40))
      .withClosedLoopRampRate(Seconds.of(0.25))
      .withOpenLoopRampRate(Seconds.of(0.25));

//Vendor motor controller objects
  private SparkMax shooterSpark = new SparkMax(2, MotorType.kBrushless);
  private SparkMax indexerSpark = new SparkMax(3, MotorType.kBrushless);

//Create our SmartMotorControllers with Neo motors
  private SmartMotorController  shooterMotor = new SparkWrapper(shooterSpark, DCMotor.getNEO(1), shooterConfig);
  private SmartMotorController indexerMotor = new SparkWrapper(indexerSpark, DCMotor.getNEO(1), indexerConfig);

// Shooter Mechanism Config
  private final FlyWheelConfig upperFlywheelConfig = new FlyWheelConfig(shooterMotor)
      .withDiameter(Inches.of(4))
      .withMass(Pounds.of(1))
      .withUpperSoftLimit(RPM.of(6000))//double check this
      .withTelemetry("Shooter", TelemetryVerbosity.HIGH);

// Indexer Mechanism Config
  private final FlyWheelConfig lowerFlywheelConfig = new FlyWheelConfig(indexerMotor)
      .withDiameter(Inches.of(4))
      .withMass(Pounds.of(1))
      .withUpperSoftLimit(RPM.of(6000))//double check this
      .withTelemetry("Indexer", TelemetryVerbosity.HIGH);

// FlyWheel Mechanisms
  private FlyWheel upperFlywheel = new FlyWheel(upperFlywheelConfig);
  private FlyWheel lowerFlywheel = new FlyWheel(lowerFlywheelConfig);


  /** Creates a new DoubleFlywheelSubsystem. */
  public OuttakeSubsystem() {}

  /**
   * Set both flywheel speeds independently.
   *
   * @param upperSpeed Upper flywheel target speed
   * @param lowerSpeed Lower flywheel target speed
   * @return Command to set both flywheel speeds
   */
  public Command setFlywheelSpeeds(AngularVelocity upperSpeed, AngularVelocity lowerSpeed) {
    return Commands.parallel(
        upperFlywheel.setSpeed(upperSpeed),
        lowerFlywheel.setSpeed(lowerSpeed)
    );
  }

  /**
   * Neutral spin shot - both wheels at same speed.
   *
   * @return Command for neutral spin shot
   */
  public Command neutralSpinShot() {
    return setFlywheelSpeeds(RPM.of(3000), RPM.of(3000));
  }

  /**
   * Backspin shot - upper wheel faster for lob shots.
   *
   * @return Command for backspin shot
   */
  public Command backspinShot() {
    return setFlywheelSpeeds(RPM.of(3500), RPM.of(2500));
  }

  /**
   * Topspin shot - lower wheel faster for line drives.
   *
   * @return Command for topspin shot
   */
  public Command topspinShot() {
    return setFlywheelSpeeds(RPM.of(2500), RPM.of(3500));
  }

  /**
   * Stop both flywheels.
   *
   * @return Command to stop flywheels
   */
  public Command stopFlywheels() {
    return setFlywheelSpeeds(RPM.of(0), RPM.of(0));
  }

  /**
   * Check if both flywheels are near their target velocities.
   *
   * @param tolerance Acceptable velocity error tolerance
   * @return true if both flywheels are within tolerance of their target
   */
  //public boolean atTargetVelocity(AngularVelocity tolerance) {
    //return upperFlywheel.isNear(tolerance) && lowerFlywheel.isNear(tolerance);
  

  /**
   * Get the current upper flywheel velocity.
   *
   * @return Upper flywheel velocity
   */
  public AngularVelocity getUpperVelocity() {
    return upperFlywheel.getSpeed();
  }

  /**
   * Get the current lower flywheel velocity.
   *
   * @return Lower flywheel velocity
   */
  public AngularVelocity getLowerVelocity() {
    return lowerFlywheel.getSpeed();
  }

  @Override
  public void periodic() {
    // Update telemetry for both flywheels
    upperFlywheel.updateTelemetry();
    lowerFlywheel.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    // Run simulation for both flywheels
    upperFlywheel.simIterate();
    lowerFlywheel.simIterate();
  }
}