# Demo 2 - Average Square Chaser

Here you can find all of the information about the second demo

### Screenshots
![alt text](/Demos/Demo2/Demo2.png)
/Screenshot needs to be replaced/

### Quick Introduction
This demo tracks the average geographical point within a threshold, which is then checked for collisions on a random grid. The grid consists of two different square types: 19 "normal" Game squares you destroy in order to gain points and one regenerate Square you use to regenerate the playing field in order to collect new points all over again.
- Potentially this can be expanded to include a timer or something else.
- You can adjust the threshold of the tracking with the buttons ```UP``` and ```DOWN```

### Based on these Demos

#### [AveragePointTracking](https://github.com/shiffman/OpenKinect-for-Processing/tree/master/OpenKinect-Processing/examples/Kinect_v1/AveragePointTracking)
->This is the Example within the OpenKinect library that this demo relies on

#### [Circle_Multiple_Square_Collisions2](Example_Libraries/Circle_Multiple_Square_Collisions2)
->This is the game backend the demo is based on
### Required libraries

#### [OpenKinect](Example_Libraries/FingerTrackerKinect)
[remember to manually install .dylibs on (M1) Mac (through Homebrew) and libusbK drivers through Zadig on Windows] <br>
->This is the base integration we use in order to connect the Kinect to Processing.

### How to run

#### Atom
![](other_Resources/atom-logo.png)
0. (Clone github repo into a folder on your computer)
1. Load Github project folder into atom
2. Select Demo2.pde in the Demo2 folder and click into the code
3. Use the keyboard shortcut  ```ctrl+alt+b``` or ```ctrl+opt+b``` to run the code (make sure you have atom [processing](https://github.com/bleikamp/processing) installed beforehand)

- It is recommended to connect the projector and the kinect at least 30s beforehand depending on your machine or it might lag/take over your main screen
- To stop the Demo simply press ```esc``` or force quit the opened java applications

#### Processing IDE
![](other_Resources/processing-logo.svg)
0. (Clone github repo into a folder on your computer)
1. Go to File>Open and select the Demo2.pde inside the Demo2 Folder
2. Press the blue & white play button to start the Demo

- It is recommended to connect the projector and the kinect at least 30s beforehand depending on your machine or it might lag/take over your main screen
- To stop the Demo simply press ```esc``` or force quit the opened java applications/press the STOP button in the Processing IDE

### Functions & Issues

#### Issues/To-Do
- Tracking very inaccurate as soon as more than the hand is introduced
- Do something with the score, right now it just gets printed to the console.
- Implement proper scaling of the kinect image and game

#### Functions
- Only relies on one library
- Can be used with anything not just hands

### Credits
- Daniel Shiffman for his OpenKinect Library and the Example
- Jeffrey Thompson for the Object Collision example
