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

    //for (int i = 0; i < fingers.getNumFingers(); i++) {
      PVector position = fingers.getFinger(3);
      x = position.x-5; //I don't know why the -5 is here but they had it in the example and I guess it
      y = position.y-5; //makes it more accurate
    //}


  }

  // draw
  void display() {
    fill(255, 175, 99, 140);
    noStroke();
    ellipse(x,y, r*2, r*2);
  }
}
