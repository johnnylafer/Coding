//original source: https://github.com/atduskgreg/FingerTracker
//Rewrote all of this to use the OpenKinect library instead of SimpleOpenNI -johnnylr
//please remember to install the fingertracker library first

// import the fingertracker library
import fingertracker.*;
//import OpenKinect libraries;
import org.openkinect.freenect.*;
import org.openkinect.processing.*;


// declare FignerTracker and SimpleOpenNI objects
FingerTracker fingers;
Kinect kinect;
// set a default threshold distance:
// 625 corresponds to about 2-3 feet from the Kinect  //2-3 feet equals 0,6096 m - 0,9144 m --Johnny
int threshold = 625;

//Game related code START
Circle circle;

// a list of rectangles
Rectangle[] rects = new Rectangle[20];

int score,gen,i;

public int dit; //display iteration variable

int[] squares= new int[20]; //array to check if any square has already been hovered over
//Game related code END

//add an enabler or disabler for the image
boolean showimg;

void setup() {
  size(640, 480);

  // initialize your SimpleOpenNI object
  // and set it up to access the depth image
  kinect = new Kinect(this);
  kinect.initDepth();
  // mirror the depth image so that it is more natural
 // kinect.setMirror(true);

  // initialize the FingerTracker object
  // with the height and width of the Kinect
  // depth image
  fingers = new FingerTracker(this, 640, 480);
  // the "melt factor" smooths out the contour
  // making the finger tracking more robust
  // especially at short distances
  // farther away you may want a lower number
  fingers.setMeltFactor(100);


  //Game related code START
    dit = 1; //set display iteration variable to 1 (shouldn't be needed but does not work without)
    // create a new Circle with 30px radius
    circle = new Circle(0,0, 20);

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
  // get new depth data from the kinect
  //kinect.update();
  // get a depth image and display it
  PImage depthImage = kinect.getDepthImage();
  if (showimg = true){
  image(depthImage, 0, 0);
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
    ellipse(position.x - 5, position.y -5, 10, 10);
  }


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


  // show the threshold on the screen
  fill(255,0,0);
  text(threshold, 10, 20);
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


// keyPressed event:
// pressing the '-' key lowers the threshold by 10
// pressing the '+/=' key increases it by 10
void keyPressed(){
  if(key == 'x'){
    threshold -= 10;
  }

  if(key == 'y'){
    threshold += 10;
  }

  if(key == 'c'){
    showimg = true;
  }

  if(key == 'v'){
    showimg = false;
  }
}