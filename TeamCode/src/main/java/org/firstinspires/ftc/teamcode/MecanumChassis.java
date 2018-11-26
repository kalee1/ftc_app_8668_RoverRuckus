package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * @author Error 404: Team Name Not Found
 * @see Chassis
 */
public class MecanumChassis extends Chassis
{

    /** The right front drive motor. */
    private DcMotor rFrontMotor = null;
    /** The right rear drive motor. */
    private DcMotor rRearMotor = null;
    /** The left front drive motor. */
    private DcMotor lFrontMotor = null;
    /** The left rear drive motor */
    private DcMotor lRearMotor = null;

    /** The navx gyro. */
    private NavxMicroNavigationSensor navx = null;

    static final double FORWARD = 0.0;
    static final double BACKWARD = 180.0;
    static final double RIGHT = -90;
    static final double LEFT = 90.0;

    static final double FORWARD_RIGHT_DIAGONAL = -45;
    static final double FORWARD_LEFT_DIAGONAL = 45.0;
    static final double REVERSE_RIGHT_DIAGONAL = -135;
    static final double REVERSE_LEFT_DIAGONAL = 135.0;

    final double COUNTS_PER_MOTOR_REV = 1120;
    final double DRIVE_GEAR_REDUCTION = 1.3;
    final double WHEEL_DIAMETER_INCHES = 4.0;
    final double INCH_PER_REV = WHEEL_DIAMETER_INCHES * 3.14159;
    final double COUNTS_PER_INCH = (1120*10)/(13*4*3.14159);

    /** An int variable used in drive, tankDrive, and pointTurn to capture the encoder position before each move. */
    int initialPosition;
    /** A double variable used in drive and tankDrive to capture the initial heading before each move. */
    double initialHeading;
    /** A double variable used in pointTurn to either turn the robot left or right. */
    double directionalPower;
    double direction;


