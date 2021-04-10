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

/*
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Drivable Vision
 * Written by Aiden Maraia
 * Version: 11/13/2020
 * Feel free to make any changes and use at your disposal.
 */
@TeleOp(name="Vision Drivable", group="Vision Testing")
//@Disabled
public class LauncherTeleOP extends LinearOpMode{

    private LauncherHardware robot = new LauncherHardware(false, true);

    private ElapsedTime runtime = new ElapsedTime();

    private float deadzone = 0.025f;

    double[][] launcherNumbers = new double[3][2];

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

        launcherNumbers[0][0] = runtime.milliseconds();
        launcherNumbers[1][0] = robot.launcher1.getCurrentPosition();
        launcherNumbers[2][0] = robot.launcher2.getCurrentPosition();

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

            launcherNumbers[0][1] = runtime.milliseconds();
            launcherNumbers[1][1] = robot.launcher1.getCurrentPosition();
            launcherNumbers[2][1] = robot.launcher2.getCurrentPosition();



            telemetry.addData("Launcher 1 RPM", robot.launcherRPM(launcherNumbers[1][0], launcherNumbers[1][1], launcherNumbers[0][0], launcherNumbers[0][1]));
            telemetry.addData("Launcher 2 RPM", robot.launcherRPM(launcherNumbers[2][0], launcherNumbers[2][1], launcherNumbers[0][0], launcherNumbers[0][1]));

            telemetry.update();

        }

    }
}
