#include <Servo.h>
Servo monServo1;
Servo monServo2;

void setup() {
  monServo1.attach(5); // ouverture
  monServo2.attach(6); // lever
}

void loop() {
  attrapeCyl(1);
  delay(5000);
}

void attrapeCyl(int tailleCyl){
  if (monServo2.read() <= 170){ //monter la pince
      monServo2.write(180);
    }
    monServo1.write(140); //ouvrir la pince
    delay(500);
    for (int position = monServo2.read(); position >=150; position--) { //descendre la pince
      monServo2.write(position);
      delay(30);
    }
    //coller-serrer le cylindre
    delay(500);
    if (tailleCyl == 1){ // taille petit
      for (int position = monServo1.read(); position >= 50; position--){ //fermer la pince
        monServo1.write(position);
        delay(20);
      }
    }
    else if (tailleCyl == 3){ // taille grand
      for (int position = monServo1.read(); position >= 70; position--){ //fermer la pince
        monServo1.write(position);
        delay(20);
      }
    }
    delay(500);
    for (int position = monServo2.read(); position <=180; position++) { //remonter la pince
      monServo2.write(position);
      delay(80); //un peu plus lent
    }
  //blockCyl(int tailleCyl);    
  }
