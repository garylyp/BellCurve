class Terrain {
  
  ArrayList<Point> landPoints;
  float size;
  //float generatorSpeed;
  float heightLimit;
  
  Terrain(int size) {
    landPoints = new ArrayList(size);
    this.size = size;
    //generatorSpeed = 4;
    heightLimit = height * 0.6;
  }
  /**
  * Assigns the height of the current point with the height of the next point 
  */
  void updateTerrain(Point p) {
    for(int i = 0; i < size - 1; i ++) {
      float nxtY = landPoints.get(i + 1).y;
      landPoints.get(i).y = nxtY;
    }
    landPoints.get(int(size - 1)).y = p.y;
  }
  
  
  /**
  * Controls the position of the new point with mouse
  */
  void updateLocY() {
    float prevY = landPoints.get(int(size - 1)).y;
    float diff = mouseY - prevY;
    float _y;
    if(diff < 0){
      _y = prevY + 0.003*diff;
    } else {
      _y = prevY + 0.007*diff;
    }
    if(_y < heightLimit) {
      _y = heightLimit;
    }
    if(_y > height) {
      _y = height; 
    }
    //landPoints.get(int(size*0.7 - 1)).y = _y;
  }
  
  /**
  * Creates a new point at the right most start point 
  */
    Point generateTerrain() {
      float prevY = landPoints.get(int(size - 1)).y;
      float diff = mouseY - prevY;
      float y;
      if(diff < 0){
        y = prevY + 0.003*diff;
      } else {
        y = prevY + 0.007*diff;
      }
      if(y < heightLimit) {
        y = heightLimit;
      }
      if(y > height) {
        y = height; 
      }
    Point p = new Point(0, y);
    return p;
  }
  
  void display() {
    for(int i = 0; i < size*1 ; i+= 10){
      Point p1 = landPoints.get(i);
      stroke(30,180,10);
      strokeWeight(5);
      line(p1.x, p1.y, p1.x, height);
    }
    
    //for(int i = 0; i < size-1; i++){
    //  Point p1 = landPoints.get(i);
    //  Point p2 = landPoints.get(i + 1);
    //  line(p1.x, p1.y, p2.x, p2.y);
    //}
  }
  
  String toString() {
    String result = "size: " + size;
    for(Point p : landPoints) {
       result += p;
       result += ",";
    }
    return result;
  }
}  
