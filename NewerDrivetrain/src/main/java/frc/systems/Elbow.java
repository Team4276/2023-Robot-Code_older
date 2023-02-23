package frc.systems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Robot;

public class Elbow {
    
    private static CANSparkMax driveElbow;
    private double deadband = 0.05;


    public Elbow(int port) {
        driveElbow = new CANSparkMax(port, MotorType.kBrushless);
   }    

    // Speed inrange -1.0 to +1.0
    public void setElbowSpeed(double speed) {
        driveElbow.set(speed);
     }
    
    public void updatePeriodic() {
        if (Math.abs(Robot.xboxController.getRightY()) > deadband) {
            double rightY = Math.pow(Robot.xboxController.getRightY(), 3 / 2);
            setElbowSpeed(rightY);
        }
    }
}
