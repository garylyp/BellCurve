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
  
  int calculateElapsedTime() {
    Time curTime = new Time();
    return curTime.calculateDifference(initialTime);
  }
  
  void newGrade(){
    grades.add(new String());
  }
  
  void calculateScore(){
    if(collected == 0){
      score = 0;
    } else {
      score = totalScore/collected;
    }
  }

}
