class World {
  
  Ball b;
  Terrain t;
  TokenFactory tf;
  Score scoreboard;
  
  int numOfTokens = 20;
  Token[] tk = new Token[numOfTokens];
  
  Point currentPt;
  Point futurePt;
  Point previousPt;
  
  boolean rolling = false;
  int bias = 10;
  float previousYDifference;
  
  World(Ball _b, Terrain _t, Score _s){
    b = _b;
    t = _t;
    scoreboard = _s;
    currentPt = t.landPoints.get(int(b.x));
    previousPt = t.landPoints.get(int(b.x) - bias);
    tf = new TokenFactory();
  }
  
  void generateTokens() {
    for(int i = 0; i < tk.length; i++) {
      if(tk[i] == null || tk[i].isOutOfScreen()) {
        tk[i] = tf.generateToken();
      } 
    }
  }
  
  /**
  * Checks if ball is in contact with ground
  */
  boolean isInContact(){
    return ((currentPt.y - b.y) < b.d / 2); 
  }
  
  /**
  * Keeps ball on ground surface and
  * Updates ball velocity according to gradient
  */
  void roll(){
    b.y = currentPt.y - b.d / 2;
    b.vy = previousYDifference * 1.1 ;
    gravity = 0;
    //println("Roll");
  }
  
  /**
  * Changes direction of ball when it hits the ground
  */
  void bounce() {
    b.vy = -0.7 * b.vy;
    //println("Bounce");
  }
  
  /**
  * Makes ball falls with gravity only
  */
  void fly() {
    gravity = 0.2;
    rolling = false;
    //println("Fly");
  }
  
  /**
  * Checks if ball will bounce
  */
  boolean isFallingFast(){
    return (b.vy > 1);
  }
  
  /**
  * Actual interaction between ball and the terrain
  */
  boolean interaction(){
    //println(b);
    previousYDifference = currentPt.y - previousPt.y;
    if(!isInContact()){
        fly();
    } else if(isFallingFast()){
        bounce();
    } else if(rolling){
        roll();
    } else {
        rolling = true;
    }
    for(int i = 0; i < tk.length; i++) {
      if(tk[i] != null && isTokenConsumed(tk[i])) { 
        int index = tk[i].idx;
        float score = tk[i].gradesValues[index];
        if(tk[i].isCpp()) {
          tk[i] = tf.generateToken();
          return true;
        }
        if(tk[i].isSU()) {
          scoreboard.collected--;
          scoreboard.calculateScore();
          tk[i] = tf.generateToken();
          break;
        }
        updateScore(score);
        tk[i] = tf.generateToken();
      }
    }
    return false;
  }
  
  boolean isTokenConsumed(Token t) {
    return sqrt(sq(t.x - b.x) + sq(t.y - b.y)) <= b.d;
  }
  
  void updateScore(float score) {
    scoreboard.collected += 1;
    scoreboard.totalScore += score;
    scoreboard.calculateScore();
  }
  
}
