class Scanner {
 
  //variables
  int x;
  int y;
  
  //constructor
  Scanner(){
    x = xMin;
    y = yMin;
    
  }
  
  //functions
  
  void scan(){
    if(millis() - lastTime >= tempo){
      x++;
      lastTime = millis();
    }
    if(x > xMax){
      x = xMin; 
      clearEvents();
    }
  }
  
  void display(){
    noStroke();
    fill(127,90);
    rect(map(x,xMin,xMax,1920,0),map(y,yMin,yMax,0,1080), 2*skip, (yMax-yMin)*skip);
  }
  

  
  void playEvents(){
    if(events[x - xMin] == 2){
      float rate = map(float(modifiers[x-xMin]),0.0,float(yMax-yMin),0.5,1.5);
      soundfile2.play();
      soundfile2.rate(rate);
    }

    if(events[x - xMin] == 3){
      float rate = map(float(modifiers[x-xMin]),0.0,float(yMax-yMin),0.5,1.5);
      soundfile3.play();
      soundfile3.rate(rate);
    }
    if(events[x - xMin] == 4){
      float rate = map(float(modifiers[x-xMin]),0.0,float(yMax-yMin),0.5,1.5);
      soundfile4.play();
      soundfile4.rate(rate);
      soundfile4.amp(.3);
    }    
  }
  
  void checkEvents(){
    for(int i = 0; i < (yMax - yMin); i++){  //check all pixels in that column

      if(pixelColors[x - xMin + i*(xMax-xMin)] == 2 && pixelY[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelY[x - xMin + i*(xMax-xMin)] < (yMax-yMin) - (2*radius + border) && pixelX[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelX[x - xMin + i*(xMax-xMin)] < (xMax - xMin) - (2*radius + border)){
        //put an if statement here to exlude pixels within a certain distance of already recorded events
        boolean otherEvents = false;
        //for(int a = pixelX[x - xMin + i*(xMax-xMin)] - radius; a < pixelX[x - xMin + i*(xMax-xMin)] + radius; a++){
        for(int a = -radius*2; a < radius; a++){
          //println("x position is " + pixelX[x - xMin + i*(xMax-xMin)] + " and a value is " + a);
          if(events[pixelX[x - xMin + i*(xMax-xMin)]+ a] == 2){
           otherEvents = true; 
          }
        }
        //println(otherEvents);
        if(otherEvents == false){

          float true2 = 0;
          float false2 = 0;
          //Look at every pixel in a rectangle that is two radi further in x and one radius up and down in the y
          for(int yCheck = 0; yCheck < 2*radius; yCheck++){
            for(int xCheck = 0; xCheck < 2*radius; xCheck++){
              if(pixelColors[(x - xMin + xCheck) + (i + yCheck)*(xMax-xMin)] == 2){
                true2++;
              }
              else{
                false2++;
              }
            }
          }
         float trueRatio = true2/false2;
         //if(trueRatio - sensitivity > 0.65 && trueRatio + sensitivity < 0.82){
         if(Math.abs(trueRatio - 0.785) < sensitivity){
           //println("Event added at x = " + pixelX[x - xMin + i*(xMax-xMin)] + " with a trueRatio of " + trueRatio);
           events[pixelX[x - xMin + i*(xMax-xMin)]] = 2;
           modifiers[pixelX[x - xMin + i*(xMax-xMin)]] = pixelY[x - xMin + i*(xMax-xMin)];  //maybe map yTemp value to something like 1-100
         }
        }
      }
    }
    for(int i = 0; i < (yMax - yMin); i++){  //check all pixels in that column

      if(pixelColors[x - xMin + i*(xMax-xMin)] == 3 && pixelY[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelY[x - xMin + i*(xMax-xMin)] < (yMax-yMin) - (2*radius + border) && pixelX[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelX[x - xMin + i*(xMax-xMin)] < (xMax - xMin) - (2*radius + border)){
        //put an if statement here to exlude pixels within a certain distance of already recorded events
        boolean otherEvents = false;
        for(int a = -radius*2; a < radius; a++){
          if(events[pixelX[x - xMin + (i)*(xMax-xMin)]+ a] == 3){
           otherEvents = true; 
          }
        }
        //println(otherEvents);
        if(otherEvents == false){

          float true3 = 0;
          float false3 = 0;
          //Look at every pixel in a rectangle that is two radi further in x and one radius up and down in the y
          for(int yCheck = 0; yCheck < 2*radius; yCheck++){
            for(int xCheck = 0; xCheck < 2*radius; xCheck++){
              if(pixelColors[(x - xMin + xCheck) + (i + yCheck)*(xMax-xMin)] == 3){
                true3++;
              }
              else{
                false3++;
              }
            }
          }
         float trueRatio = true3/false3;
         //if(trueRatio - sensitivity > 0.65 && trueRatio + sensitivity < 0.82){
         if(Math.abs(trueRatio - 0.785) < sensitivity){
           //println("Event added at x = " + pixelX[x - xMin + i*(xMax-xMin)] + " with a trueRatio of " + trueRatio);
           events[pixelX[x - xMin + i*(xMax-xMin)]] = 3;
           modifiers[pixelX[x - xMin + i*(xMax-xMin)]] = pixelY[x - xMin + i*(xMax-xMin)];  //maybe map yTemp value to something like 1-100
         }
        }
      }
    }
    for(int i = 0; i < (yMax - yMin); i++){  //check all pixels in that column

      if(pixelColors[x - xMin + i*(xMax-xMin)] == 4 && pixelY[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelY[x - xMin + i*(xMax-xMin)] < (yMax-yMin) - (2*radius + border) && pixelX[x - xMin + i*(xMax-xMin)] > 2*radius + border && pixelX[x - xMin + i*(xMax-xMin)] < (xMax - xMin) - (2*radius + border)){
        //put an if statement here to exlude pixels within a certain distance of already recorded events
        boolean otherEvents = false;
        for(int a = -radius*2; a < radius; a++){
          if(events[pixelX[x - xMin + (i)*(xMax-xMin)]+ a] == 4){
           otherEvents = true; 
          }
        }
        //println(otherEvents);
        if(otherEvents == false){

          float true4 = 0;
          float false4 = 0;
          //Look at every pixel in a rectangle that is two radi further in x and one radius up and down in the y
          for(int yCheck = 0; yCheck < 2*radius; yCheck++){
            for(int xCheck = 0; xCheck < 2*radius; xCheck++){
              if(pixelColors[(x - xMin + xCheck) + (i + yCheck)*(xMax-xMin)] == 4){
                true4++;
              }
              else{
                false4++;
              }
            }
          }
         float trueRatio = true4/false4;
         //if(trueRatio - sensitivity > 0.65 && trueRatio + sensitivity < 0.82){
         if(Math.abs(trueRatio - 0.785) < sensitivity){
           //println("Event added at x = " + pixelX[x - xMin + i*(xMax-xMin)] + " with a trueRatio of " + trueRatio);
           events[pixelX[x - xMin + i*(xMax-xMin)]] = 4;
           modifiers[pixelX[x - xMin + i*(xMax-xMin)]] = pixelY[x - xMin + i*(xMax-xMin)];  //maybe map yTemp value to something like 1-100
         }
        }
      }
    }
  }
  
  void clearEvents(){
   for(int i = 0; i < events.length; i++){
     events[i] = 0;
   }
  }
  void clearModifiers(){
   for(int i = 0; i < modifiers.length; i++){
     modifiers[i] = 0;
      } 
  }
    }
