package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * @author Error 404: Team Name Not Found
 * @see Chassis
 */
public class MecanumChassis extends Chassis
{

    private DcMotor rFrontMotor = null;
    private DcMotor rRearMotor = null;
    private DcMotor lFrontMotor = null;
    private DcMotor lRearMotor = null;

    /**
     * Look for a specified set of motors in the config file. If the motors are found, give them a
     * specified direction. If a motor is not found, ignore the error and set the motor to equal null.
     *
     * @param hwMap  The hardware map that the code will use to find and classify the objects it sees.
     */
    @Override
    public void init(HardwareMap hwMap)
    {
        try {
            rFrontMotor = hwMap.dcMotor.get("rightFront");
            rFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception p_exeception) {
            rFrontMotor = null;
        }
        try {
            lFrontMotor = hwMap.dcMotor.get("leftFront");
            lFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception p_exeception) {
            lFrontMotor = null;
        }
        try {
            rRearMotor = hwMap.dcMotor.get("rightRear");
            rRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception p_exeception) {
            rRearMotor = null;
        }
        try {
            lRearMotor = hwMap.dcMotor.get("leftRear");
            lFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception p_exeception) {
            rRearMotor = null;
        }
    }

    /**
     * This method takes the command values from the x- and y-axes of the left and right joysticks
     * on the gamepad and converts them to motor speed commands. The code then makes sure that the
     * command values don't exceed a magnitude of 1.
     *
     * The code making sure the speed commands don't exceed a value of 1 was sourced from:
     * http://www.chiefdelphi.com/media/papers/download/2906
     *
     * @param leftStickX  The x-axis of the left joystick
     * @param leftStickY  The y-axis of the left joystick
     * @param rightStickX  The x-axis of the right joystick
     * @param rightStickY  The y-axis of the right joystick
     */
    @Override
    public void joystickDrive(double leftStickX, double leftStickY, double rightStickX, double rightStickY)
    {
        // These are the calculations need to make a simple mecaccnum drive.
        // The left joystick controls moving straight forward/backward and straight sideways.
        // The right joystick control turning.
        double rightFront = (-leftStickY+rightStickX+leftStickX);
        double leftFront = (leftStickY+rightStickX+leftStickX);
        double rightRear=  (-leftStickY+rightStickX-leftStickX);
        double leftRear = (leftStickY+rightStickX-leftStickX);


        //Find the largest command value given and assign it to max.
        double max = Math.abs(leftFront);
        if(Math.abs(rightFront) > max) {max = Math.abs(rightFront);}
        if(Math.abs(leftRear) > max) {max = Math.abs(leftRear);}
        if(Math.abs(rightRear) > max) {max = Math.abs(rightRear);}

        // If max is greater than 1, divide all command values by max to ensure that all command
        // values stay below a magnitude of 1.
        if(max > 1)
        {
            leftFront/=max;
            rightFront/=max;
            leftRear/=max;
            rightRear/=max;
        }

        //Give the motors the final power values -- sourced from the calculations above.
        rFrontMotor.setPower(rightFront);
        lFrontMotor.setPower(leftFront);
        rRearMotor.setPower(rightRear);
        lRearMotor.setPower(leftRear);
    }


    /**
     * The drive method moves the four drive motors on the robot and will move the robot forward or
     * backward depending on whenther it recieved a positive or negative power value.
     *
     * @param distance  How far the robot will drive.
     * @param power  How fast the robot will drive.
     */
    @Override
    public void drive(double power, double distance)
    {
        // Haven't figured out how to incorporate distnace yet...

        // Call joystickDrive using the power parameter as a simulated joystick command
        joystickDrive(0, power,0,0);
     }

    /**
     * The pointTurn method turns the robot left or right depeinging on whether the power input is
     * positive or negative.
     *
     * @param heading  The direction in which the robot will move.
     * @param power  The power at which the robot will move.
     */
    @Override
    public void pointTurn(double power, double heading)
    {
        // Haven't figured out how to incorporate heading yet...

        // Call joystickDrive using the power parameter as a simulated joystick command
        joystickDrive(0, 0,power,0);

    }

    /**
     * Stop all four drive motors by setting their power to zero.
     */
    @Override
    public void stopMotors()
    {
        setPower(0.0);
    }

    /**
     * The strafe method moves the robot left or right depending on whether the power input is positive or negative.
     *
     * @param power  How fast the robot will move.
     * @param heading  At what heading the robot will move.
     */
    public void strafe(double power, double heading)  //joystickDrive(power,0,0,0)
    {
        // Haven't figured out how to incorporate heading yet...

        // Call joystickDrive using the power parameter as a simulated joystick command
        joystickDrive(power, 0,0,0);
    }

    /**
     * setMode sets all four drive motors to a specified mode. There are three mode choices:
     * 1) RUN_USING_ENCODERS,
     * 2) RUN_WITHOUT_ENCODERS, and
     * 3) RUN_TO_POSITION.
     *
     * @param mode The type of mode the motors will will run with.
     */
    private void setMode(DcMotor.RunMode mode)
    {
        if(rFrontMotor != null){rFrontMotor.setMode(mode);}
        if(lFrontMotor != null){lFrontMotor.setMode(mode);}
        if(rRearMotor != null) {rRearMotor.setMode(mode);}
        if(lRearMotor != null){lRearMotor.setMode(mode);}
    }

    /**
     * setDirection sets all four drive motors to a specified direction. There are two direction choices:
     * 1) FORWARD and
     * 2) REVERSE.
     *
     * @param direction  The direction the motors will use.
     */
    private void setDirection(DcMotorSimple.Direction direction)
    {
        if(rFrontMotor != null){rFrontMotor.setDirection(direction);}
        if(lFrontMotor != null){lFrontMotor.setDirection(direction);}
        if(rRearMotor != null) {rRearMotor.setDirection(direction);}
        if(lRearMotor != null){lRearMotor.setDirection(direction);}
    }

    /**
     * setPower set all four drive motors to a specified power.
     *
     * @param power  The power the motors will run at.
     */
    private void setPower(double power)
    {
        if(rFrontMotor != null) {rFrontMotor.setPower(power);}
        if(lFrontMotor != null) {lFrontMotor.setPower(power);}
        if(rRearMotor != null) {rRearMotor.setPower(power);}
        if(lRearMotor != null) {lRearMotor.setPower(power);}

    }





}