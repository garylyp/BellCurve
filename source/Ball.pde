  class Ball{
    
    float x = width/10;
    float y = height/2;
    float vx;
    float vy;
    float ax = 0;
    float ay = gravity;
    float d = 30;
    
    void display() {
      fill(255);
      ellipse(x, y, d, d);
    }
    
    void update() {
      vy += ay;
      y += vy; 
    }
    
    String toString() {
      //return "x = " + x +
      //        "y = " + y +
      //        "vx = " + vx +
      //        "vy = " + vy +
      //        "ax = " + ax +
      //        "ay = " + ay +
      //        "gravity = " + gravity;
      return "vy = " + vy;
    }
  }
