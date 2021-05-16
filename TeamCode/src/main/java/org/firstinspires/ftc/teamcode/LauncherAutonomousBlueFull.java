package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Launcher Autonomous Blue FullS
 * Written by Aiden Maraia
 * Version: 04/12/2020
 * Feel free to make any changes and use at your disposal.
 */
@Autonomous(name="Launcher Autonomous Blue Full", group="@Competition")
@Disabled
public class LauncherAutonomousBlueFull extends LauncherAutonomous {

    @Override
    public void loopBody(){

        switch (stepMove) {

            case "Emerge":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(24 * robot.ticksPerInchSide, Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Go Forth";
                    firstMove = true;
                }

                break;

            case "Go Forth":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    robot.launcherSpeed(robot.launchPower);
                    firstMove = false;
                }

                if(curvedMeccanumDrive(52 * robot.ticksPerInchFront, 0, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Jerk Back";
                    firstMove = true;
                }

                break;

            case "Jerk Back":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(4 * robot.ticksPerInchFront, Math.PI, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Smooth Across";
                    firstMove = true;
                }

                break;

            case "Smooth Across":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    timeSnapshot = runtime.milliseconds();
                    firstMove = false;
                }

                if(runtime.milliseconds() - 650 >= timeSnapshot){
                    robot.conveyor.setPower(1.0);
                    robot.collector.setPower(1.0);
                }

                if(smoothMeccanumDrive(24 * robot.ticksPerInchSide, -Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition(), 0.3)){
                    if(ringNumber == 1){
                        stepMove = "Forward B";
                    }else{
                        stepMove = "Left AC";
                    }
                    robot.launcherSpeed(0.0);
                    robot.conveyor.setPower(0.0);
                    robot.collector.setPower(0.0);
                    firstMove = true;
                }

                break;

                //Goal B

            case "Forward B":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(24 * robot.ticksPerInchFront, 0, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    robot.wobble.setPosition(robot.wobbleOpen);
                    stepMove = "Right B";
                    firstMove = true;
                }

                break;

            case "Right B":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(12 * robot.ticksPerInchSide, Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Back B";
                    firstMove = true;
                }

                break;

            case "Back B":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(24 * robot.ticksPerInchFront, Math.PI, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    stepMove = "Park";
                    firstMove = true;
                }

                break;

                //Goal A or C

            case "Left AC":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(24 * robot.ticksPerInchSide, -Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    if(ringNumber == 4){
                        stepMove = "Forward C";
                    }else if(ringNumber == 0){
                        stepMove = "Forward A";
                    }
                    firstMove = true;
                }

                break;

            case "Forward A":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(12 * robot.ticksPerInchFront, 0, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    robot.wobble.setPosition(robot.wobbleOpen);
                    stepMove = "Right AC";
                    firstMove = true;
                }

                break;

            case "Forward C":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(48 * robot.ticksPerInchFront, 0, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    robot.wobble.setPosition(robot.wobbleOpen);
                    stepMove = "Right AC";
                    firstMove = true;
                }

                break;

            case "Right AC":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(curvedMeccanumDrive(36 * robot.ticksPerInchSide, Math.PI / 2, tickSnapshot, robot.leftFront.getCurrentPosition())){
                    if(ringNumber == 4) {
                        stepMove = "Back C";
                    }else{
                        stepMove = "Park";
                    }
                    firstMove = true;
                }

                break;

            case "Back C":

                if(firstMove){
                    tickSnapshot = robot.leftFront.getCurrentPosition();
                    firstMove = false;
                }

                if(smoothMeccanumDrive(48 * robot.ticksPerInchFront, Math.PI, tickSnapshot, robot.leftFront.getCurrentPosition(), 0.2)){
                    stepMove = "To Park";
                    firstMove = true;
                }

                break;

            case "Park":
                stopMotors();
                break;

        }


    }

}
