#include <L298N.h>

//pin definition
#define EN1 9
#define IN1 12
#define IN2 7
#define EN2 10
#define IN3 4
#define IN4 8
//create a motor instance
L298N motor1(EN1, IN1, IN2);
L298N motor2(EN2, IN3, IN4);

#define sensor A0 // Sharp IR GP2Y0A41SK0F (4-30cm, analog)

volatile unsigned long t = 0;
volatile unsigned long tf = 0;
volatile unsigned long dt = 0;
volatile unsigned short theSpeed = 50;
volatile unsigned long dt1 = 0;
volatile unsigned long dt2 = 0;
volatile unsigned long dt3 = 0;
volatile unsigned long compte_cylindre = 0;

void setup() {
  Serial.begin(9600); // start the serial port
  motor1.setSpeed(30);
  motor2.setSpeed(30);
}

void loop() {
  delay(50);
  motor1.forwardFor(5000, callback);
  motor2.forwardFor(5000, callback);
  
  // 
  //Serial.println(analogRead(sensor));
  float volts = analogRead(sensor)*0.0048828125;  // value from sensor * (5/1024)0.0048828125
  //Serial.println(volts);                                                                                                   
  int distance = 13*pow(volts, -1); // worked out from datasheet graph
  // slow down serial port 
  
  if (distance <= 18 and distance >= 8){
    dt = 0;
    if (t==0){
      t = micros();
     }
    Serial.println(distance) ; 
    tf = micros();
    }
  else{
    
    if (dt == 0){
      dt = tf-t;
      t = 0;
      if (dt*0.000001>=1 and dt*0.000001<=10){
        Serial.println(dt*0.000001);
        if (compte_cylindre = 0){
          dt1 = dt;
          Serial.println(dt1*0.000001);
          }
        if (compte_cylindre = 1){
          dt2 = dt;
          }
        if (compte_cylindre = 2){
          dt3 = dt;
          }
        compte_cylindre += 1;
        
        }
      }
    }
  }
void callback() {

  //each time the callback function is called increase the speed of the motor or reset to 0
  if (theSpeed <= 255) {
    theSpeed = 25;
  } else {
    theSpeed = 0;
  }

  //re-enable motor movements
  motor1.reset();
  motor2.reset();
  //set the new speed
  motor1.setSpeed(theSpeed);
  motor2.setSpeed(theSpeed);
}
