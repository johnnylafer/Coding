/* autogenerated by Processing revision 1286 on 2022-10-26 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import org.openkinect.freenect.*;
import org.openkinect.processing.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Demo2 extends PApplet {

// Daniel Shiffman
// Tracking the average location beyond a given depth threshold
// Thanks to Dan O'Sullivan

// https://github.com/shiffman/OpenKinect-for-Processing
// http://shiffman.net/p5/kinect/




// The kinect stuff is happening in another class
KinectTracker tracker;
Kinect kinect;

float scale;


//Game related code START
Circle circle;

// a list of rectangles
Rectangle[] rects = new Rectangle[20];

int score,gen,i;

public int dit; //display iteration variable

int[] squares= new int[20]; //array to check if any square has already been hovered over
//Game related code END


 public void setup() {
  /* size commented out by preprocessor */;
  //size(640, 520);
  kinect = new Kinect(this);
  tracker = new KinectTracker();
  scale=2.4f;

//Game related code START
  dit = 1; //set display iteration variable to 1 (shouldn't be needed but does not work without)
  // create a new Circle with 30px radius
  circle = new Circle(0,0, 20);

  // generate rectangles in random locations
  // but snap to grid!
    generate(); //generate the grid on startup
//Game related code END

}

//Game related code START
 public void generate(){
  for (i=0; i<rects.length; i++) {
    //println(i);
    float x = PApplet.parseInt(random(50,width-50)/50) * 50;
    float y = PApplet.parseInt(random(50,height-50)/50) * 50;
    rects[i] = new Rectangle(x,y, 50,50);
     //I moved this from setup and added a delay in order to generate more squares as the game progresses - Johnny
  //Rectangle.circleRect = false;
}
}
//Game related code END

 public void draw() {
  background(255);

  // Run the tracking analysis
  tracker.track();
  // Show the image
  tracker.display();

  // Let's draw the raw location
//  PVector v1 = tracker.getPos();
//  fill(50, 100, 250, 200);
//  noStroke();
//  ellipse(v1.x*scale, v1.y*scale, 50, 50);

  // Let's draw the "lerped" location
  PVector v2 = tracker.getLerpedPos();
  fill(100, 250, 50, 200);
  noStroke();
  ellipse(v2.x*scale, v2.y*scale, 100, 100);


  //Game related code START
  // go through all rectangles...
  for (Rectangle r : rects) {
    r.checkCollision(circle);  // check for collision
    r.display();               // and draw
  }

  // update circle\u2019s position and draw
  circle.update();
  circle.display();
  //Game related code END


  // Display some info
  int t = tracker.getThreshold();
  fill(0);
  text("threshold: " + t + "    " +  "framerate: " + PApplet.parseInt(frameRate) + "    " +
    "UP increase threshold, DOWN decrease threshold", 10, 500);
}

