import processing.sound.*;

//MAIN
   
//Gravity of world. Why is it here instead of World class? idk. @author gary lim yan peng
float gravity = 0.2;

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

void setup() {
  //fullScreen();
  size(1500, 800);
  frameRate(framerate);
  
  //Components of the world.
  Terrain t = new Terrain(width);
  Ball b1 = new Ball();
  Score scoreboard = new Score();
  
  //Terrain generation.
  for(int i = 0; i < width; i++) {
    t.landPoints.add(new Point(i, height * 0.6));
  }
  
  //Load Clouds and resizes
  cloudImg = loadImage("Untitled.png");
  cloudImg.resize(180, 50);
  
  //Generate Clouds
  for(int i = 0; i < clouds.length; i++) {
    clouds[i] = new Cloud(cloudImg, width, random(height * 0.1, height * 0.3));
  }
  
  //Background music
  soundFile = new SoundFile(this, "Spectre.wav");
  soundFile.play();
  
  //World creation
  w1 = new World(b1, t, scoreboard);

}

void draw() {
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

void updateDisco() {
 if(isDiscoModeOn && discoTime > 0) {
   discoMode();
   discoTime--;
 } else {
    isDiscoModeOn = false;
    discoTime = 200;
    background(135,206,250);
    for(int i = 0; i < clouds.length; i++) {
      if(clouds[i].isOutOfScreen()) {
        clouds[i] = new Cloud(cloudImg, width, random(height * 0.1, height * 0.3));
      }
      clouds[i].display();
      clouds[i].update();
    }
 }
}

/**
* Introduces up to 5 tokens at a random rate.
*/
void displayTokens() {
   for (int i = 0; i < w1.tk.length; i++) {
    if(w1.tk[i] != null) {
      w1.tk[i].display();
    }
  } 
}

//TODO make a disco mode toggle FK
void discoMode() {
  background(random(0,135),random(0,206),random(0,250)); 
}

/**
* Slows down camera if booster mode has been activated before.
*/
void slowdownCamera() {
  if(camSpeed > camMin){
    camSpeed --;
  } else {
    camSpeed = camMin;
  } 
}

/**
* Booster mode, increases side scroll speed while any mouse button is held down.
*/
void mouseDragged(){
  if(camSpeed < camMax){
    camSpeed ++;
  }
}

/**
* Displays scoreboard on screen.
*/
void graphic(){
  stroke(0);
  strokeWeight(1);
  fill(0);
  textSize(20);
  text(String.format("Score : %.2f", w1.scoreboard.score), 10, 30);
  text(String.format("Collected : %.0f", w1.scoreboard.collected), 10, 60);
  text("Time : " + w1.scoreboard.calculateElapsedTime() + "s", 10, 90);
}
