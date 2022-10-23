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

    for (int i = 0; i < fingers.getNumFingers(); i++) {
      PVector position = fingers.getFinger(i);
      x = position.x;
      y = position.y;
    }


  }

  // draw
  void display() {
    fill(255, 175, 99, 140);
    noStroke();
    ellipse(x,y, r*2, r*2);
  }
}
