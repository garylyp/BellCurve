import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BellCurve extends PApplet {



//MAIN
   
//Gravity of world. Why is it here instead of World class? idk. @author gary lim yan peng
float gravity = 0.2f;

//World of our game.
World w1;

//Support for cpp disco lights
boolean isDiscoModeOn = false;
int discoTime = 100;

//Side-scrolling speed
int camSpeed = 10;
int camMin = 10;
int camMax = 40;

//Clouds
Cloud[] clouds = new Cloud[5];
PImage cloudImg;

//Default framerate of game
int framerate = 120;

SoundFile soundFile;

public void setup() {
  //fullScreen();
  
  frameRate(framerate);
  
  //Components of the world.
  Terrain t = new Terrain(width);
  Ball b1 = new Ball();
  Score scoreboard = new Score();
  
  //Terrain generation.
  for(int i = 0; i < width; i++) {
    t.landPoints.add(new Point(i, height * 0.6f));
  }
  
  //Load Clouds and resizes
  cloudImg = loadImage("Untitled.png");
  cloudImg.resize(180, 50);
  
  //Generate Clouds
  for(int i = 0; i < clouds.length; i++) {
    clouds[i] = new Cloud(cloudImg, width, random(height * 0.1f, height * 0.3f));
  }
  
  //Background music
  soundFile = new SoundFile(this, "Spectre.wav");
  soundFile.play();
  
  //World creation
  w1 = new World(b1, t, scoreboard);

}

public void draw() {
  updateDisco();
  stroke(0);
  graphic();
  
  //Display world
  w1.b.display();
  w1.t.display();
  w1.generateTokens();
  displayTokens();
    
  //Update world
  w1.b.update();
  for(int i = 0; i < camSpeed; i++){
    w1.t.updateTerrain(w1.t.generateTerrain());
    w1.t.updateLocY();
  }
  
  //Other necessary actions to take - manage game interaction
  slowdownCamera();
  if(w1.interaction()) {
    isDiscoModeOn = true;
  }
}

public void updateDisco() {
 if(isDiscoModeOn && discoTime > 0) {
   discoMode();
   discoTime--;
 } else {
    isDiscoModeOn = false;
    discoTime = 200;
    background(135,206,250);
    for(int i = 0; i < clouds.length; i++) {
      if(clouds[i].isOutOfScreen()) {
        clouds[i] = new Cloud(cloudImg, width, random(height * 0.1f, height * 0.3f));
      }
      clouds[i].display();
      clouds[i].update();
    }
 }
}

/**
* Introduces up to 5 tokens at a random rate.
*/
public void displayTokens() {
   for (int i = 0; i < w1.tk.length; i++) {
    if(w1.tk[i] != null) {
      w1.tk[i].display();
    }
  } 
}

//TODO make a disco mode toggle FK
public void discoMode() {
  background(random(0,135),random(0,206),random(0,250)); 
}

/**
* Slows down camera if booster mode has been activated before.
*/
public void slowdownCamera() {
  if(camSpeed > camMin){
    camSpeed --;
  } else {
    camSpeed = camMin;
  } 
}

/**
* Booster mode, increases side scroll speed while any mouse button is held down.
*/
public void mouseDragged(){
  if(camSpeed < camMax){
    camSpeed ++;
  }
}

/**
* Displays scoreboard on screen.
*/
public void graphic(){
  stroke(0);
  strokeWeight(1);
  fill(0);
  textSize(20);
  text(String.format("Score : %.2f", w1.scoreboard.score), 10, 30);
  text(String.format("Collected : %.0f", w1.scoreboard.collected), 10, 60);
  text("Time : " + w1.scoreboard.calculateElapsedTime() + "s", 10, 90);
}
  class Ball{
    
    float x = width/10;
    float y = height/2;
    float vx;
    float vy;
    float ax = 0;
    float ay = gravity;
    float d = 30;
    
    public void display() {
      fill(255);
      ellipse(x, y, d, d);
    }
    
    public void update() {
      vy += ay;
      y += vy; 
    }
    
    public String toString() {
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
  
  public void display() {
    image(img, x ,y);
  }
  
  public void update() {
    this.x -= speed;
  }
  
  public boolean isOutOfScreen() {
    //The 180 is magic. Reized 180 pixels
    return x + 180 < 0;
  }
}
class Point {
   
  float x;
  float y;
  
  Point(float x, float y) {
     this.x = x;
     this.y = y;
  }
  
  //String toString() {
  //    return "(" + x + "," + y + ")"; 
  //}
}
class Score{
  ArrayList<String> grades;
  float totalScore;
  float collected;
  float score;
  Time initialTime;
  
  Score() {
    grades = new ArrayList();
    score = 0;
    collected = 0;
    totalScore = 0;
    initialTime = new Time();
  }
  
  public int calculateElapsedTime() {
    Time curTime = new Time();
    return curTime.calculateDifference(initialTime);
  }
  
  public void newGrade(){
    grades.add(new String());
  }
  
  public void calculateScore(){
    if(collected == 0){
      score = 0;
    } else {
      score = totalScore/collected;
    }
  }

}
class Terrain {
  
  ArrayList<Point> landPoints;
  float size;
  //float generatorSpeed;
  float heightLimit;
  
  Terrain(int size) {
    landPoints = new ArrayList(size);
    this.size = size;
    //generatorSpeed = 4;
    heightLimit = height * 0.6f;
  }
  /**
  * Assigns the height of the current point with the height of the next point 
  */
  public void updateTerrain(Point p) {
    for(int i = 0; i < size - 1; i ++) {
      float nxtY = landPoints.get(i + 1).y;
      landPoints.get(i).y = nxtY;
    }
    landPoints.get(PApplet.parseInt(size - 1)).y = p.y;
  }
  
  
  /**
  * Controls the position of the new point with mouse
  */
  public void updateLocY() {
    float prevY = landPoints.get(PApplet.parseInt(size - 1)).y;
    float diff = mouseY - prevY;
    float _y;
    if(diff < 0){
      _y = prevY + 0.003f*diff;
    } else {
      _y = prevY + 0.007f*diff;
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
    public Point generateTerrain() {
      float prevY = landPoints.get(PApplet.parseInt(size - 1)).y;
      float diff = mouseY - prevY;
      float y;
      if(diff < 0){
        y = prevY + 0.003f*diff;
      } else {
        y = prevY + 0.007f*diff;
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
  
  public void display() {
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
  
  public String toString() {
    String result = "size: " + size;
    for(Point p : landPoints) {
       result += p;
       result += ",";
    }
    return result;
  }
}  
class Time {

  int hour;
  int min;
  int sec;
  
  Time() {
    hour = hour();
    min = minute();
    sec = second();
  }
  
  public int toSeconds() {
    return sec + min * 60 + hour * 3600;
  }
  
  public int calculateDifference(Time t) {
    return Math.abs(t.toSeconds() - this.toSeconds());
  }
}  

class Token {
  
  float x;
  float y;
  float speed;
  float w = 30;
  float h = 30;
  int idx;
  
  String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C++", "C+", "C", "D+", "D", "F", "SU"};
  float[] gradesValues = {5.0f, 5.0f, 4.5f, 4.0f, 3.5f, 3.0f, 0, 2.5f, 2.0f, 1.5f, 1.0f, 0, 0};
  
  Token(float x, float y, float speed) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.idx = PApplet.parseInt(random(0,grades.length)); 
    //this.idx = 6;
  }
  
  public void display() {
    fill(0);
    text(grades[idx], x, y);
    updateToken();
  }
  
  public void updateToken() {
    x -= speed;
  }
  
  public boolean isOutOfScreen() {
    return x + w < 0;
  }
  
  public boolean isCpp() {
     return idx == 6; 
  }
  
  public boolean isSU() {
     return idx == 12; 
  }
  
  
}
class TokenFactory {
 
  public Token generateToken() {
    float ran = random(2);
    if(ran > 0.02f) {
       return null;
    }
    float heightLimit = height / 2; 
    //float widthLimit = width * 0.7;
    //float x = random(widthLimit, width);
    float y = random(0, heightLimit);
    float speed = 2 + random(1, 100)/100 * 2;
    return new Token(width, y, speed); 
  }
}
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
    currentPt = t.landPoints.get(PApplet.parseInt(b.x));
    previousPt = t.landPoints.get(PApplet.parseInt(b.x) - bias);
    tf = new TokenFactory();
  }
  
  public void generateTokens() {
    for(int i = 0; i < tk.length; i++) {
      if(tk[i] == null || tk[i].isOutOfScreen()) {
        tk[i] = tf.generateToken();
      } 
    }
  }
  
  /**
  * Checks if ball is in contact with ground
  */
  public boolean isInContact(){
    return ((currentPt.y - b.y) < b.d / 2); 
  }
  
  /**
  * Keeps ball on ground surface and
  * Updates ball velocity according to gradient
  */
  public void roll(){
    b.y = currentPt.y - b.d / 2;
    b.vy = previousYDifference * 1.1f ;
    gravity = 0;
    //println("Roll");
  }
  
  /**
  * Changes direction of ball when it hits the ground
  */
  public void bounce() {
    b.vy = -0.7f * b.vy;
    //println("Bounce");
  }
  
  /**
  * Makes ball falls with gravity only
  */
  public void fly() {
    gravity = 0.2f;
    rolling = false;
    //println("Fly");
  }
  
  /**
  * Checks if ball will bounce
  */
  public boolean isFallingFast(){
    return (b.vy > 1);
  }
  
  /**
  * Actual interaction between ball and the terrain
  */
  public boolean interaction(){
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
  
  public boolean isTokenConsumed(Token t) {
    return sqrt(sq(t.x - b.x) + sq(t.y - b.y)) <= b.d;
  }
  
  public void updateScore(float score) {
    scoreboard.collected += 1;
    scoreboard.totalScore += score;
    scoreboard.calculateScore();
  }
  
}
  public void settings() {  size(1500, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "BellCurve" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
