//import org.openkinect.freenect.*;
import org.openkinect.processing.*;
import processing.video.*;

Kinect kinect;

float deg;

boolean ir = false;
boolean colorDepth = false;
boolean mirror = false;

void setup() {
 //fullScreen();
  kinect = new Kinect(this);
  kinect.initDepth();
 // kinect.initVideo();
  //kinect.enableIR(ir);
  kinect.enableColorDepth(colorDepth);

  deg = kinect.getTilt();
  // kinect.tilt(deg);
}


void draw() {
  background(0);
//  image(kinect.getVideoImage(), 0, 0);

  image(kinect.getDepthImage(), 0, 0);

  fill(255);
  text(
    "Press 'i' to enable/disable between video image and IR image,  " +
    "Press 'c' to enable/disable between color depth and gray scale depth,  " +
    "Press 'm' to enable/diable mirror mode, "+
    "UP and DOWN to tilt camera   " +
    "Framerate: " + int(frameRate), 10, 515);
}

void keyPressed() {
  if (key == 'i') {
    ir = !ir;
    kinect.enableIR(ir);
  } else if (key == 'c') {
    colorDepth = !colorDepth;
    kinect.enableColorDepth(colorDepth);
  }else if(key == 'm'){
    mirror = !mirror;
    kinect.enableMirror(mirror);
  } else if (key == CODED) {
    if (keyCode == UP) {
      deg++;
    } else if (keyCode == DOWN) {
      deg--;
    }
    deg = constrain(deg, 0, 30);
    kinect.setTilt(deg);
  }
}
