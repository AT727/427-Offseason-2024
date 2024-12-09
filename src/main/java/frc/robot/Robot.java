// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.swing.text.Position;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The robot is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

   CANSparkMax motor = new CANSparkMax(0,MotorType.kBrushless);
   RelativeEncoder encoder = motor.getEncoder();

   PIDController pid = new PIDController(2, 0, 0.5);
   ArmFeedforward feedforward = new ArmFeedforward(5.0, 10.0, 0);



  @Override
  public void robotInit() {
    motor.setIdleMode(IdleMode.kBrake);
    motor.setInverted(false);
    motor.setSmartCurrentLimit(40);

    pid.setTolerance(5);

    SmartDashboard.putNumber("kp", SmartDashboard.getNumber("kp", 0));
    SmartDashboard.putNumber("ki", SmartDashboard.getNumber("ki", 0));
    SmartDashboard.putNumber("kd", SmartDashboard.getNumber("kd", 0));

    SmartDashboard.putNumber("ks", SmartDashboard.getNumber("ks", 0));
    SmartDashboard.putNumber("kg", SmartDashboard.getNumber("kg", 0));
    SmartDashboard.putNumber("setpoint", SmartDashboard.getNumber("setpoint", 0));
  }

  @Override
  public void robotPeriodic() {

    double p = SmartDashboard.getNumber("kp", 0);
    double i = SmartDashboard.getNumber("ki", 0);
    double d = SmartDashboard.getNumber("kd", 0);

    double s = SmartDashboard.getNumber("ks", 0);
    double g = SmartDashboard.getNumber("kg", 0);

    double setpoint = SmartDashboard.getNumber("setpoint",0);

    pid.setPID(p, i, d);
    feedforward = new ArmFeedforward(s, g, 0);
    pid.setSetpoint(setpoint);

    double pidOutput = pid.calculate(encoder.getPosition());
    double ffOutput = feedforward.calculate(encoder.getPosition(), 0);

    motor.set(pidOutput + ffOutput);

    SmartDashboard.getNumber("motor position ", encoder.getPosition());
    SmartDashboard.getBoolean("at setpoint? ", pid.atSetpoint());
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
