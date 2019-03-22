package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 *
 * @author Andrew, Error 404: Team Name Not Found
 * @see RobotAction
 * */
public class DriveAction extends RobotAction
{
    double power;
    double direction;
    double gain;
    double distance;
    String id;
    String nextAction;

    DriveAction(double thePower, double theDirection, double theGain, double theDistance,
                double duration, String theId, String theNextAction)
    {
        power = thePower;
        direction = theDirection;
        gain = theGain;
        distance = theDistance;
        timeout = duration;
        id = theId;

        if(theNextAction.isEmpty())
        {
            nextAction = null;
        }
        else
        {
            nextAction = theNextAction;
        }
    }

    @Override
    public void exit()
    {
        robot.stopMotors();
        super.exit();
    }

    @Override
    public boolean execute()
    {
        return robot.drive(power, direction, gain, distance, timeout) || super.execute();
    }
}
