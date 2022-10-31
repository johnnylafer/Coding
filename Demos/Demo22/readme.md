# Demo 2.2 - Square Chaser OSC

<b> README STILL NEEDS TO BE ADJUSTED </b>
Here you can find all of the information about the second & OSC version of the second demo

### Screenshots
![alt text](/Demos/Demo21/Demo21.png)
/Screenshot needs to be replaced/

### Quick Introduction
This demo tracks your fingers and maps each of them to an ellipse, which is then checked for collisions on a random grid. The grid consists of two different square types: 19 "normal" Game squares you destroy in order to gain points and one regenerate Square you use to regenerate the playing field in order to collect new points all over again.
- Potentially this can be expanded to include a timer or something else.
- You can adjust the threshold of the tracking with the buttons ```x``` and ```y``` and turn the camera image on and off using ```c``` and ```v```

### Based on these Demos

#### [Osc-Kinect-Broadcaster](Example_Libraries/osc-kinect-broadcaster)
- In this demo you have to have both this example and the demo itself running for it to function. (Also check if the IPs correspond to the current ones in your osc settings in the demo and broadcaster)
- This handles all of the backhand tracking and just sends the Blob id,x,y,width,height over OSC.

#### [Circle_Multiple_Square_Collisions2](Example_Libraries/Circle_Multiple_Square_Collisions2)
->This is the game backend the demo is based on, including the rewritten changes
### Required libraries

#### [Osc-Kinect-Broadcaster Libraries](https://github.com/atduskgreg/FingerTracker)
[Not a library! Has a lot of dependencies though.] <br>
[Needs to be run at the same time]
- This enables the blob tracking on top of the OpenKinect framework

#### [OSC Libraries](Example_Libraries/FingerTrackerKinect)
[remember to manually adjust IPs to suit your network.] <br>
->This is the base integration we use in order to connect the Kinect to Processing.

### How to run

#### Atom
![](other_Resources/atom-logo.png)
0. (Clone github repo into a folder on your computer)
1. Load Github project folder into atom
2. Select Demo21.pde in the Demo21 folder and click into the code
3. Use the keyboard shortcut  ```ctrl+alt+b``` or ```ctrl+opt+b``` to run the code (make sure you have atom [processing](https://github.com/bleikamp/processing) installed beforehand)

- It is recommended to connect the projector and the kinect at least 30s beforehand depending on your machine or it might lag/take over your main screen
- To stop the Demo simply press ```esc``` or force quit the opened java applications

#### Processing IDE
![](other_Resources/processing-logo.svg)
0. (Clone github repo into a folder on your computer)
1. Go to File>Open and select the Demo21.pde inside the Demo21 Folder
2. Press the blue & white play button to start the Demo

- It is recommended to connect the projector and the kinect at least 30s beforehand depending on your machine or it might lag/take over your main screen
- To stop the Demo simply press ```esc``` or force quit the opened java applications/press the STOP button in the Processing IDE

### Functions & Issues

#### Issues/To-Do
- Tracking kind of jumpy
- Do something with the score, right now it just gets printed to the console.
- Implement scaling of the kinect image

#### Functions
- Jumpy tracking enables a lot of different points (multiple hands?) to be used at the same time

### Credits
- Daniel Shiffman for his OpenKinect Library and the Example
- Bleikamp for the Fingertracker Library and example
- Jeffrey Thompson for the Object Collision example
