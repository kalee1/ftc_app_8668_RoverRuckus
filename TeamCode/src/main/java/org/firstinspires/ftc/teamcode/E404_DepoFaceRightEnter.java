package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="DepoFace (Right Enter)", group="Field Position")

public class E404_DepoFaceRightEnter extends Error404Autonomus
{
    @Override public void init()
    {
        // values are based on my incredible guesstimating including the use of a scaled version of the field and some math.

        mineralDriveDistance = 55.0;
        mineralSlideDistance = 0.0;
        depoTurnHeading = 45.0; // ~45 degrees
        depoDriveDistance = 0.0;
        headingReset = 45;
        craterDriveDistance = 50;
        craterTurnHeading = 85;
        craterSlideDistance = 60;
        enterCraterDistance = 35;
        super.init();
    }

    @Override public void start()
    {
        super.start();
    }


    @Override public void loop()
    {
        super.loop();
    }
}