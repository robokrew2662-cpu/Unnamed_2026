// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class RotateIntake extends SubsystemBase {
  /** Creates a new Pneumatics. */
  private Solenoid m_pneumatics = new Solenoid(PneumaticsModuleType.CTREPCM, 3);
 

  public Command ExtendIntake(){
    return this.runOnce(() -> m_pneumatics.set(true));
  }

   public Command RetractIntake(){
    return this.runOnce(() -> m_pneumatics.set(false));
  }


  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    // Publish the solenoid state to telemetry.
    builder.addBooleanProperty("extended", () -> m_pneumatics.get() == true, null);
  }
}
