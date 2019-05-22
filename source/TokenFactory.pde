class TokenFactory {
 
  Token generateToken() {
    float ran = random(2);
    if(ran > 0.02) {
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
