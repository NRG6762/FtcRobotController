package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Launcher Autonomous Blue
 * Written by Aiden Maraia
 * Version: 04/12/2020
 * Feel free to make any changes and use at your disposal.
 */
@Autonomous(name="Launcher Autonomous Blue 1", group="@Competition")
//@Disabled
public class LauncherAutonomousBlue extends LauncherAutonomous {

    @Override
    public void loopBody(){

        switch (stepMove) {

            case "Emerge":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(12 * robot.ticksPerInchSide, 0, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Go Forth";
                    firstMove = true;
                }

                break;

            case "Go Forth":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    stepShoot = "Launch";
                    firstMove = false;
                }

                if(curvedMeccanumDrive(24 * robot.ticksPerInchFront, Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Smooth Across";
                    firstMove = true;
                }

                break;

            case "Smooth Across":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(smoothMeccanumDrive(12 * robot.ticksPerInchSide, Math.PI, tickSnapshot, robot.leftFront.getCurrentPosition(), 0.2)){
                    stepMove = "Make Room";
                    stepShoot = "Standby";
                    firstMove = true;
                }

                break;

            case "Make Room":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(12 * robot.ticksPerInchFront, Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Reorient";
                    firstMove = true;
                }

                break;

            case "Reorient":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(6 * robot.ticksPerInchSide, Math.PI, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Back Up";
                    stepShoot = "Launch";
                    firstMove = true;
                }

                break;

            case "Back Up":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(18 * robot.ticksPerInchSide, -Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "To Park";
                    firstMove = true;
                }

                break;

            case "To Park":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    stepShoot = "Stall";
                    firstMove = false;
                }

                if(curvedMeccanumDrive(12 * robot.ticksPerInchSide, Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Park";
                    firstMove = true;
                }

                break;

            case "Park":
                stopMotors();
                break;

        }

        switch (stepShoot) {

            case "Standby":

                robot.launcherSpeed(robot.standbyPower);

                break;

            case "Launch":

                robot.launcherSpeed(robot.launchPower);

                break;

            case "Stall":

                robot.launcherSpeed(0.0);

                break;

        }

        switch (stepCollect) {

            case "Run It":

                robot.launcherSpeed(0.0);

                break;

        }

        switch (stepGrab) {
        }

        switch (stepVuforia) {
        }
    }

}
