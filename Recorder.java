package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Recorder extends LinearOpMode {
    private DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    private int start = 0;
    private int iterationTime = 100;
    private BufferedOutputStream bos;
    private ObjectOutputStream oos;
    private ArrayList<Double> LFPowerArr = new ArrayList<>(),
            RFPowerArr = new ArrayList<>(),
            LBPowerArr = new ArrayList<>(),
            RBPowerArr = new ArrayList<>();
    private ArrayList<Integer> timeArr = new ArrayList<>(),
            LFPosArr = new ArrayList<>(),
            RFPosArr = new ArrayList<>(),
            LBPosArr = new ArrayList<>(),
            RBPosArr = new ArrayList<>();
    private ArrayList<ArrayList> mainArr = new ArrayList<>(),
        auxMotorPowerArr = new ArrayList<>(),
        auxMotorPosArr = new ArrayList<>(),
        auxServoPosArr = new ArrayList<>();
    ArrayList<DcMotor> auxMotors = new ArrayList<>();
    ArrayList<Servo> auxServos = new ArrayList<>();

    public void runOpMode() {}

    public Recorder(DcMotor LF, DcMotor RF, DcMotor LB, DcMotor RB) {
        leftFrontMotor = LF;
        rightFrontMotor = RF;
        leftBackMotor = LB;
        rightBackMotor = RB;

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setAuxMotors(DcMotor ... motors) {
        for (DcMotor motor : motors) {
            auxMotors.add(motor);
        }
    }

    public void setAuxServos(Servo ... servos) {
        for (Servo servo : servos) {
            auxServos.add(servo);
        }
    }

    public void setIterationTime(int newTime) {
        iterationTime = newTime;
    }

    public void run() {
        if (gamepad1.share && (gamepad1.x || gamepad1.b)) end();

        LFPowerArr.add(leftFrontMotor.getPower());
        RFPowerArr.add(leftFrontMotor.getPower());
        LBPowerArr.add(leftFrontMotor.getPower());
        RBPowerArr.add(leftFrontMotor.getPower());
        LFPosArr.add(leftFrontMotor.getCurrentPosition());
        RFPosArr.add(rightFrontMotor.getCurrentPosition());
        LBPosArr.add(leftBackMotor.getCurrentPosition());
        RBPosArr.add(rightBackMotor.getCurrentPosition());

        ArrayList<Double> instantAuxMotorPower = new ArrayList<>();
        ArrayList<Integer> instantAuxMotorPos = new ArrayList<>();
        ArrayList<Double> instantAuxServoPos = new ArrayList<>();

        for (DcMotor motor : auxMotors) {
            instantAuxMotorPower.add(motor.getPower());
            instantAuxMotorPos.add(motor.getCurrentPosition());
        }

        for (Servo servo : auxServos) {
            instantAuxServoPos.add(servo.getPosition());
        }

        auxMotorPowerArr.add(instantAuxMotorPower);
        auxMotorPosArr.add(instantAuxMotorPos);
        auxServoPosArr.add(instantAuxServoPos);

        timeArr.add((int)System.currentTimeMillis() - start);

        while (System.currentTimeMillis() - start < iterationTime) {}
    }
    public void startTime() {
        start = (int)System.currentTimeMillis();
    }
    private void end() {
        //String name = gamepad1.x ? "blue" : "red";
        String name = "blue";
        telemetry.addLine("Programming in [" + name + "] slot...");
        telemetry.update();
        mainArr.add(LFPowerArr);
        mainArr.add(RFPowerArr);
        mainArr.add(LBPowerArr);
        mainArr.add(RBPowerArr);
        mainArr.add(LFPosArr);
        mainArr.add(LBPosArr);
        mainArr.add(RFPosArr);
        mainArr.add(RBPosArr);
        mainArr.add(auxMotors);
        mainArr.add(auxServos);
        mainArr.add(auxMotorPowerArr);
        mainArr.add(auxMotorPosArr);
        mainArr.add(auxServoPosArr);
        mainArr.add(timeArr);

        ArrayList<Object> extraData = new ArrayList<>();
        extraData.add(iterationTime);
        extraData.add(leftFrontMotor); extraData.add(rightFrontMotor);
        extraData.add(leftBackMotor); extraData.add(rightBackMotor);
        mainArr.add(extraData);
        try {
            bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/auto_data.ser." + name));
            oos = new ObjectOutputStream(bos);
            oos.writeObject(mainArr);
            oos.close();
            requestOpModeStop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
