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
