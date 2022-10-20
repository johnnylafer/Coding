/* autogenerated by Processing revision 1286 on 2022-10-20 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import org.openkinect.processing.*;
import processing.sound.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class SoundSandBox extends PApplet {

/*
  Music Sequencer for Kinect V2 + Projector

  This Processing program matches the data from a Kinect V2 sensor to a projector.
  When circular obejct of a particular height are detected (indicated by a white bar scanning
  across the interactive/projected area) a sound is played.  The pitch of the sound is changed
  by moving the obejct higher or lower in the interactive area.

  Johnny Devine 2019

*/




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
float sensitivity = 0.02f;

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

 public void setup() {
  /* size commented out by preprocessor */;  //This sends the visual output to the second monitor, in this case the projector
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


 public void draw() {
  background(0);

  //Have the Kinect look at what is in front of it
  //Then define that depth data as a 'brightness' at each pixel
  //Based on the brightness of the pixel, draw a rectangle at that location with the appropriate color
  PImage img = kinect.getDepthImage();
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
 public int[] colorHandler(float b, int i, int[] pixs){

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
 public void keyPressed(){
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
class Scanner {

  //variables
  int x;
  int y;

  //constructor
  Scanner(){
    x = xMin;
    y = yMin;

  }

  //functions

   public void scan(){
    if(millis() - lastTime >= tempo){
      x++;
      lastTime = millis();
    }
    if(x > xMax){
      x = xMin;
      clearEvents();
    }
  }

   public void display(){
    noStroke();
    fill(127,90);
    rect(map(x,xMin,xMax,1920,0),map(y,yMin,yMax,0,1080), 2*skip, (yMax-yMin)*skip);
  }



   public void playEvents(){
    if(events[x - xMin] == 2){
      float rate = map(PApplet.parseFloat(modifiers[x-xMin]),0.0f,PApplet.parseFloat(yMax-yMin),0.5f,1.5f);
      //soundfile2.play();
      //soundfile2.rate(rate);
    }

    if(events[x - xMin] == 3){
      float rate = map(PApplet.parseFloat(modifiers[x-xMin]),0.0f,PApplet.parseFloat(yMax-yMin),0.5f,1.5f);
      //soundfile3.play();
    //  soundfile3.rate(rate);
    }
    if(events[x - xMin] == 4){
      float rate = map(PApplet.parseFloat(modifiers[x-xMin]),0.0f,PApplet.parseFloat(yMax-yMin),0.5f,1.5f);
    //  soundfile4.play();
    //  soundfile4.rate(rate);
    //  soundfile4.amp(.3);
    }
  }

   public void checkEvents(){
    for(int i = 0; i < (yMax - yMin); i++){  //check all pixels in that column

      if(pixelColors[x - xMin + i*(xMax-xMin)] == 2 && pixelY[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelY[x - xMin + i*(xMax-xMin)] < (yMax-yMin) - (2*radius + border) && pixelX[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelX[x - xMin + i*(xMax-xMin)] < (xMax - xMin) - (2*radius + border)){
        //put an if statement here to exlude pixels within a certain distance of already recorded events
        boolean otherEvents = false;
        //for(int a = pixelX[x - xMin + i*(xMax-xMin)] - radius; a < pixelX[x - xMin + i*(xMax-xMin)] + radius; a++){
        for(int a = -radius*2; a < radius; a++){
          //println("x position is " + pixelX[x - xMin + i*(xMax-xMin)] + " and a value is " + a);
          if(events[pixelX[x - xMin + i*(xMax-xMin)]+ a] == 2){
           otherEvents = true;
          }
        }
        //println(otherEvents);
        if(otherEvents == false){

          float true2 = 0;
          float false2 = 0;
          //Look at every pixel in a rectangle that is two radi further in x and one radius up and down in the y
          for(int yCheck = 0; yCheck < 2*radius; yCheck++){
            for(int xCheck = 0; xCheck < 2*radius; xCheck++){
              if(pixelColors[(x - xMin + xCheck) + (i + yCheck)*(xMax-xMin)] == 2){
                true2++;
              }
              else{
                false2++;
              }
            }
          }
         float trueRatio = true2/false2;
         //if(trueRatio - sensitivity > 0.65 && trueRatio + sensitivity < 0.82){
         if(Math.abs(trueRatio - 0.785f) < sensitivity){
           //println("Event added at x = " + pixelX[x - xMin + i*(xMax-xMin)] + " with a trueRatio of " + trueRatio);
           events[pixelX[x - xMin + i*(xMax-xMin)]] = 2;
           modifiers[pixelX[x - xMin + i*(xMax-xMin)]] = pixelY[x - xMin + i*(xMax-xMin)];  //maybe map yTemp value to something like 1-100
         }
        }
      }
    }
    for(int i = 0; i < (yMax - yMin); i++){  //check all pixels in that column

      if(pixelColors[x - xMin + i*(xMax-xMin)] == 3 && pixelY[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelY[x - xMin + i*(xMax-xMin)] < (yMax-yMin) - (2*radius + border) && pixelX[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelX[x - xMin + i*(xMax-xMin)] < (xMax - xMin) - (2*radius + border)){
        //put an if statement here to exlude pixels within a certain distance of already recorded events
        boolean otherEvents = false;
        for(int a = -radius*2; a < radius; a++){
          if(events[pixelX[x - xMin + (i)*(xMax-xMin)]+ a] == 3){
           otherEvents = true;
          }
        }
        //println(otherEvents);
        if(otherEvents == false){

          float true3 = 0;
          float false3 = 0;
          //Look at every pixel in a rectangle that is two radi further in x and one radius up and down in the y
          for(int yCheck = 0; yCheck < 2*radius; yCheck++){
            for(int xCheck = 0; xCheck < 2*radius; xCheck++){
              if(pixelColors[(x - xMin + xCheck) + (i + yCheck)*(xMax-xMin)] == 3){
                true3++;
              }
              else{
                false3++;
              }
            }
          }
         float trueRatio = true3/false3;
         //if(trueRatio - sensitivity > 0.65 && trueRatio + sensitivity < 0.82){
         if(Math.abs(trueRatio - 0.785f) < sensitivity){
           //println("Event added at x = " + pixelX[x - xMin + i*(xMax-xMin)] + " with a trueRatio of " + trueRatio);
           events[pixelX[x - xMin + i*(xMax-xMin)]] = 3;
           modifiers[pixelX[x - xMin + i*(xMax-xMin)]] = pixelY[x - xMin + i*(xMax-xMin)];  //maybe map yTemp value to something like 1-100
         }
        }
      }
    }
    for(int i = 0; i < (yMax - yMin); i++){  //check all pixels in that column

      if(pixelColors[x - xMin + i*(xMax-xMin)] == 4 && pixelY[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelY[x - xMin + i*(xMax-xMin)] < (yMax-yMin) - (2*radius + border) && pixelX[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelX[x - xMin + i*(xMax-xMin)] < (xMax - xMin) - (2*radius + border)){
        //put an if statement here to exlude pixels within a certain distance of already recorded events
        boolean otherEvents = false;
        for(int a = -radius*2; a < radius; a++){
          if(events[pixelX[x - xMin + (i)*(xMax-xMin)]+ a] == 4){
           otherEvents = true;
          }
        }
        //println(otherEvents);
        if(otherEvents == false){

          float true4 = 0;
          float false4 = 0;
          //Look at every pixel in a rectangle that is two radi further in x and one radius up and down in the y
          for(int yCheck = 0; yCheck < 2*radius; yCheck++){
            for(int xCheck = 0; xCheck < 2*radius; xCheck++){
              if(pixelColors[(x - xMin + xCheck) + (i + yCheck)*(xMax-xMin)] == 4){
                true4++;
              }
              else{
                false4++;
              }
            }
          }
         float trueRatio = true4/false4;
         //if(trueRatio - sensitivity > 0.65 && trueRatio + sensitivity < 0.82){
         if(Math.abs(trueRatio - 0.785f) < sensitivity){
           //println("Event added at x = " + pixelX[x - xMin + i*(xMax-xMin)] + " with a trueRatio of " + trueRatio);
           events[pixelX[x - xMin + i*(xMax-xMin)]] = 4;
           modifiers[pixelX[x - xMin + i*(xMax-xMin)]] = pixelY[x - xMin + i*(xMax-xMin)];  //maybe map yTemp value to something like 1-100
         }
        }
      }
    }
  }

   public void clearEvents(){
   for(int i = 0; i < events.length; i++){
     events[i] = 0;
   }
  }
   public void clearModifiers(){
   for(int i = 0; i < modifiers.length; i++){
     modifiers[i] = 0;
      }
  }
    }


  public void settings() { fullScreen(2); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SoundSandBox" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
