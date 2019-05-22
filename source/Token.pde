class Token {
  
  float x;
  float y;
  float speed;
  float w = 30;
  float h = 30;
  int idx;
  
  String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C++", "C+", "C", "D+", "D", "F", "SU"};
  float[] gradesValues = {5.0, 5.0, 4.5, 4.0, 3.5, 3.0, 0, 2.5, 2.0, 1.5, 1.0, 0, 0};
  
  Token(float x, float y, float speed) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.idx = int(random(0,grades.length)); 
    //this.idx = 6;
  }
  
  void display() {
    fill(0);
    text(grades[idx], x, y);
    updateToken();
  }
  
  void updateToken() {
    x -= speed;
  }
  
  boolean isOutOfScreen() {
    return x + w < 0;
  }
  
  boolean isCpp() {
     return idx == 6; 
  }
  
  boolean isSU() {
     return idx == 12; 
  }
  
  
}
