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
    PVector v2 = tracker.getLerpedPos();
    x = v2.x*scale;
    y = v2.y*scale;
  }

  // draw
  void display() {
    fill(255, 175, 99, 140);
    noStroke();
    ellipse(x,y, r*2, r*2);
  }
}
