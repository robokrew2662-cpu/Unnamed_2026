// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;


public class PneumaticsSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  public PneumaticsSubsystem() { 
Solenoid IntakeSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0);

public void ExtendIntake()
{
    IntakeSolenoid.set(true); // TODO boolean may need to flipped to false get desired result
}


public void RetractClaw()
{
    IntakeSolenoid.set(false); // TODO boolean may need to flipped to true get desired result
}

}
}
