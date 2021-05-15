package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
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

    LauncherHardware robot = new LauncherHardware(true, true, true, true, true, true,true, true);
    ElapsedTime runtime = new ElapsedTime();

    String stepMove = "Emerge";
    boolean firstMove = true;
    String stepShoot = "Standby";
    boolean firstShoot = true;
    String stepCollect = "Off";
    boolean firstCollect = true;
    String stepGrab = "Off";
    boolean firstGrab = true;

    int tickSnapshot = 0;
    double initHeading = 0;
    double shootTime = 0;

    int ringNumber = 0;

    @Override
    public void runOpMode() {

        //Initialize the Hardware Map
        robot.init(hardwareMap);

        //Signify the Hardware Map has been initialized
        telemetry.addData("Status", "Initialized");

        initHeading = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.RADIANS).firstAngle;

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

        //Run until the end of the match (driver presses STOP)
        while (!isStopRequested()) {

            loopBody();

            telemetry.addData("Step Move", stepMove);
            telemetry.addData("Step Shoot", stepShoot);
            telemetry.addData("Step Collect", stepCollect);
            telemetry.addData("Step Grab", stepGrab);

            telemetry.update();

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

    }

    boolean curvedMeccanumDrive(double distance, double direction, double startPos, double currPos){

        double power = motorCurve((double)(currPos - startPos) / distance);

        //direction = direction - Math.PI / 2;

        if(power <= 0.01){

            stopMotors();

            return true;

        }else{

            autoMeccanumDrive(power, direction, 0);

            return false;

        }
    }

    boolean smoothMeccanumDrive(double distance, double direction, double startPos, double currPos, double power){

        if((double)(currPos - startPos) / distance >= 0.99){

            stopMotors();

            return true;

        }else{

            autoMeccanumDrive(power, direction, 0);

            return false;

        }
    }

    void stopMotors(){
        robot.leftFront.setPower(0.0);
        robot.rightFront.setPower(0.0);
        robot.leftBack.setPower(0.0);
        robot.rightBack.setPower(0.0);
    }

    void autoMeccanumDrive(double speed, double direction, double spin){

        spin += robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.RADIANS).firstAngle - initHeading;

        double drive1 = speed * Math.sin(direction + (Math.PI/4)) + spin;
        double drive2 = speed * Math.cos(direction + (Math.PI/4)) - spin;
        double drive3 = speed * Math.cos(direction + (Math.PI/4)) + spin;
        double drive4 = speed * Math.sin(direction + (Math.PI/4)) - spin;

        //Set maxValue to the absolute value of first power level
        double scale = Math.abs(drive1);

        //If the absolute value of the second power level is less than the maxValue, make it the new maxValue
        if (Math.abs(drive2) > scale) scale = Math.abs(drive2);

        //If the absolute value of the third power level is less than the maxValue, make it the new maxValue
        if (Math.abs(drive3) > scale) scale = Math.abs(drive3);

        //If the absolute value of the fourth power level is less than the maxValue, make it the new maxValue
        if (Math.abs(drive4) > scale) scale = Math.abs(drive4);

        //Check if need to scale -- if not set maxValue to 1 to nullify scaling
        if (scale < 1) scale = 1;

        //Apply the scale to the outputs for each wheel (final values)
        robot.leftFront.setPower(drive1/scale);
        robot.rightFront.setPower(drive2/scale);
        robot.leftBack.setPower(drive3/scale);
        robot.rightBack.setPower(drive4/scale);

    }

    double motorCurve(double currPos){
        if(currPos < .2){
            return 1.0 - 20 * (.2 - currPos) * (.2 - currPos);
        }else if(currPos <= .6){
            return 1.0;
        }else if(currPos < .9){
            return 1.0 - 50 * (.8 - currPos) * (.8 - currPos);
        }else if(currPos < 1.0){
            return 50 * (1 - currPos) * (1 - currPos);
        }
        return 0.0;
    }

}
