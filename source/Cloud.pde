class Cloud {
  
  PImage img;
  float x;
  float y;
  float speed;
  
  Cloud(PImage img, float x, float y) {
    this.img = img;
    this.x = x;
    this.y = y;
    this.speed = random(0, 1);
  }
  
  void display() {
    image(img, x ,y);
  }
  
  void update() {
    this.x -= speed;
  }
  
  boolean isOutOfScreen() {
    //The 180 is magic. Reized 180 pixels
    return x + 180 < 0;
  }
}
