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

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/*
 * NRG6762
 * Northwestern Regional 7 Gearheads
 * 2020-2021 Season - Ultimate Goal
 * Hardware Class
 * Written by Aiden Maraia,
 * Version: 10/13/2020
 * Feel free to make any changes and use at your disposal.
 */
public class LauncherHardware {

    //Declare Drive Motors/Activator
    public Boolean                  driveMotorsTrue     = false;
    public DcMotor                  leftFront           = null;
    public DcMotor                  rightFront          = null;
    public DcMotor                  leftBack            = null;
    public DcMotor                  rightBack           = null;

    //Declare Drive Motor Values
    public double                   ticksPerInchFront   = 60;
    public double                   ticksPerInchSide    = 70;

    //Declare Launcher Motors/Activator
    public Boolean                  launcherDriveTrue   = false;
    public DcMotor                  launcher1           = null;
    public DcMotor                  launcher2           = null;

    //Declare Grabber Motors/Activator
    public Boolean                  grabberTrue         = false;
    public DcMotor                  grabber             = null;

    //Declare Conveyor Motors/Activator
    public Boolean                  conveyorTrue        = false;
    public DcMotor                  conveyor            = null;

    //Declare Aimer Servo/Activator
    public Boolean                  aimerTrue           = false;
    public Servo                    aimer               = null;

    //Declare IMU/Activator
    public Boolean                  imuTrue             = true;
    public BNO055IMU                imu                 = null;

    //Declare Distance Sensors/Activator
    public Boolean                  distanceTrue        = true;
    public DistanceSensor           distanceScissor     = null;
    public DistanceSensor           distanceFront       = null;

    //Declare Vision Sensors/Objects/Activator
    public Boolean                  visionTrue          = true;
    public boolean                  visionActive;
    public boolean                  visionFullyActive;
    public OpenGLMatrix             lastLocation        = null;
    public VuforiaLocalizer         vuforia             = null;
    public VuforiaLocalizer.Parameters     parameters   = null;
    public VuforiaTrackables        targetsSkyStone     = null;
    public VuforiaTrackable         stoneTarget         = null;
    public OpenGLMatrix             robotFromCamera     = null;
    public WebcamName               webcam              = null;

    //Declare Vision Variables
    public float                            phoneXRotate        = 0;
    public float                            phoneYRotate        = 0;
    public float                            phoneZRotate        = 0;
    public VuforiaLocalizer.CameraDirection CAMERA_CHOICE       = BACK;
    public boolean                          PHONE_IS_PORTRAIT   = false;
    public static final float               mmPerInch           = 25.4f;
    public static final float               mmTargetHeight      = (6) * mmPerInch;
    public static final float               stoneZ              = 2.00f * mmPerInch;
    public final float             CAMERA_FORWARD_DISPLACEMENT  = 8.0f * mmPerInch;   // eg: Camera is 8 Inches in front of robot-center
    public final float             CAMERA_VERTICAL_DISPLACEMENT = 3.35f * mmPerInch;   // eg: Camera is 3.35 Inches above ground
    public final float             CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line
    private static final String VUFORIA_KEY = "Ae7R6fb/////AAABmVuwcWo6s0kbghzDBT2iWX5a9AzYV1xx4EfFgQsn4UY3YDqBjlyh27OdlzOKfBIEqk2DJBtkB/XZM8zKZqXO/fypt9AaTbfdMF+xn09VNiscSC6kufSp+cnojxzInsxa6fn2Xf435YYSI5i1k/u73PmTbGM9eiokW6Ka6MCLaqVoi14bm+c8hUnL5IFRh2oCeqi2lECV4vXFtl+ZrlvAttCOzRkOZI3lNu45uUfvfcwB0AVskV/G9S9IT4Gga6MBYShUu8ti7Ss0wjcVFbpvUkkjEKooS9i+bEAN696UmYFjOwEUF5iqh1VgAXfJqdwA5Nv0uMueO0plqtCtrWLT5J1Crh52hrYte61CjzleOA4Y";

    //Local OpMode Members
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    //Constructor
    public LauncherHardware(boolean isAutonomous, boolean isHalfField){

        visionActive = isAutonomous;
        visionFullyActive = !isHalfField;

    }

    //Initialize standard Hardware interfaces
    public void init(HardwareMap ahwMap){

        //Save reference to Hardware map
        hwMap = ahwMap;

        //Drive Motor Initialization
        if(driveMotorsTrue){

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

        //Launcher Motor Initialization
        if(launcherDriveTrue){

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

        //Grabber Motor Initialization
        if(grabberTrue){

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

        //Conveyor Motor Initialization
        if(conveyorTrue){

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

        //Internal Measurement Unit Initialization
        if(imuTrue){

            //Create and Get Internal Measurement Unit Parameters
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled       = true;
            parameters.mode                 = BNO055IMU.SensorMode.IMU;
            parameters.loggingTag           = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            //Get Internal Measurement Unit
            imu = ahwMap.get(BNO055IMU.class, "imu");

            //Initialize Internal Measurement Unit with Parameters
            imu.initialize(parameters);
        }

        //Distance Sensor Initialization
        if(distanceTrue){

            //Get Distance Sensor
            distanceScissor = ahwMap.get(DistanceSensor.class, "distance_scissor");
            distanceFront = ahwMap.get(DistanceSensor.class, "distance_front");

        }

    }

}


