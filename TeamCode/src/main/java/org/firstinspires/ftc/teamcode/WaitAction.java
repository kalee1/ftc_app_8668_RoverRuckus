package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.ams.AMSColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Loaded into the run map as an action that waits a set amount of time. Each action is parameterized by the CSV file.
 *
 * @author Andrew, Error 404: Team Name Not Found
 * @see RobotAction
 * */
public class WaitAction extends RobotAction
{


    /** Creates a new object from the supplied parameters. */
    WaitAction(String id, String nextAction, double duration)
    {
        super( id, nextAction, duration);
//        theId = id;
//        timeout = duration;
//
//        if(nextAction.isEmpty())
//        {
//            theNextAction = null;
//        }
//        else
//        {
//            theNextAction = nextAction;
//        }

    }

    /** Takes the parameters from the CSV file, converts them appropriately, and calls the
     * parameterized constructor */
    WaitAction(String[] params)
    {
        this(params[0], params[1], Double.parseDouble(params[2]));
    }


    /** Placeholder for entry. Currently only calls the parent entry method.  */
    @Override
    public void init(Telemetry telem, RuckusBot theRobot)
    {
        super.init(telem, theRobot);
    }

    @Override
    public void entry()
    {
        super.entry();
    }

    /** Placeholder for execute. Calls the parent execute method. */
    @Override
    public boolean execute()
    {
        telemetry.addData("timeout: ", timeout);
        telemetry.addData("get run time: ", getRuntime());
        return super.execute();
    }

    /** Stops the motors and servos and calls the parent exit method. */
    public void exit()
    {
        robot.stopMotors();
        robot.collectorStop();
        super.exit();
    }

}
