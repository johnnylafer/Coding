//Taken from https://www.jeffreythompson.org/collision-detection/object_oriented_collision.php
//full credit goes to jeffreythompson
//modified to be persistent by johnnylr
//I should probably rewrite this whole thing to use arrays because right now it is a mess
//with arrays it should be easier to modify

// import the fingertracker library
import fingertracker.*;
//import OpenKinect libraries;
import org.openkinect.freenect.*;
import org.openkinect.processing.*;


// declare FignerTracker and OpenKinect objects
FingerTracker fingers;
Kinect kinect;
// set a default threshold distance:
// 625 corresponds to about 2-3 feet from the Kinect  //2-3 feet equals 0,6096 m - 0,9144 m --Johnny
int threshold = 555;


float px = 0;      // point position (move with mouse)
float py = 0;

//rectangle 1
float sx = 0;    // square position
float sy = 0;
float sw = 100;    // and dimensions
float sh = 400;

//rectangle 2
float sx2 = 100;    // square position
float sy2 = 0;
float sw2 = 100;    // and dimensions
float sh2 = 400;
//rectangle 3
float sx3 = 200;    // square position
float sy3 = 0;
float sw3 = 100;    // and dimensions
float sh3 = 400;

//rectangle 4
float sx4 = 300;    // square position
float sy4 = 0;
float sw4 = 100;    // and dimensions
float sh4 = 400;

//rectangle 5
float sx5 = 400;    // square position
float sy5 = 0;
float sw5 = 100;    // and dimensions
float sh5 = 400;

//rectangle 6
float sx6 = 500;    // square position
float sy6 = 0;
float sw6 = 100;    // and dimensions
float sh6 = 400;

int h1,h2,h3,h4,h5,h6; //persistence variables --Johnny

int r11,g11,b11,r12,g12,b12; //color variables rectangle 1

//add an enabler or disabler for the image
boolean showimg;

float scalex; //float to scale the image on width
float scaley; //float to scale the image on width

void setup() {
  scalex = 3; //3 for 1920
  scaley = 2.25;
  //size(640, 480);
  fullScreen(2);
  noCursor();

  strokeWeight(5);    // thicker stroke = easier to see

  kinect = new Kinect(this);
  kinect.initDepth();
  // initialize the FingerTracker object
  // with the height and width of the Kinect
  // depth image
  fingers = new FingerTracker(this, 640, 480);

  // the "melt factor" smooths out the contour
  // making the finger tracking more robust
  // especially at short distances
  // farther away you may want a lower number
  fingers.setMeltFactor(80);
}

void random_bar_colors() { //this is where I will try to set up a random color generator
  //THIS IS STILL HEAVILY WIP
  //ONLY ADDED THE COLORS HERE FOR NOW
  r11=255;
  g11=150;
  b11=0;
  r12=0;
  g12=155;
  b12=255;

}
void draw() {
  background(255);

  // get new depth data from the kinect
  //kinect.update();
  // get a depth image and display it
  PImage depthImage = kinect.getDepthImage();
  if (showimg = true){ //if the showimg is set to true
  image(depthImage, 0, 0, kinect.width * scalex, kinect.height*scaley);
  }


  // update the depth threshold beyond which
  // we'll look for fingers
  fingers.setThreshold(threshold);

  // access the "depth map" from the Kinect
  // this is an array of ints with the full-resolution
  // depth data (i.e. 500-2047 instead of 0-255)
  // pass that data to our FingerTracker
  int[] depthMap = kinect.getRawDepth();
  fingers.update(depthMap);

  // iterate over all the contours found
  // and display each of them with a green line
  stroke(0,255,0);
  for (int k = 0; k < fingers.getNumContours(); k++) {
    fingers.drawContour(k);
  }

  // iterate over all the fingers found
  // and draw them as a red circle
  noStroke();
  fill(255,0,0);
  for (int i = 0; i < fingers.getNumFingers(); i++) {
    PVector position = fingers.getFinger(i);
    ellipse(position.x*scalex - 5, position.y*scaley -5, 10, 10);
  }


// update point to mouse coordinates
//for (int i = 0; i < fingers.getNumFingers(); i++) {
  PVector position = fingers.getFinger(3);
  px = position.x-5*scalex;
  py = position.y-5*scalex;
//}
  // check for collision
  // if hit, change rectangle color
  boolean hit = pointRect(px,py, sx,sy,sw,sh);
  if (hit) {
    h1=1;

    //fill(255,150,0);
  }
  else {
    //h6=0;
    //fill(0,150,255);
  }
  if (h1==1) {
    fill(255,150,0);
  }
  else {
    fill(0,150,255);
  }
  noStroke();
    rect(sx,sy,sw,sh);
//do the same for the rectangle 2 -johnny
    boolean hit2 = pointRect(px,py, sx2,sy2,sw2,sh2);
    if (hit2) {
      h2=1;
      //fill(255,150,0);
    }
    else {
      //h6=0;
      //fill(0,150,255);
    }
    if (h2==1) {
      fill(255,150,0);
    }
    else {
      fill(0,150,255);
    }
    noStroke();
      rect(sx2,sy2,sw2,sh2);
//same for rectangle 3 -johnny
        boolean hit3 = pointRect(px,py, sx3,sy3,sw3,sh3);
        if (hit3) {
          h3=1;
          //fill(255,150,0);
        }
        else {
          //h6=0;
          //fill(0,150,255);
        }
        if (h3==1) {
          fill(255,150,0);
        }
        else {
          fill(0,150,255);
        }
        noStroke();
          rect(sx3,sy3,sw3,sh3);

    //same for rectangle 4 -johnny
        boolean hit4 = pointRect(px,py, sx4,sy4,sw4,sh4);
        if (hit4) {
          h4=1;
          //fill(255,150,0);
        }
        else {
          //h6=0;
          //fill(0,150,255);
        }
        if (h4==1) {
          fill(255,150,0);
        }
        else {
          fill(0,150,255);
        }
        noStroke();
          rect(sx4,sy4,sw4,sh4);

        //same for rectangle 5 -johnny
        boolean hit5 = pointRect(px,py, sx5,sy5,sw5,sh5);
        if (hit5) {
          h5=1;
          //fill(255,150,0);
        }
        else {
          //h6=0;
          //fill(0,150,255);
        }
        if (h5==1) {
          fill(255,150,0);
        }
        else {
          fill(0,150,255);
        }
        noStroke();
          rect(sx5,sy5,sw5,sh5);

            //same for rectangle 6 -johnny
        boolean hit6 = pointRect(px,py, sx6,sy6,sw6,sh6);
  if (hit6) {
    h6=1;
    //fill(255,150,0);
  }
  else {
    //h6=0;
    //fill(0,150,255);
  }
  if (h6==1) {
    fill(255,150,0);
  }
  else {
    fill(0,150,255);
  }
  noStroke();
    rect(sx6,sy6,sw6,sh6);

if (h1+h2+h3+h4+h5+h6==6) { //reset the screen to be able to fill the bars again
  h1 = 0;
  h2 = 0;
  h3 = 0;
  h4 = 0;
  h5 = 0;
  h6 = 0;
}


  // draw the point
  stroke(0);
  ellipse(px*scalex,py*scaley,50,50);
}


// POINT/RECTANGLE
boolean pointRect(float px, float py, float rx, float ry, float rw, float rh) {

  // is the point inside the rectangle's bounds?
  if (px >= rx &&        // right of the left edge AND
      px <= rx + rw &&   // left of the right edge AND
      py >= ry &&        // below the top AND
      py <= ry + rh) {   // above the bottom
        return true;
  }
  return false;
}
