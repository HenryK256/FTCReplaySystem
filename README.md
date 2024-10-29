# FTCReplaySystem
Record and replay movements for competitive robotics, specifically in FIRST Tech Challenge (FTC). Made for teams 23545 Dusters and 10243 Ramblers.

Download the Recorder.java and Replayer.java and place them in the same TeamCode file that the OpModes are written in.

Starter's Guide:
---
Recorder
  1. Create a Recorder object with the necessary parameters of the 4 drive motors
     Ex:
       ```
       Recorder recorder = new Recorder(LFmotor, RFmotor, LBmotor, RBmotor);
  2. Use the run() and startTime() methods in **conjunction** with the manually controlled OpMode in the while loop contained in the CommonOpMode. The startTime() should be at the very top of the while loop and the run() should be at the very bottom. While this is running, the robot can be driven around and its movement will be recorded.
     Ex:
       ```
       while (opModeIsActive()) {
         recorder.startTime();
         manualCode();
         recorder.run();
       }
  3. Once you have finished recording, press the Share and X buttons on the gamepad1 simultaneously. The Share button will be different for each controller, but it should be       one       of the menu buttons not commonly used.
  4. The file with the data will be saved automatically, further implementation of naming the saved files is yet to be added.
---
Replayer
  1. Create a Replayer object in a similar manner to the recorder
     Ex:
       ```
       Replayer replayer = new Replayer();
  2. Similarly to Replayer, the run() method has to be looped the same way any other OpMode would but should not run simultaneously with any other OpMode.
     Ex:
       ```
       while (opModeIsActive()) {
         replayer.run();
       }
  3. The autonomous function will stop automatically after finishing the recorded sequence.

Advanced Guide:
---
Try to complete the starter's guide and ensure that all of the drive motors' encoders are functioning properly with a correct output. If the robot is jittering a little bit while driving, it's OK, this aspect needs to be ironed out.

```
Recorder.setAuxMotors(DcMotor ... motors)
```
The above method is intended to receive a list of motors that are used beyond driving purposes such as an attachment. This method will eventually be deprecated, but is now used for efficiency. Ex:
```
Recorder recorder = new Recorder(LFmotor, RFmotor, LBmotor, RBmotor);
recorder.setAuxMotors(leftLiftMotor, rightLiftMotor);
```
---
```
Recorder.setAuxServos(Servo ... servos)
```
The above method is used in the same way as adding auxillary motors, but is instead used to add Servos that are used in attachments.

---
```
Recorder.setIterationTime(double newTime)
```
The above method is used to change the amount of time that each loop runs for in milliseconds. The default is 100ms and shouldn't really need to be changed. **This only matters in experimental mode**

---
```
Replayer.setReplaySpeed(double ReplaySpeed)
```
The above method is used to change the speed of the replay. By default this value is 1, but changing it to 2 would for example make the duration of the replay half of the original. **This is an experimental feature**. If you would like to run at 2x replay speed, I would recommend making the maximum value of your drive motors to be 0.5 so they max out at 1 when replayed. Using a smaller speed as 1.5 or 1.2 should work better but needs to be tested for each robot.

---
```
Replayer.setExperimentalMode(boolean eMode)
```
This is where the magic happens. By default, experimentalMode is set to false, meaning the methods stating they are only available in experimental mode will not function. In order to enable features such as Replay Speed or iterationTime, experimentalMode must be set to true.

Notes
---
This software is still going to be heavily developed and is in its very early stages. Kinks and wrinkles need to be worked out over time that require effort and time from the developers of the project (only me right now). Take everything with a grain of salt.