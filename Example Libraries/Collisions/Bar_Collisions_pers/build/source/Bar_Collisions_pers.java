/* autogenerated by Processing revision 1286 on 2022-10-26 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Bar_Collisions_pers extends PApplet {

//Taken from https://www.jeffreythompson.org/collision-detection/object_oriented_collision.php
//full credit goes to jeffreythompson
//modified to be persistent by johnnylr
//I should probably rewrite this whole thing to use arrays because right now it is a mess
//with arrays it should be easier to modify

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

 public void setup() {
  /* size commented out by preprocessor */;
  noCursor();

  strokeWeight(5);    // thicker stroke = easier to see
}

 public void random_bar_colors() { //this is where I will try to set up a random color generator
  //THIS IS STILL HEAVILY WIP
  //ONLY ADDED THE COLORS HERE FOR NOW
  r11=255;
  g11=150;
  b11=0;
  r12=0;
  g12=155;
  b12=255;

}
 public void draw() {
  background(255);

  // update point to mouse coordinates
  px = mouseX;
  py = mouseY;

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
  point(px,py);
}


// POINT/RECTANGLE
 public boolean pointRect(float px, float py, float rx, float ry, float rw, float rh) {

  // is the point inside the rectangle's bounds?
  if (px >= rx &&        // right of the left edge AND
      px <= rx + rw &&   // left of the right edge AND
      py >= ry &&        // below the top AND
      py <= ry + rh) {   // above the bottom
        return true;
  }
  return false;
}


  public void settings() { size(600, 400); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Bar_Collisions_pers" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
