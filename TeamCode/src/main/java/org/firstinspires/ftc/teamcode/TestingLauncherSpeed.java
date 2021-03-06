package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Testing Launcher Speed
 * Written by Aiden Maraia
 * Version: 04/12/2020
 * Feel free to make any changes and use at your disposal.
 */
@TeleOp(name="Testing Launcher Speed", group="@Testing")
//@Disabled
public class TestingLauncherSpeed extends LinearOpMode{

    private LauncherHardware robot = new LauncherHardware(false, false, true, true, true, true, false, false);

    private ElapsedTime runtime = new ElapsedTime();


    private boolean newButton = true;
    private boolean wheelButton = true;
    private double speed = 0;
    private int index = 0;
    double[] place = {0.1, 0.01, 0.001, 0.0001};

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

            if(newButton){
                if(gamepad1.dpad_up){
                    speed += place[index];
                    newButton = false;
                }else if(gamepad1.dpad_down){
                    speed -= place[index];
                    newButton = false;
                }else if(gamepad1.dpad_right){
                    index++;
                    newButton = false;
                }else if(gamepad1.dpad_left){
                    index--;
                    newButton = false;
                }
            }

            if(!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_right && !gamepad1.dpad_left){
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

            if(gamepad1.right_trigger < 1){
                robot.conveyor.setPower(gamepad1.right_trigger);
            }

            if(gamepad1.left_trigger < 1){
                robot.collector.setPower(gamepad1.left_trigger);
            }

            telemetry.addData("Conveyor Speed", gamepad1.right_trigger);
            telemetry.addData("Collector Speed", gamepad1.left_trigger);

            if(gamepad1.a){
                robot.grabber.setPower(0.0);
            }else if(gamepad1.b){
                robot.grabber.setPower(robot.grabberZero);
            }else if(gamepad1.y){
                robot.grabber.setPower(1.0);
            }

            telemetry.addData("Grabber Wheels Speed", robot.grabber.getPower());



            telemetry.update();

        }

    }
}
