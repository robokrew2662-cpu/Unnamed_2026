// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;


public class PneumaticsSubsystem {
  Solenoid IntakeSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 3);
  public Object openSlap;

public Command openSlap()
{
  IntakeSolenoid.set(true);
    return null;
}
public Command closeSlap()
{
  IntakeSolenoid.set(false);
  return null;
}


}
