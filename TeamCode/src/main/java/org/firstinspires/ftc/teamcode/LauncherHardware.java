package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/**
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Hardware Class
 * Written by Aiden Maraia,
 * Version: 3/7/2020
 * Feel free to make any changes and use at your disposal.
 */
public class LauncherHardware {

    //Global Activators
    public boolean                  visionActive;
    public boolean                  restActive;

    //Declare Drive Motors & Activator
    public boolean                  driveMotorsTrue     = true;
    public DcMotor                  leftFront           = null;
    public DcMotor                  rightFront          = null;
    public DcMotor                  leftBack            = null;
    public DcMotor                  rightBack           = null;

    //Declare Drive Motor Values
    public double                   ticksPerInchFront   = 60;
    public double                   ticksPerInchSide    = 70;

    //Declare Launcher Motors/Activator
    public boolean                  launcherDriveTrue   = false;
    public DcMotor                  launcher1           = null;
    public DcMotor                  launcher2           = null;

    //Declare Grabber Motors/Activator
    public boolean                  grabberTrue         = false;
    public DcMotor                  grabber             = null;

    //Declare Conveyor Motors/Activator
    public boolean                  conveyorTrue        = false;
    public DcMotor                  conveyor            = null;

    //Declare Aimer Servo/Activator
    public boolean                  aimerTrue           = false;
    public Servo                    aimer               = null;

    //Declare IMU/Activator
    public boolean                  imuTrue             = true;
    public BNO055IMU                imu                 = null;

    //Declare Distance Sensors/Activator
    public boolean                  distanceTrue        = false;
    public DistanceSensor           distanceSide        = null;
    public DistanceSensor           distanceFront       = null;

    //Declare Vision Sensors/Objects/Activator
    public Boolean                  visionTrue          = true;
    public OpenGLMatrix             lastLocation        = null;
    public VuforiaLocalizer         vuforia             = null;
    public VuforiaLocalizer.Parameters     parameters   = null;
    public VuforiaTrackables        targetsUltimateGoal = null;
    public OpenGLMatrix             robotFromCamera     = null;
    public List<VuforiaTrackable>   allTrackables       = null;
    public WebcamName               webcam              = null;
    public TFObjectDetector         tfod                = null;

    //Declare Vision Variables
    //Digital Information
    public static final String VUFORIA_KEY =
            "AeBHpT//////AAABmVuiA5UnWEyfiHdkKh7y5HUKxtNnKfUiPUxg1EChNg5mayvOWsVPnnFMXk6p9NRq7PgxAocbY3kWK8ja1SriqRTf+rEFMVYu4uc8WmQzZz8KFG2o0ORG0xNRnHKhUC/ruCm1ochnmB8r8r2V4bb0+kdvTGeFqwGx3fca3Y+/tKRHECb9zx7Vb3fO1USHtxj4rWngJKrkhkhTQ/SaslJvYdM4iUdnB+uRDhujTOfHXZTLY9o+um4j9IellV4zjdnaBQ0dljrYVT8zKN+cgx142U70l5A+pT9Tove+h8w2X/P6mjheJ5ILqY0ZYgbpanOe5DN2HiwTkbBASxF735iiIQG8K0r4ZCOOaikMczWDelo8\n";
    public static final String TFOD_MODEL_ASSET         = "UltimateGoal.tflite";
    public static final String LABEL_FIRST_ELEMENT      = "Quad";
    public static final String LABEL_SECOND_ELEMENT     = "Single";
    public boolean targetVisible                        = false;
    //Location Information
    public final float CAMERA_FORWARD_DISPLACEMENT      = 9.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot-center
    public final float CAMERA_VERTICAL_DISPLACEMENT     = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
    public final float CAMERA_LEFT_DISPLACEMENT         = 0;     // eg: Camera is ON the robot's center line
    public static final float mmPerInch                 = 25.4f;
    public static final float mmTargetHeight            = (6) * mmPerInch;
    public static final float halfField                 = 72 * mmPerInch;
    public static final float quadField                 = 36 * mmPerInch;
    //TensorFlow Parameters
    public static final float tfodConfidence            = 0.5f;
    public static final double tfodMagnification        = 2.5;
    public static final double tfodAspectRatio          = 16.0/9.0;

