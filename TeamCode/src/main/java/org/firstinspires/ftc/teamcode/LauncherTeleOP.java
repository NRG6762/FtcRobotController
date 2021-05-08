package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Launcher TeleOP
 * Written by Aiden Maraia
 * Version: 04/12/2020
 * Feel free to make any changes and use at your disposal.
 */
@TeleOp(name="Launcher TeleOP", group="@Competition")
//@Disabled
public class LauncherTeleOP extends LinearOpMode{

    private LauncherHardware robot = new LauncherHardware(false, true, true, true, true, true,false, true, false, 100);

    private ElapsedTime runtime = new ElapsedTime();

    private float deadzone = 0.025f;

    private boolean newButton = true;
    private double speed = 0;
    private int index = 0;
    double[] place = {0.1, 0.01, 0.001, 0.0001};

    private boolean stickToggleL = true;
    private boolean stickButtonL = true;
    private boolean stickToggleR = true;
    private boolean stickButtonR = true;

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

        //Run until the end of the match (driver presses STOP)
        while (!isStopRequested()) {

            //Drive Control

            //Create a deadzone for the joysticks
            gamepad1.setJoystickDeadzone(deadzone);
            gamepad2.setJoystickDeadzone(deadzone);

            //Calculate the necessary values for the drive motors
            double[][] drivePowah = robot.meccanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            //Send the scaled power levels to drive motors
            robot.leftFront.setPower(drivePowah[0][0]);
            robot.rightFront.setPower(drivePowah[0][1]);
            robot.leftBack.setPower(drivePowah[0][2]);
            robot.rightBack.setPower(drivePowah[0][3]);

            //Flywheel Control

            if(newButton){
                if(gamepad2.dpad_up){
                    speed += place[index];
                    newButton = false;
                }else if(gamepad2.dpad_down){
                    speed -= place[index];
                    newButton = false;
                }else if(gamepad2.dpad_right){
                    index++;
                    newButton = false;
                }else if(gamepad2.dpad_left){
                    index--;
                    newButton = false;
                }
            }

            if(!gamepad2.dpad_up && !gamepad2.dpad_down && !gamepad2.dpad_right && !gamepad2.dpad_left && !gamepad2.right_bumper && !gamepad2.left_bumper){
                newButton = true;
            }

            if(speed > 1.0){
                speed = 1.0;
            }else if(speed < -1.0){
                speed = -1.0;
            }

            if(index > 3){
                index = 0;
            }else if(index < 0){
                index = 3;
            }

            robot.launcherSpeed(speed);

            telemetry.addData("Place", place[index]);
            telemetry.addData("Launcher Speed", speed);

            //Conveyor Control

            if(gamepad2.right_stick_button){
                stickButtonR = false;
                stickToggleR = !stickToggleR;
            }else{
                stickButtonR = true;
            }

            if(stickToggleR && gamepad2.right_stick_y <= 1 && gamepad2.right_stick_y >= -1 ){
                robot.conveyor.setPower(-gamepad2.right_stick_y);
            }

            if(gamepad2.left_stick_button){
                stickButtonL = false;
                stickToggleL = !stickToggleL;
            }else{
                stickButtonL = true;
            }

            if(stickToggleL && gamepad2.left_stick_y <= 1 && gamepad2.left_stick_y >= -1){
                robot.collector.setPower(-gamepad2.left_stick_y);
            }

            telemetry.addData("Conveyor Speed", robot.conveyor.getPower());
            telemetry.addData("Collector Speed", robot.collector.getPower());

            //Grabber Wheels Control

            if(gamepad2.a){
                robot.grabber.setPower(0.0);
            }else if(gamepad2.b){
                robot.grabber.setPower(1.0);
            }

            telemetry.update();

        }

    }
}
