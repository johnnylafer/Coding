/* autogenerated by Processing revision 1286 on 2022-10-22 */
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

public class hue extends PApplet {

/**
 * Hue.
 *
 * Hue is the color reflected from or transmitted through an object
 * and is typically referred to as the name of the color such as
 * red, blue, or yellow. In this example, move the cursor vertically
 * over each bar to alter its hue.
 */

int barWidth = 20;
int lastBar = -1;

 public void setup() {
  /* size commented out by preprocessor */;
  colorMode(HSB, height, height, height);
  noStroke();
  background(0);
}

 public void draw() {
  int whichBar = mouseX / barWidth;
  if (whichBar != lastBar) {
    int barX = whichBar * barWidth;
    fill(mouseY, height, height);
    rect(barX, 0, barWidth, height);
    lastBar = whichBar;
  }
}


  public void settings() { size(1920, 1080); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "hue" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
