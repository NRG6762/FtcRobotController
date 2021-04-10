package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

/*
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Launcher Autonomous
 * Written by Aiden Maraia
 * Version: 3/7/2020
 * Feel free to make any changes and use at your disposal.
 */
@Autonomous(name="Launcher Autonomous", group="Autonomous")
//@Disabled
public class LauncherAutonomous extends LinearOpMode {

    private LauncherHardware robot = new LauncherHardware(true, true);
    private ElapsedTime runtime = new ElapsedTime();

    private String stepMove = "To Goal Line";
    private boolean firstMove = true;
    private String stepShoot = "Up To Speed";
    private boolean firstShoot = true;
    private String stepCollect = "Off";
    private boolean firstCollect = true;
    private String stepGrab = "Off";
    private boolean firstGrab = true;
    private String stepVuforia = "On";
    private boolean firstVuforia = true;

    private int ringNumber = 0;

    //Declare variables used only in this class
    private float deadzone = 0.025f;
    private boolean activatorA = true;
    private int activatorB = 0;
    private boolean firstTime = true;

    @Override
    public void runOpMode() {

        //Initialize the Hardware Map
        robot.init(hardwareMap);

        //Signify the Hardware Map has been initialized
        telemetry.addData("Status", "Initialized");

        //Activate TensorFlow Object Detection
        if (robot.tfod != null) robot.tfod.activate();

        //Run until the start of the match (driver presses START/PLAY)
        while(!isStarted()){

            if (robot.tfod != null) {

                List<Recognition> recognitions = robot.tfod.getRecognitions();
                int maxRingNumber = 0;

                if (recognitions != null) {
                    for (Recognition recognition : recognitions) {
                        int tempRingNumber = 0;
                        if (recognition.getLabel().equals("Single")) {
                            tempRingNumber = 1;
                        }else if(recognition.getLabel().equals("Quad")){
                            tempRingNumber = 4;
                        }
                        if(tempRingNumber > maxRingNumber) maxRingNumber = tempRingNumber;
                    }
                }

                ringNumber = maxRingNumber;

                telemetry.addData("Ring Number", ringNumber)
            }

            //Update the telemetry
            telemetry.update();
        }

        //Reset the game clock to zero in Start()
        runtime.reset();

        //Deactivate TensorFlow Object Detection
        if (robot.tfod != null) robot.tfod.shutdown();

        //Activate the vision stuff
        robot.targetsUltimateGoal.activate();

        //Run until the end of the match (driver presses STOP)
        while (!isStopRequested()) {

            //
            switch (stepMove) {
            }

            switch (stepShoot) {
            }

            switch (stepCollect) {
            }

            switch (stepGrab) {
            }

            switch (stepVuforia) {
            }

        }

    }
}