//Game related code START
// CIRCLE/RECTANGLE
 public boolean circleRect(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {

  // temporary variables to set edges for testing
  float testX = cx;
  float testY = cy;

  // which edge is closest?
  if (cx < rx)         testX = rx;      // compare left edge
  else if (cx > rx+rw) testX = rx+rw;   // right edge
  if (cy < ry)         testY = ry;      // top edge
  else if (cy > ry+rh) testY = ry+rh;   // bottom edge

  // get distance from closest edges
  float distX = cx-testX;
  float distY = cy-testY;
  float distance = sqrt( (distX*distX) + (distY*distY) );

  // if the distance is less than the radius, collision!
  if (distance <= radius) {
    return true;
  }
  return false;
}
//Game related code END

// Adjust the threshold with key presses
 public void keyPressed() {
  int t = tracker.getThreshold();
  if (key == CODED) {
    if (keyCode == UP) {
      t+=5;
      tracker.setThreshold(t);
    } else if (keyCode == DOWN) {
      t-=5;
      tracker.setThreshold(t);
    }
  }
}
class Circle {
  float x, y;    // position
  float r;       // radius

  Circle (float _x, float _y, float _r) {
    x = _x;
    y = _y;
    r = _r;
  }

  // move into mouse position
   public void update() {
    PVector v2 = tracker.getLerpedPos();
    x = v2.x*scale;
    y = v2.y*scale;
  }

  // draw
   public void display() {
    fill(255, 175, 99, 140);
    noStroke();
    ellipse(x,y, r*2, r*2);
  }
}
// Daniel Shiffman
// Tracking the average location beyond a given depth threshold
// Thanks to Dan O'Sullivan

// https://github.com/shiffman/OpenKinect-for-Processing
// http://shiffman.net/p5/kinect/

class KinectTracker {

  // Depth threshold
  int threshold = 745;

  // Raw location
  PVector loc;

  // Interpolated location
  PVector lerpedLoc;

  // Depth data
  int[] depth;

  // What we'll show the user
  PImage display;

  KinectTracker() {
    // This is an awkard use of a global variable here
    // But doing it this way for simplicity
    kinect.initDepth();
    kinect.enableMirror(true);
    // Make a blank image
    display = createImage(kinect.width, kinect.height, RGB);
    // Set up the vectors
    loc = new PVector(0, 0);
    lerpedLoc = new PVector(0, 0);
  }

   public void track() {
    // Get the raw depth as array of integers
    depth = kinect.getRawDepth();

    // Being overly cautious here
    if (depth == null) return;

    float sumX = 0;
    float sumY = 0;
    float count = 0;

    for (int x = 0; x < kinect.width; x++) {
      for (int y = 0; y < kinect.height; y++) {

        int offset =  x + y*kinect.width;
        // Grabbing the raw depth
        int rawDepth = depth[offset];

        // Testing against threshold
        if (rawDepth < threshold) {
          sumX += x;
          sumY += y;
          count++;
        }
      }
    }
    // As long as we found something
    if (count != 0) {
      loc = new PVector(sumX/count, sumY/count);
    }

    // Interpolating the location, doing it arbitrarily for now
    lerpedLoc.x = PApplet.lerp(lerpedLoc.x, loc.x, 0.3f);
    lerpedLoc.y = PApplet.lerp(lerpedLoc.y, loc.y, 0.3f);
  }

   public PVector getLerpedPos() {
    return lerpedLoc;
  }

   public PVector getPos() {
    return loc;
  }

   public void display() {
    PImage img = kinect.getDepthImage();

    // Being overly cautious here
    if (depth == null || img == null) return;

    // Going to rewrite the depth image to show which pixels are in threshold
    // A lot of this is redundant, but this is just for demonstration purposes
    display.loadPixels();
    for (int x = 0; x < kinect.width; x++) {
      for (int y = 0; y < kinect.height; y++) {

        int offset = x + y * kinect.width;
        // Raw depth
        int rawDepth = depth[offset];
        int pix = x + y * display.width;
        if (rawDepth < threshold) {
          // A red color instead
          display.pixels[pix] = color(150, 50, 50);
        } else {
          display.pixels[pix] = img.pixels[offset];
        }
      }
    }
    display.updatePixels();

    // Draw the image
    image(display, 0, 0,kinect.width*scale,kinect.height*scale); //added the two scaling variables here --Johnny
  }

   public int getThreshold() {
    return threshold;
  }

   public void setThreshold(int t) {
    threshold =  t;
  }
}
class Rectangle {
  float x, y;            // position
  float w, h;            // size
  boolean hit = false;   // is it hit?

  Rectangle (float _x, float _y, float _w, float _h) {
    x = _x;
    y = _y;
    w = _w;
    h = _h;
  }

  // check for collision with the circle using the
  // Circle/Rect function we made in the beginning
   public void checkCollision(Circle c) {
    hit = circleRect(c.x,c.y,c.r, x,y,w,h);
  }

//  draw the rectangle
//  if hit, change the fill color
//  void display() {
//
//    if (hit) fill(0,150,150);
//    else fill(0,150,255);
//    noStroke();
//    rect(x,y, w,h);
//  }
// }

  public void display()  {
     //println(dit);
     dit = dit+1;

     if (dit > rects.length-1) { //let the display iteration variable only go up to the rect length to be able to select one
       dit=0;
     }
     else {

            //int rancol = random(0,r);
       if (dit == 1) {
         if (hit) {
           generate(); //if the different tile is hit regenerate a new playing field
            for (int cs=1;cs < squares.length;cs++) { //for loop in order to set every single value in the array back to zero
            squares[cs] = 0; //reset the array to null
            }
         }
         else { //if its not hit yet then don't generate but color it differently so you know its the end goal
           fill(255,199,100);
          rect(x,y, w,h);
          }
        }
         else if (hit && squares[dit] != 1){ //check if any other block than 1 is hit & if it is the first time
           squares[dit] = 1; //if it is the first time, write a 1 at the array position of the square
           score=score+1;
           println(score);
           //println(squares); // -
           fill(0,0,0);
           rect(x,y, w,h);
         }
          else if (hit && squares[dit] == 1) { //if the square is hit again, just keep it black (probably redundant)
            noStroke();
            fill(0,0,0);
            rect(x,y, w,h);
         }
         else if (squares[dit] == 1)  { //if the square is not hit, but was hit already, just draw it black
           fill(0,0,0);
           rect(x,y, w,h);
         }
         else { //if the square is not dit = 1, but also hasn't been hit yet, just draw the field
           fill(0,150,255);
           rect(x,y, w,h);
         }
    }

   }
}


  public void settings() { fullScreen(2); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Demo2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
