// Daniel Shiffman
// Tracking the average location beyond a given depth threshold
// Thanks to Dan O'Sullivan

// https://github.com/shiffman/OpenKinect-for-Processing
// http://shiffman.net/p5/kinect/

import org.openkinect.freenect.*;
import org.openkinect.processing.*;

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


void setup() {
  fullScreen(2);
  //size(640, 520);
  kinect = new Kinect(this);
  tracker = new KinectTracker();
  scale=2.4;

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
void generate(){
  for (i=0; i<rects.length; i++) {
    //println(i);
    float x = int(random(50,width-50)/50) * 50;
    float y = int(random(50,height-50)/50) * 50;
    rects[i] = new Rectangle(x,y, 50,50);
     //I moved this from setup and added a delay in order to generate more squares as the game progresses - Johnny
  //Rectangle.circleRect = false;
}
}
//Game related code END

void draw() {
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

  // update circle’s position and draw
  circle.update();
  circle.display();
  //Game related code END


  // Display some info
  int t = tracker.getThreshold();
  fill(0);
  text("threshold: " + t + "    " +  "framerate: " + int(frameRate) + "    " +
    "UP increase threshold, DOWN decrease threshold", 10, 500);
}

//Game related code START
// CIRCLE/RECTANGLE
boolean circleRect(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {

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
void keyPressed() {
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