    /**
     * Look for a specified set of motors in the config file. If the motors are found, give them a
     * specified direction. If a motor is not found, ignore the error and set the motor to equal null.
     *
     * @param hwMap  The hardware map that the code will use to find and classify the objects it sees.
     * @param telem  Initializes a telemetry object that allows for telemetry statements.
     */
    @Override
    public void init(HardwareMap hwMap, Telemetry telem)
    {
        super.init(hwMap, telem);

        try
        {
            rFrontMotor = hwMap.dcMotor.get("rightFront");
            rFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            rFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
        catch (Exception p_exeception)
        {
            rFrontMotor = null;
        }
        try
        {
            lFrontMotor = hwMap.dcMotor.get("leftFront");
            lFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            lFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception p_exeception)
        {
            lFrontMotor = null;
        }
        try
        {
            rRearMotor = hwMap.dcMotor.get("rightRear");
            rRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            rRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception p_exeception)
        {
            rRearMotor = null;
        }
        try
        {
            lRearMotor = hwMap.dcMotor.get("leftRear");
            lRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            lRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception p_exeception)
        {
            rRearMotor = null;
        }
        try {
            navx = hwMap.get(NavxMicroNavigationSensor.class, "navx");
        }
        catch (Exception p_exeception) {
            telem.addData("navx not found in config file", 0);
            navx = null;
        }

        telemetry.addData("heading", getHeadingDbl());
    }


    /**
     * This method takes the command values from the x- and y-axes of the left and right joysticks
     * on the gamepad and converts them to motor speed commands. The code then makes sure that the
     * command values don't exceed a magnitude of a selected limit.
     *
     * The code making sure the speed commands don't exceed a value of 1 was sourced from:
     * http://www.chiefdelphi.com/media/papers/download/2906
     *
     * @param leftStickX  The x-axis of the left joystick
     * @param leftStickY  The y-axis of the left joystick
     * @param rightStickX  The x-axis of the right joystick
     * @param rightStickY  The y-axis of the right joystick
     * @param powerLimit  The max power allowed to the motors
     */
    @Override
    public void joystickDrive(double leftStickX, double leftStickY, double rightStickX, double rightStickY, double powerLimit)
    {
        /*
            These are the calculations need to make a simple mecaccnum drive.
              - The left joystick controls moving straight forward/backward and straight sideways.
              - The right joystick control turning.
        */
        double rightFront = (-leftStickY+rightStickX+leftStickX);
        double leftFront = (leftStickY+rightStickX+leftStickX);
        double rightRear=  (-leftStickY+rightStickX-leftStickX);
        double leftRear = (leftStickY+rightStickX-leftStickX);


        //Find the largest command value given and assign it to max.
        double max = 0.0;
        if(Math.abs(leftFront) > max) {max = Math.abs(leftFront);}
        if(Math.abs(rightFront) > max) {max = Math.abs(rightFront);}
        if(Math.abs(leftRear) > max) {max = Math.abs(leftRear);}
        if(Math.abs(rightRear) > max) {max = Math.abs(rightRear);}

        //Set the minimum and maximum power allowed for drive moves and compare it to the parameter powerLimit.
        powerLimit = Range.clip(powerLimit, .05, 1);
        //If max still equals zero after checking all four motors, then set the max to 1
        if(max == 0.0)
        {
            max = 1;
        }

        // If max is greater than the power limit, divide all command values by max to ensure that all command
        // values stay below the magnitude of the power limit.
        if(max > powerLimit)
        {
            leftFront = leftFront / max * powerLimit;
            rightFront = rightFront / max * powerLimit;
            leftRear = leftRear / max * powerLimit;
            rightRear = rightRear / max *powerLimit;
        }

        //Give the motors the final power values -- sourced from the calculations above.
        if(rFrontMotor != null)
        {
            rFrontMotor.setPower(rightFront);
        }

        if(lFrontMotor != null)
        {
            lFrontMotor.setPower(leftFront);
        }

        if(rRearMotor != null)
        {
            rRearMotor.setPower(rightRear);
        }

        if(lRearMotor != null)
        {
            lRearMotor.setPower(leftRear);
        }

//        telemetry.addData("1. right front encoder", rFrontMotor.getCurrentPosition());
//        telemetry.addData("2. left front encoder", lFrontMotor.getCurrentPosition());
//        telemetry.addData("3. right rear encoder", rRearMotor.getCurrentPosition());
//        telemetry.addData("4. left rear encoder", lRearMotor.getCurrentPosition());

//        telemetry.addData("5. right front power", rFrontMotor.getPower());
//        telemetry.addData("6. left front power", lFrontMotor.getPower());
//        telemetry.addData("7. right rear power", rRearMotor.getPower());
//        telemetry.addData("8. left rear power", lRearMotor.getPower());
    }


    /**
     * The mecanumDrive method moves the four drive motors on the robot and will move the robot forward,
     * backward, left, right, or at a 45 degree angle in any direction.
     *
     * @param power  How fast the robot will drive.
     * @param gain  The rate at which the robot will correct an error
     * @param direction  In which direction the robot will drive (forward, backward, left, right,
     *                   or 45 degrees in any direction).
     * @param distance  How far the robot will drive.
     * @param time  The max time this move can take. A time-out feature: if the move stalls for some
     *              reason, the timer will catch it.
     * @return  A boolean that tells us whether or not the robot is moving.
     */
    @Override
    public boolean drive(double power, double direction, double gain, double distance, double time)
    {
        double driveDistance = COUNTS_PER_INCH * distance;
        double correction;
        double actual = getHeadingDbl();

        if(Math.abs(direction) > 130  &&  actual < 0.0)
        {
            actual += 360;
        }
        if(!moving)
        {
            initialHeading = getHeadingDbl();
            if(Math.abs(direction) > 130  &&  initialHeading < 0.0)
            {
                initialHeading += 360.0;
            }
            if(direction == FORWARD_LEFT_DIAGONAL || direction  == REVERSE_LEFT_DIAGONAL)
            {
                initialPosition = rFrontMotor.getCurrentPosition();
            }
            else
            {
                initialPosition = lFrontMotor.getCurrentPosition();
            }
            resetStartTime();
            moving = true;
        }
        correction = ((initialHeading - actual) * gain);

        double lStickX = power * Math.sin(Math.toRadians(direction));
        double lStickY = -(power * Math.cos(Math.toRadians(direction)));

        telemetry.addData("stick x", lStickX);
        telemetry.addData("stick y", lStickY);

        joystickDrive(lStickX, lStickY, correction, 0.0, power);

        if(((Math.abs(lFrontMotor.getCurrentPosition() - initialPosition)) >= driveDistance) || (getRuntime() > time))
        {
            stopMotors();
            moving = false;
        }

        return !moving;
     }


     /**
      * A drive method that does not utilize the mecanum wheels on the robot and therefore only
      * drives forward and backward
      *
      * @param power
      * @param direction
      * @param gain
      * @param distance
      * @param time
      * @return A boolean that says whether or not the robot is moving
      */
     public boolean tankDrive(double power, TankDirection direction, double gain, double distance, double time)
     {
         double driveDistance = COUNTS_PER_INCH * distance;
         double correction;
         double actual = getHeadingDbl();
         double numDirection = 0.0;

         if(direction == TankDirection.FORWARD)
         {
             //forward
             numDirection = 0.0;
         }
         else
         {
             //backward
             numDirection = 180.0;
         }
         if(direction == TankDirection.REVERSE)
         {
             actual += 360.0;
         }

         if(!moving)
         {
             initialHeading = getHeadingDbl();
             if(direction == TankDirection.REVERSE)
             {
                 initialHeading += 360.0;
             }
             initialPosition = lFrontMotor.getCurrentPosition();
             resetStartTime();
             moving = true;
         }
         correction = ((initialHeading - actual) * gain);

         double lStickY = -(power * Math.cos(Math.toRadians(numDirection)));

         joystickDrive(0.0, lStickY, correction,0.0, power);

         if(((Math.abs(lFrontMotor.getCurrentPosition() - initialPosition)) >= driveDistance) || (getRuntime() > time))
         {
             stopMotors();
             moving = false;
         }

         return !moving;
     }


    /**
     * The pointTurn method turns the robot left or right depending on whether the power input is
     * positive or negative.
     *
     * @param targetHeading  The direction in which the robot will move.
     * @param time  The maximum time the move can take before the code moves on.
     * @param power  The power at which the robot will move.
     * @return A boolean that tells use whether or not the robot is moving.
     */
    @Override
    public boolean pointTurn(double power, TurnDirection turnDirection, double targetHeading, double time)
    {

        double currentHeading = getHeadingDbl();

        if(targetHeading <= -170)
        {
            targetHeading += 360;
        }
        else if(Math.abs(targetHeading) >= 170  &&  currentHeading < 0)
        {
            currentHeading =+ 360;
        }

        if(!moving)
        {
            if(turnDirection == Chassis.TurnDirection.RIGHT)
            {
                direction = -1;
                directionalPower = -power * direction;
            }
            else
            {
                direction = 1;
                directionalPower = power * direction;
            }

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            resetStartTime();
            moving = true;
        }

        joystickDrive(0.0, 0.0, directionalPower, 0.0, power);

        if(Math.abs(currentHeading - targetHeading) < 4.0 || getRuntime() > time)
        {
            stopMotors();
            moving = false;
        }

        telemetry.addData("heading", getHeadingDbl());

        return !moving;
    }

    /**
     * Used to get the robot's heading.
     *
     * @return  the robtot's heading as an double
     */
    public double getHeadingDbl()
    {
        Orientation angles = navx.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
    }


    /**
     * Stop all four drive motors by setting their power to zero.
     */
    @Override
    public void stopMotors()
    {
        setPower(0.0);
        moving = false;
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
