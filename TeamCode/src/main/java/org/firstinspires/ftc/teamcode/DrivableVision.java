package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Testing Drivable Vision
 * Written by Aiden Maraia
 * Version: 04/12/2020
 * Feel free to make any changes and use at your disposal.
 */
@TeleOp(name="Vision Drivable", group="@Testing")
//@Disabled
public class DrivableVision extends LinearOpMode{

    private LauncherHardware robot = new LauncherHardware(true, true, false, false, false, false,false, false);

    private ElapsedTime runtime = new ElapsedTime();

    //Declare variables used only in this class
    private float deadzone = 0.025f;
    private boolean activatorA = true;
    private int activatorB = 0;
    private boolean firstTime = false;

    @Override
    public void runOpMode() {

        //Initialize the Hardware Map
        robot.init(hardwareMap);

        //Signify the Hardware Map has been initialized
        telemetry.addData("Status", "Initialized");

        //Update the telemetry
        telemetry.update();

        //Wait for the Pressing of the Start Button
        waitForStart();

        //Reset the game clock to zero in Start()
        runtime.reset();

        //Activate the vision stuff
        robot.targetsUltimateGoal.activate();
        if (robot.tfod != null) robot.tfod.activate();

        //Run until the end of the match (driver presses STOP)
        while (!isStopRequested()) {

            /* DRIVE CODE */

            //Create a deadzone for the joysticks
            gamepad1.setJoystickDeadzone(deadzone);
            gamepad2.setJoystickDeadzone(deadzone);

            //Do the math to find the power for each of the wheels.
            double[][] drivePowah = robot.meccanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_y);

            //Send the scaled power levels to drive motors
            robot.leftFront.setPower(drivePowah[0][0]);
            robot.rightFront.setPower(drivePowah[0][1]);
            robot.leftBack.setPower(drivePowah[0][2]);
            robot.rightBack.setPower(drivePowah[0][3]);

            /* VISION CODE */

            //Switch vision mode using the A button, starting as off.
            if(gamepad1.a && activatorA){
                activatorB++;
                if(activatorB >= 3) activatorB = 0;
                activatorA = false;
                firstTime = true;
            }else if(!gamepad1.a){
                activatorA = true;
            }

            //Run VuMark Tracking
            if(activatorB == 1) {

                //Tell drive what mode vision is on
                telemetry.addLine("Vision: VuMark ID");

                //Check if a target, any target, is seen
                robot.targetVisible = false;
                for (VuforiaTrackable trackable : robot.allTrackables) {
                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                        telemetry.addData("Visible Target", trackable.getName());
                        robot.targetVisible = true;

                        // getUpdatedRobotLocation() will return null if no new information is available since
                        // the last time that call was made, or if the trackable is not currently visible.
                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                        if (robotLocationTransform != null) {
                            robot.lastLocation = robotLocationTransform;
                        }
                        break;
                    }
                }

                // Provide feedback as to where the robot is located (if we know).
                if (robot.targetVisible) {
                    // express position (translation) of robot in inches.
                    VectorF translation = robot.lastLocation.getTranslation();
                    telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                            translation.get(0) / robot.mmPerInch, translation.get(1) / robot.mmPerInch, translation.get(2) / robot.mmPerInch);

                    // express the rotation of the robot in degrees.
                    Orientation rotation = Orientation.getOrientation(robot.lastLocation, EXTRINSIC, XYZ, DEGREES);
                    telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
                } else {
                    telemetry.addData("Visible Target", "none");
                }

            //Run Object Tracking
            }else if(activatorB == 2){

                //Tell drive what mode vision is on
                telemetry.addLine("Vision: Object ID");

                if (robot.tfod != null) {
                    // getUpdatedRecognitions(+) will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> recognitions = robot.tfod.getRecognitions();
                    if (recognitions != null) {
                        telemetry.addData("# Object Detected", recognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : recognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                        }
                    }
                }

            }else{

                telemetry.addLine("Vision: None Currently :)");

            }

            telemetry.update();

        }

        //Close out any remaining vision processes
        if(activatorB == 1){
            robot.targetsUltimateGoal.deactivate();
        }else if(activatorB == 2){
            if (robot.tfod != null) robot.tfod.shutdown();
        }

    }
}
