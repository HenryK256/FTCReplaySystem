package org.firstinspires.ftc.teamcode;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Recorder extends CommonOpMode {
    private DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    private int iterationTime = 100;
    private BufferedOutputStream bos;
    private ObjectOutputStream oos;
    private ArrayList<Double> LFPowerArr = new ArrayList<>(),
        RFPowerArr = new ArrayList<>(),
        LBPowerArr = new ArrayList<>(),
        RBPowerArr = new ArrayList<>();
    private ArrayList<Integer> LFPosArr = new ArrayList<>(),
        RFPosArr = new ArrayList<>(),
        LBPosArr = new ArrayList<>(),
        RBPosArr = new ArrayList<>();
    private ArrayList<ArrayList> mainArr = new ArrayList<>();

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
