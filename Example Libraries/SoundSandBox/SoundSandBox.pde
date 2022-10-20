/*
  Music Sequencer for Kinect V2 + Projector

  This Processing program matches the data from a Kinect V2 sensor to a projector.
  When circular obejct of a particular height are detected (indicated by a white bar scanning
  across the interactive/projected area) a sound is played.  The pitch of the sound is changed
  by moving the obejct higher or lower in the interactive area.

  Johnny Devine 2019

*/

import org.openkinect.processing.*;
import processing.sound.*;

Kinect kinect;
Scanner scanner;
SoundFile soundfile2;
SoundFile soundfile3;
SoundFile soundfile4;

int progStartTime; //used to delay event detection when starting
int lastTime; //used to move scanner across screen
int tempo = 20; //used to determine speed of scanner

//These are the depth values that defined the differences in sound/color
int layerNumber  = 4;
int lowBound = 55;
int highBound = 69;

int pixelColor;
float skip = 10;
int radius = 8;
int border = 2;
float sensitivity = 0.02;

//These are the pixels that define the portion of the Kinect image that should be mapped to the projector
int xMin = 131;
int xMax = 383;
int yMin = 147;
int yMax = 300;

//arrays to store depth data, color, individual pixel information for each dimension, whether a triggering evnet was detected, etc.
float[] bOnePrevious = new float[217088];
float[] bTwoPrevious = new float[217088];
int[] pixelColors = new int[(xMax - xMin +1)*(yMax - yMin +1)];
int[] pixelX = new int[(xMax - xMin +1)*(yMax - yMin + 1)];
int[] pixelY = new int[(xMax - xMin + 1)*(yMax - yMin + 1)];
int[] events = new int[xMax - xMin + 1];  //can i make this xMax - xMin + 1?
int[] modifiers = new int[xMax - xMin + 1];  //to adujst pitch of sounds

int event1 = 0;
int event2 = 0;
int event3 = 0;
int event4 = 0;

int lastEvent1 = 0;
int lastEvent2 = 0;
int lastEvent3 = 0;
int lastEvent4 = 0;

//defined height ranges for different sounds/colors
int boundDelta = highBound - lowBound;
int firstLayer = lowBound + boundDelta/layerNumber;
int secondLayer = lowBound + 2 * boundDelta/layerNumber;
int thirdLayer = lowBound + 3 * boundDelta/layerNumber;
int fourthLayer =lowBound + 4 * boundDelta/layerNumber;

void setup() {
  fullScreen(2);  //This sends the visual output to the second monitor, in this case the projector
  //size(512, 424);  //This is the number of pixels returend by the kinect by default
  kinect = new Kinect(this);
  scanner = new Scanner();

  kinect.initDepth();
  kinect.initVideo();

  lastTime = millis();
  progStartTime = millis();

  //Images and sounds used by the program need to be in the same folder or a sub folder named 'data'
  soundfile2 = new SoundFile(this, "harp-short-12.wav");
  //soundfile3 = new SoundFile(this, "harp-short-11.wav");
  soundfile3 = new SoundFile(this, "harp-short-10.wav");
  soundfile4 = new SoundFile(this, "chimes-d-3-f-1.wav");

}


void draw() {
  background(0);

  //Have the Kinect look at what is in front of it
  //Then define that depth data as a 'brightness' at each pixel
  //Based on the brightness of the pixel, draw a rectangle at that location with the appropriate color
  PImage img = kinect.getVideoImage();
  image(img,0,0);  //enable to help align projector
  int tempIndex = 0;
  for (int y = yMin; y < yMax; y += 1){
   for (int x = xMin; x < xMax; x += 1) {
    int index = x + y * img.width;
    float b = brightness(img.pixels[index]);

    //calculate the average of the last three b values.  Slows things down, but makes for less random flashing
    b = (b + bOnePrevious[index] + bTwoPrevious[index])/3;
    bTwoPrevious[index] = bOnePrevious[index];
    bOnePrevious[index] = b;



    pixelColors = colorHandler(b, tempIndex, pixelColors);
    pixelX[tempIndex] = x - xMin;  //subracting min values makes these start at zero
    pixelY[tempIndex] = y - yMin;

    noStroke();
    rect(map(x,xMin,xMax,1920,0),map(y,yMin,yMax,0,1080),skip,skip);
    tempIndex++;
    image(img,0,0);
   }
  }

  //Update the scanner() obejec so it moves its position across the screen.
  //The scanner is looking for trigger events at each frame and will play a sound if the conditions are met.
  //See the Scanner class for details.
  scanner.scan();
  scanner.display();
  if(millis() > 3000){
    scanner.checkEvents();
  }
  scanner.playEvents();
  scanner.clearModifiers();

}




//Determine color of rectangle based on brightness of
//depth image pixel and monitor event progress
int[] colorHandler(float b, int i, int[] pixs){

    //Make black if too far or close
    if (b >= highBound || b <= lowBound){
     fill(0);
     pixs[i] = 0;
    }

    //Closest to the camera: white snow caps
    else if (b > lowBound && b <= firstLayer) {
      fill(254, 254, 254);
      pixs[i] = 4;
    }

    //Second closest to camera: rocky highlands
    else if (b > firstLayer && b <= secondLayer) {
      //fill(204,102,0);
      fill(255,0,0);
      pixs[i] = 3;
    }

    //Thirdy closest to the camera: green forest
    else if (b > secondLayer && b <= thirdLayer) {
      //fill(100,100,0);
      fill(0,255,0);
      pixs[i] = 2;
    }
    //Fourth closest to the camera: blue ocean
    else if (b > thirdLayer && b <= fourthLayer) {
      fill(0,0,255);
      pixs[i] = 1;
    }

    return pixs;
}


//Used to calibrate position between kinect image and projection
void keyPressed(){
  //lowBound adjusts the boudary closer to the kinect.
  //Increasing moves the boundary further from the camera.
  if(key == 'q'){
    lowBound += 1;
    println("lowBound = " + lowBound);
  }
  else if(key == 'a'){
    lowBound -= 1;
    println("lowBound = " + lowBound);
  }
  //highBound adjusts the boundary farther away from the kinect.
  //Increasing makes the boundary further away.
  if(key == 'w'){
    highBound += 1;
    println("highBound = " + highBound);
  }
  else if(key == 's'){
    highBound -= 1;
    println("highBound = " + highBound);
  }

  else if(key == 'e'){
    xMax += 1;
    println("xMax = " + xMax);
  }
  else if(key == 'd'){
    xMax -= 1;
    println("xMax = " + xMax);
  }
  else if(key == 'r'){
    xMin += 1;
    println("xMin = " + xMin);
  }
  else if(key == 'f'){
    xMin -= 1;
    println("xMin = " + xMin);
  }
  else if(key == 't'){
    yMax += 1;
    println("yMax = " + yMax);
  }
  else if(key == 'g'){
    yMax -= 1;
    println("yMax = " + yMax);
  }
  else if(key == 'y'){
    yMin += 1;
    println("yMin = " + yMin);
  }
  else if(key == 'h'){
    yMin -= 1;
    println("yMin = " + yMin);
  }
}
