package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Skystone
 * Drivable Vision
 * Written by Aiden Maraia
 * Version: 11/10/2020
 * Feel free to make any changes and use at your disposal.
 */
@TeleOp(name="Vision Drivable", group="Vision Testing")
//@Disabled
public class DrivableVision extends LinearOpMode{

    private LauncherHardware robot = new LauncherHardware(true, true);

    private ElapsedTime runtime = new ElapsedTime();

    private float deadzone = 0.025f;

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
        while (opModeIsActive()) {

            //Create a deadzone for the joysticks
            gamepad1.setJoystickDeadzone(deadzone);
            gamepad2.setJoystickDeadzone(deadzone);

            double[][] drivePowah = robot.meccanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_y);

            //Send the scaled power levels to drive motors
            robot.leftFront.setPower(drivePowah[0][0]);
            robot.rightFront.setPower(drivePowah[0][1]);
            robot.leftBack.setPower(drivePowah[0][2]);
            robot.rightBack.setPower(drivePowah[0][3]);

        }

    }
}
