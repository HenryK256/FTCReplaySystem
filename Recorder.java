package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

public class Recorder extends Thread {
    private int iterationTime = 20;
    private ArrayList<Integer> timeArr = new ArrayList<>();
    private ArrayList<ArrayList> mainArr = new ArrayList<>(),
            motorPowerArr = new ArrayList<>(),
            motorPosArr = new ArrayList<>(),
            servoPosArr = new ArrayList<>();
    private ArrayList<DcMotor> motorList;
    private ArrayList<Servo> servoList;
    public Recorder(ArrayList<DcMotor> mList, ArrayList<Servo> sList) {
        motorList = mList;
        servoList = sList;

        for (DcMotor motor : motorList) {
            motorPowerArr.add(new ArrayList<Double>());
            motorPosArr.add(new ArrayList<Integer>());
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //motor.getConnectionInfo();
        }
        for (Servo servo : servoList) {
            servoPosArr.add(new ArrayList<Double>());
        }
    }
    public void run() {
        int start = (int)System.currentTimeMillis();

        for (int i = 0; i < motorList.size(); i++) {
            motorPowerArr.get(i).add(motorList.get(i).getCurrentPosition());
            motorPosArr.get(i).add(motorList.get(i).getCurrentPosition());
        }
        for (int i = 0; i < servoList.size(); i++) {
            servoPosArr.get(i).add(servoList.get(i).getPosition());
        }

        try {
            Thread.sleep(iterationTime - (System.currentTimeMillis() - start));
        }
        catch (Exception e) {
        }

        timeArr.add((int)System.currentTimeMillis() - start);
    }
    public ArrayList<ArrayList> compileMainArr() {
        try {
            while (motorPosSum(0) == motorPosSum(1) && servoPosSum(0) == servoPosSum(1)) {
                for (int i = 0; i < motorPosArr.size(); i++) {
                    motorPosArr.get(i).remove(0);
                    motorPowerArr.get(i).remove(0);
                }
                for (int i = 0; i < servoPosArr.size(); i++) {
                    servoPosArr.get(i).remove(0);
                }
                timeArr.remove(0);
            }
        } catch (Exception e) {}

        mainArr.add(motorPowerArr);
        mainArr.add(motorPosArr);
        mainArr.add(servoPosArr);
        mainArr.add(timeArr);

        return mainArr;
    }
    public int motorPosSum(int index) {
        int sum = 0;

        for (int i = 0; i < motorPosArr.size(); i++) {
            sum += (int)motorPosArr.get(i).get(index);
        }

        return sum;
    }
    public double servoPosSum(int index) {
        double sum = 0;

        for (int i = 0; i < servoPosArr.size(); i++) {
            sum += (double)servoPosArr.get(i).get(index);
        }

        return sum;
    }
}