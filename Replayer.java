package org.firstinspires.ftc.teamcode;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Replayer extends CommonOpMode {
    private final double FACTOR_DIVISOR = 400; // This value will probably have to be tweaked
    private DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    private int index = 0, start = 0;
    ArrayList<Double> LFPowerArr = new ArrayList<>(),
        RFPowerArr = new ArrayList<>(),
        LBPowerArr = new ArrayList<>(),
        RBPowerArr = new ArrayList<>();
    ArrayList<Integer> timeArr = new ArrayList<>(),
        LFPosArr = new ArrayList<>(),
        RFPosArr = new ArrayList<>(),
        LBPosArr = new ArrayList<>(),
        RBPosArr = new ArrayList<>();
    ArrayList<ArrayList> mainArr = new ArrayList<>();

    public Replayer(DcMotor LF, DcMotor RF, DcMotor LB, DcMotor RB) {
        leftFrontMotor = LF;
        rightFrontMotor = RF;
        leftBackMotor = LB;
        rightBackMotor = RB;

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
        timeArr = mainArr.get(8);
    }

    public void run() {
        if (index == mainArr.get(0).size()) requestOpModeStop();

        start = System.currentTimeMillis();

        double LFfactor = (LFPosArr.get(index) - leftFrontMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double RFfactor = (RFPosArr.get(index) - rightFrontMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double LBfactor = (LBPosArr.get(index) - leftBackMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double RBfactor = (RBPosArr.get(index) - rightBackMotor.getCurrentPosition()) / FACTOR_DIVISOR;

        leftFrontMotor.setPower(LFPowerArr.get(index) + LFfactor);
        rightFrontMotor.setPower(RFPowerArr.get(index) + RFfactor);
        leftBackMotor.setPower(LBPowerArr.get(index) + LBfactor);
        rightBackMotor.setPower(RBPowerArr.get(index) + RBfactor);

        leftFrontMotor.setTargetPosition(LFPosArr.get(index));
        rightFrontMotor.setTargetPosition(RFPosArr.get(index));
        leftBackMotor.setTargetPosition(LBPosArr.get(index));
        rightBackMotor.setTargetPosition(RBPosArr.get(index));

        while (System.currentTimeMillis() - start < timeArr.get(index)) {}

        index++;
    }
}