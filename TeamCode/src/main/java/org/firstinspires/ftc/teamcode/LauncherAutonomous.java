package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Launcher Autonomous
 * Written by Aiden Maraia
 * Version: 04/12/2020
 * Feel free to make any changes and use at your disposal.
 */
//@Autonomous(name="Launcher Autonomous", group="Autonomous")
//@Disabled
public abstract class LauncherAutonomous extends LinearOpMode {

    LauncherHardware robot = new LauncherHardware(true, true, 100);
    ElapsedTime runtime = new ElapsedTime();

    String stepMove = "To Goal Line";
    boolean firstMove = true;
    String stepShoot = "Up To Speed";
    boolean firstShoot = true;
    String stepCollect = "Off";
    boolean firstCollect = true;
    String stepGrab = "Off";
    boolean firstGrab = true;
    String stepVuforia = "On";
    boolean firstVuforia = true;

    int ringNumber = 0;

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

                telemetry.addData("Ring Number", ringNumber);
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

            double[] rpm = robot.launcherRPM(runtime.milliseconds());

            loopBody();

        }

    }

    public void loopBody(){
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
