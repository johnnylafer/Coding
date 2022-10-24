//Taken from https://www.jeffreythompson.org/collision-detection/object_oriented_collision.php
//full credit goes to jeffreythompson
//I only modified parts of it to turn it into a game -Johnny

// a single Circle object, controlled by the mouse
Circle circle;

// a list of rectangles
Rectangle[] rects = new Rectangle[20];

int score,gen,i;

public int dit; //display iteration variable

int[] squares= new int[20]; //array to check if any square has already been hovered over

void setup() {
  size(600,400);
  dit = 1; //set display iteration variable to 1 (shouldn't be needed but does not work without)
  // create a new Circle with 30px radius
  circle = new Circle(0,0, 20);

  // generate rectangles in random locations
  // but snap to grid!
    generate(); //generate the grid on startup
  }


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

void draw() {
  background(0);  //i changed the background to black -johnny

  // go through all rectangles...
  for (Rectangle r : rects) {
    r.checkCollision(circle);  // check for collision
    r.display();               // and draw
  }

  // update circle’s position and draw
  circle.update();
  circle.display();
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
  void update() {
    x = mouseX;
    y = mouseY;
  }

  // draw
  void display() {
    fill(255, 175, 99, 140);
    noStroke();
    ellipse(x,y, r*2, r*2);
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
  void checkCollision(Circle c) {
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

 void display()  {
     //println(dit);
     dit = dit+1;

     if (dit > rects.length-1) { //let the display iteration variable only go up to the rect length to be able to select one
       dit=0;
     }
     else {

            //int rancol = random(0,r);
       if (dit == 1) { //check if it is the first tile
         if (hit) { //if the first tile is hit do the following
           generate();
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
          else if (hit && squares[dit] == 1) { //if the square is hit again, just keep it black (probably redundant)
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
