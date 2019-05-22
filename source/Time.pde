class Time {

  int hour;
  int min;
  int sec;
  
  Time() {
    hour = hour();
    min = minute();
    sec = second();
  }
  
  int toSeconds() {
    return sec + min * 60 + hour * 3600;
  }
  
  int calculateDifference(Time t) {
    return Math.abs(t.toSeconds() - this.toSeconds());
  }
}  
