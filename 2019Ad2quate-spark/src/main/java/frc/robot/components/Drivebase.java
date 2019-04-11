package frc.robot.components;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.*;

public class Drivebase {
    private final CANSparkMax frontLeft, frontRight, backLeft, backRight;

    public Drivebase(){
        frontLeft = new CANSparkMax(Constants.FRONT_LEFT_ID, MotorType.kBrushless);
        frontLeft.setInverted(Constants.FRONT_LEFT_INVERTED);

        frontRight = new CANSparkMax(Constants.FRONT_RIGHT_ID, MotorType.kBrushless);
        frontRight.setInverted(Constants.FRONT_RIGHT_INVERTED);

        backLeft = new CANSparkMax(Constants.BACK_LEFT_ID, MotorType.kBrushless);
        backLeft.setInverted(Constants.BACK_LEFT_INVERTED);

        backRight = new CANSparkMax(Constants.BACK_RIGHT_ID, MotorType.kBrushless);
        backRight.setInverted(Constants.BACK_RIGHT_INVERTED);
    }

    public void driveLeft(final double val){
        frontLeft.set(val);
        backLeft.set(val); 
    }

    public void driveRight(final double val){
        frontRight.set(val);
        backRight.set(val);
    }

    public void drive(final double left, final double right){
        driveLeft(left);
        driveRight(right);
    }


}