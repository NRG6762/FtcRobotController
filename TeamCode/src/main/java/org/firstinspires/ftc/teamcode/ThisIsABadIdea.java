package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
@TeleOp(name="This Is A Bad Idea", group="@Testing")
@Disabled
public class ThisIsABadIdea extends LinearOpMode{

    private LauncherHardware robot = new LauncherHardware(false, true, true, true, true, true, false, false, false, 100);

    private ElapsedTime runtime = new ElapsedTime();

    private float deadzone = 0.025f;

    private boolean newButton = true;
    private boolean wheelButton = true;
    private boolean conveyor_toggle = false;
    private boolean collector_toggle = false;
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

            //Create a deadzone for the joysticks
            gamepad1.setJoystickDeadzone(deadzone);
            gamepad2.setJoystickDeadzone(deadzone);

            double[][] drivePowah = robot.meccanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_y);

            //Send the scaled power levels to drive motors
            robot.leftFront.setPower(drivePowah[0][0]);
            robot.rightFront.setPower(drivePowah[0][1]);
            robot.leftBack.setPower(drivePowah[0][2]);
            robot.rightBack.setPower(drivePowah[0][3]);

            double[] rpm = robot.launcherRPM(runtime.milliseconds());

            telemetry.addData("Launcher 1 RPM", rpm[1]);
            telemetry.addData("Launcher 2 RPM", rpm[2]);

            telemetry.update();

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
                }else if(gamepad2.right_bumper){
                    if(conveyor_toggle == false){
                        conveyor_toggle = true;
                    }else{
                        conveyor_toggle = false;
                    }
                    newButton = false;
                }else if(gamepad2.left_bumper){
                    if(collector_toggle == false){
                        collector_toggle = true;
                    }else{
                        collector_toggle = false;
                    }
                    newButton = false;
                }
            }

            if(!gamepad2.dpad_up && !gamepad2.dpad_down && !gamepad2.dpad_right && !gamepad2.dpad_left && !gamepad2.right_bumper && !gamepad2.left_bumper){
                newButton = true;
            }

            if(conveyor_toggle == true){
                robot.conveyor.setPower(1);
            }
            if(collector_toggle == true){
                robot.collector.setPower(1);
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


            telemetry.addData("Launcher 1 Position", robot.launcher1.getCurrentPosition());
            telemetry.addData("Launcher 2 Position", robot.launcher2.getCurrentPosition());
            telemetry.addData("Place", place[index]);
            telemetry.addData("Launcher Speed", speed);
            telemetry.addData("RPM Buffer Time", rpm[0]);
            //telemetry.addData("Launcher 1 RPM", rpm[1]);
            //telemetry.addData("Launcher 2 RPM", rpm[2]);

            if(gamepad2.right_trigger < 1){
                robot.conveyor.setPower(gamepad2.right_trigger);
            }

            if(gamepad2.left_trigger < 1){
                robot.collector.setPower(gamepad2.left_trigger);
            }

            telemetry.addData("Conveyor Speed", gamepad2.right_trigger);
            telemetry.addData("Collector Speed", gamepad2.left_trigger);

            if(gamepad2.a){
                robot.grabber.setPower(0.0);
            }else if(gamepad2.b){
                robot.grabber.setPower(robot.grabberZero);
            }else if(gamepad2.y){
                robot.grabber.setPower(1.0);
            }

            telemetry.addData("Grabber Wheels Speed", robot.grabber.getPower());

            telemetry.update();

        }

    }
}

