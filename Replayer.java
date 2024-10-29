package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Replayer extends LinearOpMode {
    private final double FACTOR_DIVISOR = 400; // This value will probably have to be tweaked
    private double replaySpeed = 1;
    private boolean experimentalMode;
    private DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    private int index = 0, start = 0, iterationTime = 100;
    ArrayList<Double> LFPowerArr,
            RFPowerArr,
            LBPowerArr,
            RBPowerArr;
    ArrayList<Integer> timeArr,
            LFPosArr,
            RFPosArr,
            LBPosArr,
            RBPosArr;
    ArrayList<ArrayList> mainArr = new ArrayList<>(),
        auxMotorPowerArr,
        auxMotorPosArr,
        auxServoPosArr;

    ArrayList<DcMotor> auxMotors;
    ArrayList<Servo> auxServos;
    public void runOpMode() {}
    public Replayer() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/auto_data.ser." + "blue")));
            mainArr = (ArrayList) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        LFPowerArr = mainArr.get(0);
        RFPowerArr = mainArr.get(1);
        LBPowerArr = mainArr.get(2);
        RBPowerArr = mainArr.get(3);
        LFPosArr = mainArr.get(4);
        RFPosArr = mainArr.get(5);
        LBPosArr = mainArr.get(6);
        RBPosArr = mainArr.get(7);
        auxMotors = mainArr.get(8);
        auxServos = mainArr.get(9);
        auxMotorPowerArr = mainArr.get(10);
        auxMotorPosArr = mainArr.get(11);
        auxServoPosArr = mainArr.get(12);
        timeArr = mainArr.get(13);
        iterationTime = (int)mainArr.get(14).get(0);
        leftFrontMotor = (DcMotor)mainArr.get(14).get(1);
        rightFrontMotor = (DcMotor)mainArr.get(14).get(2);
        leftBackMotor = (DcMotor)mainArr.get(14).get(3);
        rightBackMotor = (DcMotor)mainArr.get(14).get(4);

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setTargetPosition(0);
        rightFrontMotor.setTargetPosition(0);
        leftBackMotor.setTargetPosition(0);
        rightBackMotor.setTargetPosition(0);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor motor : auxMotors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(0);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    public void setReplaySpeed(double playback) { // eMode must be true for a different replaySpeed
        replaySpeed = playback;
    }
    public void setExperimentalMode(boolean eMode) {
        experimentalMode = eMode;
    }
    public void run() {
        if (index == mainArr.get(0).size()) requestOpModeStop();

        start = (int) System.currentTimeMillis();

        double LFfactor = (LFPosArr.get(index) - leftFrontMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double RFfactor = (RFPosArr.get(index) - rightFrontMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double LBfactor = (LBPosArr.get(index) - leftBackMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double RBfactor = (RBPosArr.get(index) - rightBackMotor.getCurrentPosition()) / FACTOR_DIVISOR;

        leftFrontMotor.setPower((LFPowerArr.get(index) + LFfactor) * replaySpeed); // Questionable but may work
        rightFrontMotor.setPower((RFPowerArr.get(index) + RFfactor) * replaySpeed);
        leftBackMotor.setPower((LBPowerArr.get(index) + LBfactor) * replaySpeed);
        rightBackMotor.setPower((RBPowerArr.get(index) + RBfactor) * replaySpeed);

        leftFrontMotor.setTargetPosition(LFPosArr.get(index));
        rightFrontMotor.setTargetPosition(RFPosArr.get(index));
        leftBackMotor.setTargetPosition(LBPosArr.get(index));
        rightBackMotor.setTargetPosition(RBPosArr.get(index));

        for (int i = 0; i < auxMotors.size(); i++) {
            auxMotors.get(i).setPower((double)auxMotorPowerArr.get(i).get(index));
            auxMotors.get(i).setTargetPosition((int)auxMotorPosArr.get(i).get(index));
        }
        for (int i = 0; i < auxServos.size(); i++) {
            auxServos.get(i).setPosition((double)auxServoPosArr.get(i).get(index));
        }

        if (experimentalMode) while (System.currentTimeMillis() - start < iterationTime / replaySpeed) {}
        else while (System.currentTimeMillis() - start < timeArr.get(index)) {}

        index++;
    }
}