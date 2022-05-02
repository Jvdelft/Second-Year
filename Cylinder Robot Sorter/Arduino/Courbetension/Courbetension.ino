#include <L298N.h>

//pin definition
#define EN1 5
#define IN1 12
#define IN2 7
#define EN2 6
#define IN3 4
#define IN4 8
//create a motor instance
L298N motor1(EN1, IN1, IN2);
L298N motor2(EN2, IN3, IN4);
volatile int pos = 0;
volatile int tours = 0;
volatile float vit = 0;
volatile unsigned long t = 0;
volatile int apos;
volatile float vmoy = 0;

#define ENCODEURA 2
#define ENCODEURB 13

//initial speed
unsigned short theSpeed = 20;

void setup() {

  //set the initial speed
  motor1.setSpeed(theSpeed);
  motor2.setSpeed(theSpeed);
  Serial.begin(2000000);
  Serial.println("Codeur incrémental");
  pinMode(ENCODEURB, INPUT);
  attachInterrupt(0, front, CHANGE);
  t = micros();

}

void loop(){
  delay(250);
  motor1.forwardFor(3500);
  motor2.forwardFor(3500);
  unsigned long dt = micros() - t;
  int dpos = pos-apos;
  apos = pos ; //position précédente
  t = micros();
  if (dt>0) {
    vit = 1e6*(dpos)/dt *1.308997e-1; //*1.308997e-2 m/tics
  }
  //Serial.print("Position : "), Serial.print(pos), Serial.print(" tics     dpos : "), Serial.print(dpos), Serial.println(" tics");
  //Serial.print("Temps : "), Serial.print(t*1e-6), Serial.print(" s     dt : "), Serial.print(dt*1e-3), Serial.println(" ms");
  Serial.print("Vitesse : "), Serial.print(vit), Serial.println(" mm/s"), Serial.println(" ");
  if (vit > 250){
    vmoy += vit;
  }
  if (vit <1){
    vmoy = vmoy/13;
    Serial.print("Vitesse moyenne : "), Serial.print(vmoy), Serial.println(" mm/s"), Serial.println(" ");
  }
}

void front(){
  int lectureA = digitalRead(ENCODEURA);
  int lectureB = digitalRead(ENCODEURB);
  if (lectureA == lectureB){
    ++pos; //car tourne vers l'avant ==> négatif
  }
  else {
    --pos;   
  }
}
