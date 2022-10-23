/* autogenerated by Processing revision 1286 on 2022-10-23 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import Jama.*;
import Jama.examples.*;
import Jama.test.*;
import Jama.util.*;
import KinectProjectorToolkit.*;

import javax.swing.JFrame;
import org.openkinect.freenect.*;
import org.openkinect.processing.*;
import gab.opencv.*;
import controlP5.*;
import Jama.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class CALIBRATION_fix extends PApplet {

//==========================================================
// set resolution of your projector image/second monitor
// and name of your calibration file-to-be
int pWidth = 1024;
int pHeight = 768;
//int depthWidth = 640;
//int depthHeight = 480;

String calibFilename = "calibration.txt";


//==========================================================
//==========================================================








Kinect kinect;
OpenCV opencv;
ChessboardFrame frameBoard;
ChessboardApplet ca;
PVector[] depthMap;
ArrayList<PVector> foundPoints = new ArrayList<PVector>();
ArrayList<PVector> projPoints = new ArrayList<PVector>();
ArrayList<PVector> ptsK, ptsP;
PVector testPoint, testPointP;
boolean isSearchingBoard = false;
boolean calibrated = false;
boolean testingMode = false;
int cx, cy, cwidth;

 public void setup()
{
  /* size commented out by preprocessor */;
  textFont(createFont("Courier", 24));
  frameBoard = new ChessboardFrame();

  // set up kinect
  kinect = new Kinect(this);
  //kinect.setMirror(false);
  kinect.initDepth();
  //kinect.kinect.enableIR();
  kinect.initVideo();
  //kinect.alternativeViewPointDepthToImage();
  opencv = new OpenCV(this, kinect.width, kinect.height);

  // matching pairs
  ptsK = new ArrayList<PVector>();
  ptsP = new ArrayList<PVector>();
  testPoint = new PVector();
  testPointP = new PVector();
  setupGui();
}

 public void draw()
{
  //size(1024,768);
  // draw chessboard onto scene
  projPoints = drawChessboard(cx, cy, cwidth);

  // update kinect and look for chessboard
  //kinect.update();
  //depthMap = kinect.depthMapRealWorld();
  opencv.loadImage(kinect.getVideoImage());
  //opencv.loadImage(kinect.irImage());
  opencv.gray();

  if (isSearchingBoard)
    foundPoints = opencv.findChessboardCorners(4, 3);

  drawGui();
}

 public void drawGui()
{
  background(0, 100, 0);

  // draw the RGB image
  pushMatrix();
  translate(30, 120);
  textSize(22);
  fill(255);
  //image(kinect.irImage(), 0, 0);
  image(kinect.getVideoImage(), 0, 0);

  // draw chessboard corners, if found
  if (isSearchingBoard) {
    int numFoundPoints = 0;
    for (PVector p : foundPoints) {
      if (getDepthMapAt((int)p.x, (int)p.y).z > 0) {
        fill(0, 255, 0);
        numFoundPoints += 1;
      }
      else  fill(255, 0, 0);
      ellipse(p.x, p.y, 5, 5);
    }
    if (numFoundPoints == 12)  guiAdd.show();
    else                       guiAdd.hide();
  }
  else  guiAdd.hide();
  if (calibrated && testingMode) {
    fill(255, 0, 0);
    ellipse(testPoint.x, testPoint.y, 10, 10);
  }
  popMatrix();

  // draw GUI
  pushMatrix();
  pushStyle();
  translate(kinect.width+70, 40);
  fill(0);
  rect(0, 0, 450, 680);
  fill(255);
  text(ptsP.size()+" pairs", 26, guiPos.y+525);
  popStyle();
  popMatrix();
}

 public ArrayList<PVector> drawChessboard(int x0, int y0, int cwidth) {
  ArrayList<PVector> projPoints = new ArrayList<PVector>();
  int cheight = (int)(cwidth * 0.8f);
  ca.background(255);
  ca.fill(0);
  for (int j=0; j<4; j++) {
    for (int i=0; i<5; i++) {
      int x = PApplet.parseInt(x0 + map(i, 0, 5, 0, cwidth));
      int y = PApplet.parseInt(y0 + map(j, 0, 4, 0, cheight));
      if (i>0 && j>0)  projPoints.add(new PVector((float)x/pWidth, (float)y/pHeight));
      if ((i+j)%2==0)  ca.rect(x, y, cwidth/5, cheight/4);
    }
  }
  ca.fill(0, 255, 0);
  if (calibrated)
    ca.ellipse(testPointP.x, testPointP.y, 20, 20);
  ca.redraw();
  return projPoints;
}


 public void addPointPair() {
  if (projPoints.size() == foundPoints.size()) {
    for (int i=0; i<projPoints.size(); i++) {
      ptsP.add( projPoints.get(i) );
      ptsK.add( getDepthMapAt((int) foundPoints.get(i).x, (int) foundPoints.get(i).y) );
    }
  }
  guiCalibrate.show();
  guiClear.show();
}

 public PVector getDepthMapAt(int x, int y) {
  PVector dm = depthMap[kinect.width * y + x];
  return new PVector(dm.x, dm.y, dm.z);
}

 public void clearPoints() {
  ptsP.clear();
  ptsK.clear();
  guiSave.hide();
}

 public void saveC() {
  saveCalibration(calibFilename);
}

 public void loadC() {
  println("load");
  loadCalibration(calibFilename);
  guiTesting.addItem("Testing Mode", 1);
}

 public void mousePressed() {
  if (calibrated && testingMode) {
    testPoint = new PVector(constrain(mouseX-30, 0, kinect.width-1),
                            constrain(mouseY-120, 0, kinect.height-1));
    int idx = kinect.width * (int) testPoint.y + (int) testPoint.x;
    testPointP = convertKinectToProjector(depthMap[idx]);
  }
}
Jama.Matrix A, x, y;
  
 public void calibrate() {
  A = new Jama.Matrix(ptsP.size()*2, 11);
  y = new Jama.Matrix(ptsP.size()*2, 1);
  for(int i=0; i < ptsP.size()*2; i=i+2){
    PVector kc = ptsK.get(i/2);
    PVector projC = ptsP.get(i/2);
    A.set(i, 0, kc.x);
    A.set(i, 1, kc.y);
    A.set(i, 2, kc.z);
    A.set(i, 3, 1);
    A.set(i, 4, 0);
    A.set(i, 5, 0);
    A.set(i, 6, 0);
    A.set(i, 7, 0);
    A.set(i, 8, -projC.x*kc.x);
    A.set(i, 9, -projC.x*kc.y);
    A.set(i,10, -projC.x*kc.z);
    
    y.set(i, 0, projC.x);
 
    A.set(i+1, 0, 0);
    A.set(i+1, 1, 0);
    A.set(i+1, 2, 0);
    A.set(i+1, 3, 0);
    A.set(i+1, 4, kc.x);
    A.set(i+1, 5, kc.y);
    A.set(i+1, 6, kc.z);
    A.set(i+1, 7, 1);
    A.set(i+1, 8, -projC.y*kc.x);
    A.set(i+1, 9, -projC.y*kc.y);
    A.set(i+1,10, -projC.y*kc.z);
    
    y.set(i+1, 0, projC.y);
  }
  QRDecomposition problem = new QRDecomposition(A);
  x = problem.solve(y);
  if (!calibrated) {
    calibrated = true;
    guiSave.show();
    guiTesting.addItem("Testing Mode", 1);
  }
}
 
 public PVector convertKinectToProjector(PVector kp) {
  PVector out = new PVector();
  float denom = (float)x.get(8,0)*kp.x + (float)x.get(9,0)*kp.y + (float)x.get(10,0)*kp.z + 1;
  out.x = pWidth * ((float)x.get(0,0)*kp.x + (float)x.get(1,0)*kp.y + (float)x.get(2,0)*kp.z + (float)x.get(3,0)) / denom;
  out.y = pHeight * ((float)x.get(4,0)*kp.x + (float)x.get(5,0)*kp.y + (float)x.get(6,0)*kp.z + (float)x.get(7,0)) / denom;
  return out;
}

 public String[] getCalibrationString() {
  String[] coeffs = new String[11];
  for (int i=0; i<11; i++)
    coeffs[i] = ""+x.get(i,0);
  return coeffs;
}

 public void printMatrix(Jama.Matrix M) {
  double[][] a = M.getArray();
  for (int i=0; i<a.length; i++) {
    for (int j=0; j<a[i].length; j++) {
      println(i + " " + j + " : " + a[i][j]);
    }
  }
}
ControlP5 cp5;
Slider2D guiCpos;
Slider guiCwidth;
Toggle guiSearching;
Button guiAdd, guiClear, guiCalibrate, guiSave, guiLoad;
RadioButton guiTesting;
PVector guiPos;

 public void setupGui() {
  cp5 = new ControlP5(this);
  cp5.setFont(createFont("Courier", 16));

  guiPos = new PVector(kinect.width+90, 60);
  
  guiCpos = cp5.addSlider2D("chessPosition")
      .setLabel("Chessboard Position")
      .setPosition(guiPos.x, guiPos.y+15)
      .setSize(400, 360)
      .setArrayValue(new float[]{0, 0});

  guiCwidth = cp5.addSlider("cwidth")
     .setPosition(guiPos.x, guiPos.y+420)
     .setHeight(30)
     .setWidth(345)
     .setRange(5, 800)
     .setValue(100)
     .setLabel("Size");

  guiSearching = cp5.addToggle("isSearchingBoard")
     .setPosition(guiPos.x, guiPos.y+470)
     .setSize(125, 32)
     //.setMode(ControlP5.SWITCH)
     .setLabel("Searching?");

  guiAdd = cp5.addButton("addPointPair")
     .setLabel("Add pair")
     .setPosition(guiPos.x+150, guiPos.y+540)
     .setSize(105, 32)
     .hide();

  guiClear = cp5.addButton("clearPoints")
     .setPosition(guiPos.x+300, guiPos.y+540)
     .setSize(105, 32)
     .setLabel("Clear")
     .hide();

  guiCalibrate = cp5.addButton("calibrate")
     .setPosition(guiPos.x, guiPos.y+600)
     .setSize(105, 32)
     .hide();

  guiSave = cp5.addButton("saveC")
     .setPosition(guiPos.x+150, guiPos.y+600)
     .setSize(105, 32)
     .setLabel("Save")
     .hide();

  guiLoad = cp5.addButton("loadC")
     .setPosition(guiPos.x+300, guiPos.y+600)
     .setSize(105, 32)
     .setLabel("Load");

  guiTesting = cp5.addRadioButton("mode")
      .setPosition(35, 30)
      .setSize(80, 50)
      .setItemsPerRow(2)
      .setSpacingColumn(250)
      .addItem("Calibration Mode", 0)
      .activate(0);
}

 public void controlEvent(ControlEvent theControlEvent) {
  try {
    if (theControlEvent.isFrom("chessPosition")) {
      cx = (int) map(guiCpos.getArrayValue()[0], 0, 100, 0, pWidth);
      cy = (int) map(guiCpos.getArrayValue()[1], 0, 100, 0, pHeight);
    }
  } catch(Exception e) {
    println(e);
  }
  if (theControlEvent.isFrom("mode")) {
    if (theControlEvent.getValue() == 1.0f) {
      testingMode = true;
    }
  }
}
public class ChessboardFrame extends JFrame {
  public ChessboardFrame() {
    ca = new ChessboardApplet();
    String[] args = {"Chessboard"};
    PApplet.runSketch(args,ca);
    
    removeNotify(); 
    setUndecorated(true); 
    setAlwaysOnTop(false); 
    setResizable(false);  
    addNotify();     
    show();
  }
}

