int ENCODEURA = 9;
int ENCODEURB = 18;

volatile int pos = 0;
volatile int tours = 0;
volatile float vit = 0;
volatile unsigned long t = 0;
volatile int apos;
volatile int atours;

void setup(){
  Serial.begin(2000000);
  Serial.println("Codeur incrémental");
  pinMode(ENCODEURB, INPUT);
  attachInterrupt(1, front, CHANGE);
  t = micros();
}

void loop(){
  unsigned long dt = micros() - t;
  int dpos = pos-apos;
//  if (tours > atours){
//   dpos += 1920;
//  }
//  else if (tours < atours){
//   dpos -= 1920;
//  }
  apos = pos ; //position précédente
  atours = tours;
  t = micros();
  if (dt>0) {
    vit = 1e6*(dpos)/dt *1.308997e-1; //*1.308997e-2 m/tics
  }
  Serial.print("Position : "), Serial.print(pos), Serial.print(" tics     dpos : "), Serial.print(dpos), Serial.print(" tics"); //     Tours : "), Serial.println(tours);
  Serial.print("Temps : "), Serial.print(t*1e-6), Serial.print(" s     dt : "), Serial.print(dt*1e-3), Serial.println(" ms");
  Serial.print("Vitesse : "), Serial.print(vit), Serial.println(" mm/s"), Serial.println(" ");
}

void front(){
  int lectureA = digitalRead(ENCODEURA);
  int lectureB = digitalRead(ENCODEURB);
  if (lectureA == lectureB){
    ++pos; //car tourne vers l'avant ==> négatif
  }
  else {
    --pos;   
//  }
//  if (pos>1919){
//    pos = 0; tours +=1;
//  }
//  else if (pos<-1919){
//    pos = 0; tours -=1;
//  }
//  else if (tours != 0){
//    if (apos > 0 and pos <= 0){ 
//  }
  }
}
