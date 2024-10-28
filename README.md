# FTCReplaySystem
Record and replay movements for competitive robotics, specifically in FIRST Tech Challenge (FTC). Made for teams 23545 and 10243

For this system to function, there are a few things to consider:

Recorder
  1. Create a Recorder object with the necessary parameters of the 4 driver motors in the order of left front, right front, left back, and right back
     Ex:
       Recorder recorder = new Recorder(LFmotor, RFmotor, LBmotor, RBmotor);
  2. Use the run() method in **conjunction** with the manually controlled OpMode in the while loop contained in the CommonOpMode. While this is running, the robot can be driven around       and its movement will be recorded.
     Ex:
       while (opModeIsActive()) {
         manualCode();
         recorder.run();
       }
  3. Once you have finished recording, press the Share and X buttons on the gamepad1 simultaneously. The Share button will be different for each controller, but it should be       one       of the menu buttons not commonly used.
  4. The file with the data will be saved automatically, and requestOpModeStop() is used to end the program.

Replayer
  1. Create a Replayer object in the same manner as the recorder
     Ex:
       Replayer replayer = new Replayer(LFmotor, RFmotor, LBmotor, RBmotor);
  2. Similarly to Replayer, the run() method has to be looped the same way any other OpMode would.
     Ex:
       while (opModeIsActive()) {
         replayer.run();
       }
  3. The autonomous function will stop automatically after finishing the sequence.