public class ChessboardApplet extends PApplet {
  public void setup() {
    noLoop();
  }
  //@ADD START
  public void settings() {
    fullScreen(2);
  }
  public void draw() {
    int cheight = (int)(cwidth * 0.8f);
    background(255);
    fill(0);
    for (int j=0; j<4; j++) {
      for (int i=0; i<5; i++) {
        int x = PApplet.parseInt(cx + map(i, 0, 5, 0, cwidth));
        int y = PApplet.parseInt(cy + map(j, 0, 4, 0, cheight));
        
        if (i>0 && j>0)  projPoints.add(new PVector((float)x/pWidth, (float)y/pHeight));
        if ((i+j)%2==0)  rect(x, y, cwidth/5, cheight/4);
      }
    }  
    fill(0, 255, 0);
    if (calibrated)  
      ellipse(testPointP.x, testPointP.y, 20, 20);  
  }
}

 public void saveCalibration(String filename) {
  String[] coeffs = getCalibrationString();
  saveStrings(dataPath(filename), coeffs);
}

 public void loadCalibration(String filename) {
  String[] s = loadStrings(dataPath(filename));
  x = new Jama.Matrix(11, 1);
  for (int i=0; i<s.length; i++)
    x.set(i, 0, Float.parseFloat(s[i]));
  calibrated = true;
  println("done loading");
}


  public void settings() { size(1200, 768); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CALIBRATION_fix" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
