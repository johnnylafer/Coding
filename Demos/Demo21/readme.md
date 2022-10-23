# Demo 2.1 -

Here you can find all of the information about the second version of the second demo

### Screenshots
![alt text](/Demos/Demo21/Demo21.png)
/Screenshot needs to be replaced/

### Quick Introduction
This demo tracks your fingers and maps each of them to an ellipse, which is then checked for collisions on a random grid. The grid consists of two different square types: 19 "normal" Game squares you destroy in order to gain points and one regenerate Square you use to regenerate the playing field in order to collect new points all over again.
Potentially this can be expanded to include a timer or something else.
### Based on these Demos

#### [FingerTrackerKinect](Example_Libraries/FingerTrackerKinect)
->This is the tracking mechanism this demo uses

#### [Circle_Multiple_Square_Collisions2](Example_Libraries/Circle_Multiple_Square_Collisions2)
->This is the game backend the demo is based on
### Required libraries

#### [FingerTracker](https://github.com/atduskgreg/FingerTracker)
[needs to be installed maually!] <br>
->This enables the finger tracking on top of the OpenKinect framework

#### [OpenKinect](Example_Libraries/FingerTrackerKinect)
[remember to manually install .dylibs on M1 Mac (through Homebrew) and libusbK drivers through Zadig on Windows ] <br>
->This is the base integration we use in order to connect the Kinect to Processing.

### How to run

#### Atom
![](other_Resources/atom-logo.svg)

#### Processing IDE
![](other_Resources/processing-logo.svg)

### Functions & Issues

#### Issues
- Tracking kind of jumpy and inaccurate
- Do something with the score, right now it just gets printed to the console.

#### Functions
- Jumpy tracking enables a lot of different points (multiple hands?) to be used at the same time

### Demos