    //Local OpMode Members
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    //Constructor
    public LauncherHardware(boolean visionActive, boolean restActive){
        this.visionActive = visionActive;
        this.restActive = restActive;
    }

    //Initialize standard Hardware interfaces
    public void init(HardwareMap ahwMap){

        //Save reference to Hardware map
        hwMap = ahwMap;

        /** MOTORS & SENSORS */
        if(restActive) {

            /** Drive Motor Initialization */
            if (driveMotorsTrue) {

                //Get Drive Motors
                leftFront = ahwMap.dcMotor.get("left_front");
                rightFront = ahwMap.dcMotor.get("right_front");
                leftBack = ahwMap.dcMotor.get("left_back");
                rightBack = ahwMap.dcMotor.get("right_back");

                //Reset Drive Motor Encoders
                leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                //Set Drive Motor Modes
                leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                //Set Drive Motor Directions
                leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
                rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
                leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
                rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

                //Set Drive Motor Powers
                leftFront.setPower(0.0);
                rightFront.setPower(0.0);
                leftBack.setPower(0.0);
                rightBack.setPower(0.0);

            }

            /** Launcher Motor Initialization */
            if (launcherDriveTrue) {

                //Get Launcher Motors
                launcher1 = ahwMap.dcMotor.get("launcher_1");
                launcher2 = ahwMap.dcMotor.get("launcher_2");

                //Reset Launcher Motor Encoders
                launcher1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                launcher2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                //Set Launcher Motor Modes
                launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                launcher2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                //Set Launcher Motor Directions
                launcher1.setDirection(DcMotorSimple.Direction.REVERSE);
                launcher2.setDirection(DcMotorSimple.Direction.FORWARD);

                //Set Launcher Motor Powers
                launcher1.setPower(0.0);
                launcher2.setPower(0.0);

            }

            /** Grabber Motor Initialization */
            if (grabberTrue) {

                //Get Grabber Motor
                grabber = ahwMap.dcMotor.get("grabber");

                //Reset Grabber Motor Encoder
                grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                //Set Grabber Motor Mode
                grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                //Set Grabber Motor Direction
                grabber.setDirection(DcMotorSimple.Direction.FORWARD);

                //Set Grabber Motor Power
                grabber.setPower(0.0);

            }

            /** Conveyor Motor Initialization */
            if (conveyorTrue) {

                //Get Conveyor Motor
                conveyor = ahwMap.dcMotor.get("conveyor");

                //Reset Conveyor Motor Encoder
                conveyor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                //Set Conveyor Motor Mode
                conveyor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                //Set Conveyor Motor Direction
                conveyor.setDirection(DcMotorSimple.Direction.FORWARD);

                //Set Conveyor Motor Power
                conveyor.setPower(0.0);

            }

            /** IMU Initialization */
            if (imuTrue) {

                //Create and Get Internal Measurement Unit Parameters
                BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
                parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
                parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
                parameters.loggingEnabled = true;
                parameters.mode = BNO055IMU.SensorMode.IMU;
                parameters.loggingTag = "IMU";
                parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

                //Get Internal Measurement Unit
                imu = ahwMap.get(BNO055IMU.class, "imu");

                //Initialize Internal Measurement Unit with Parameters
                imu.initialize(parameters);
            }

            /** Distance Sensor Initialization */
            if (distanceTrue) {

                //Get Distance Sensor
                distanceSide = ahwMap.get(DistanceSensor.class, "distance_side");
                distanceFront = ahwMap.get(DistanceSensor.class, "distance_front");

            }

        }

        /** VUFORIA & TENSORFLOW */
        if(visionTrue && visionActive){

            /**Both*/

            //Get Webcam
            webcam = ahwMap.get(WebcamName.class, "webcam");

            //Retrieve and compile VuforiaLocalizer parameters
            parameters = new VuforiaLocalizer.Parameters(ahwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", ahwMap.appContext.getPackageName()));
            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = webcam;
            parameters.useExtendedTracking = false;

            //Apply parameters to Vuforia object
            vuforia = ClassFactory.getInstance().createVuforia(parameters);

            /**Vuforia Only*/

            //Load trackable Vuforia images
            targetsUltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");
            VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
            blueTowerGoalTarget.setName("Blue Tower Goal Target");
            VuforiaTrackable redTowerGoalTarget = targetsUltimateGoal.get(1);
            redTowerGoalTarget.setName("Red Tower Goal Target");
            VuforiaTrackable redAllianceTarget = targetsUltimateGoal.get(2);
            redAllianceTarget.setName("Red Alliance Target");
            VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
            blueAllianceTarget.setName("Blue Alliance Target");
            VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
            frontWallTarget.setName("Front Wall Target");

            //Consolidate all trackable objects into one object
            allTrackables = new ArrayList<VuforiaTrackable>();
            allTrackables.addAll(targetsUltimateGoal);

            //Set perimeter target positions (from field origin)
            redAllianceTarget.setLocation(OpenGLMatrix.translation(0, -halfField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));
            blueAllianceTarget.setLocation(OpenGLMatrix.translation(0, halfField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
            frontWallTarget.setLocation(OpenGLMatrix.translation(-halfField, 0, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

            //Set tower goal target positions
            blueTowerGoalTarget.setLocation(OpenGLMatrix.translation(halfField, quadField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));
            redTowerGoalTarget.setLocation(OpenGLMatrix.translation(halfField, -quadField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

            //Set webcam position on robot
            robotFromCamera = OpenGLMatrix.translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT).multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, -90, 0, 0));

            //Let trackable listeners know phone location
            for (VuforiaTrackable trackable : allTrackables) {
                ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
            }

            /**TensorFlow Only*/

            //Create and set parameters for TensorFlow
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(ahwMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", ahwMap.appContext.getPackageName()));
            tfodParameters.minResultConfidence = tfodConfidence;

            //Create TensorFlow object from parameters
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

            //Load physical objects into TensorFlow object
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);

            //Set TensorFlow camera zoom
            tfod.setZoom(tfodMagnification, tfodAspectRatio);

        }

    }

    /** Algorithm for changing joystick input into mecannum values */
    double[][] meccanumDrive(double leftY, double leftX, double rightY){

        //Create 2D-array of all the values used
        double[][] output = new double[3][4];

        //Change joystick input into speed, angle, and spin
        output[1][0] = Math.sqrt((leftY * leftY) + (leftX * leftX));
        output[1][1] = Math.atan2(leftX, -leftY);
        output[1][2] = -rightY / 2;

        //Change speed, angle, and spin into power levels for the four drive motors
        output[2][0] = output[1][0] * Math.sin(output[1][1] + (Math.PI / 4)) + output[1][2];
        output[2][1] = output[1][0] * Math.cos(output[1][1] + (Math.PI / 4)) - output[1][2];
        output[2][2] = output[1][0] * Math.cos(output[1][1] + (Math.PI / 4)) + output[1][2];
        output[2][3] = output[1][0] * Math.sin(output[1][1] + (Math.PI / 4)) - output[1][2];

        //Set maxValue to the absolute value of first power level
        output[1][3] = Math.abs(output[2][0]);

        //If the absolute value of the second power level is less than the maxValue, make it the new maxValue
        if (Math.abs(output[2][1]) > output[1][3]) output[1][3] = Math.abs(output[2][1]);

        //If the absolute value of the third power level is less than the maxValue, make it the new maxValue
        if (Math.abs(output[2][2]) > output[1][3]) output[1][3] = Math.abs(output[2][2]);

        //If the absolute value of the fourth power level is less than the maxValue, make it the new maxValue
        if (Math.abs(output[2][3]) > output[1][3]) output[1][3] = Math.abs(output[2][3]);

        //Check if need to scale -- if not set maxValue to 1 to nullify scaling
        if (output[1][3] < 1) output[1][3] = 1;

        //Apply the scale to the outputs for each wheel (final values)
        output[0][0] = output[2][0] / output[1][3];
        output[0][1] = output[2][1] / output[1][3];
        output[0][2] = output[2][2] / output[1][3];
        output[0][3] = output[2][3] / output[1][3];

        //Output all of the variables and values created (usable wheel power in [0])
        return output;

    }

}


