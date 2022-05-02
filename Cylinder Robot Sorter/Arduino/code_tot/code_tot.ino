#include <AFArray.h>
#include <AFArrayType.h>
#include <SimpleTimer.h>
#include <L298N.h>
#include <math.h>
#include <Servo.h>
#define EN1 6
#define IN1 12
#define IN2 7
#define EN2 5
#define IN3 4
#define IN4 8
#define sharpG A1
#define sharpD A0
#define encoDA 18
#define encoDB 3
#define encoGA 13
#define encoGB 2
#define servo_ferme 9
#define servo_monte 10
L298N motorD(EN1,IN1,IN2);
L298N motorG(EN2, IN3, IN4);
Servo servo_f;
Servo servo_m;
SimpleTimer timer;


//13 et 2 et A5 et 3

const int frequence_echantillonnage = 50;
const float entraxe = 0.17;
double coeff = 1.31*0.0001;
float kp_l = 50;
float ki_l = 0;
float kd_l = 0.5;
 float kp_theta = 5;
 float ki_theta = 0;
 float kd_theta = 0.05;
double x_cible;
double y_cible;
double  x0;
double  y0;
double theta0;
double theta_robot;
double delta_theta;

int tic_D = 0;
int tic_G = 0;

 float theta_rad = 0;
 float l;

float somme_erreur_theta = 0.;
float erreur_theta_old = theta_rad;
float somme_erreur_l = 0.;
float erreur_l_old = l;
double cmd_l;
double cmd_theta;
double delta_l;
double delta_x;
double delta_y;
unsigned int temps = 0;

 int pos_G = 0;
 int tours_G = 0;
 float vit_G = 0;
 unsigned long t = 0;
 int apos_G;
 int atours_G;

 int pos_D = 0;
 int tours_D = 0;
 float vit_D = 0;
 int apos_D;
 int atours_D;

 int i = 0;
 float position_c1_x;
 float position_c1_y;
 double dt_theta = 0;
 double dt_l = 0;
 short vitG = 0;
 short vitD = 0;
 double vitesse;

bool priority1;
bool priority2;

float dt;
float tf;
int  compte_cylindre = 0;
double  dt1;
double dt2;
double  dt3;
double dt4;
double dt5;
double dt6;
float  xcyl1;
float  xcyl2;
float  xcyl3;
float  xcyl4;
float  xcyl5;
float  xcyl6;
float  ycyl1;
float  ycyl2;
float  ycyl3;
float  ycyl4;
float  ycyl5;
float  ycyl6;
bool priority3 = true;
float t1 = 0;
float t2 = 0;
float last_distance;
 AFArray<float> cylindres;
 float *dt_array;
 int last_tic_G = 0;
 int last_tic_D = 0;
 AFArray<float> cylindres_2;
 double theta_evo = 0;
double l_evo = 0;
 double x_robot;
 double y_robot;
 bool tourne = false;
 bool inite = true;
 int j = 0;
 float last_theta_evo;
 float last_l_evo;
 int counter = 0;
 int counter2 = 0;
 float last_detecD = 0;
 int k = 0;
 int cylindre_transport = 0;
 int temps2 = 0;
 int m = 0;
 float last_detecG = 0;
 bool phase1 = false;
 int s = 0;
 boolean aller = true;
 bool prise = false;
 double dist_parcou;

void setup(){
  timer.setInterval(20,odometry_x);   //Les différentes fonctions gérant l'odométrie et la régulation
  timer.setInterval(20,odometry_y);   // sont appelées toutes les 20 millisecondes.
  timer.setInterval(20,odometry_theta);
  timer.setInterval(20, bloc);
  pinMode(sharpD, INPUT);         // on initialise les capteurs sharps.
  pinMode(sharpG, INPUT);     
  servo_f.attach(servo_ferme);
  servo_m.attach(servo_monte);
  pinMode(encoGA, INPUT);
  pinMode(encoDA, INPUT);
  attachInterrupt(digitalPinToInterrupt(encoGB), front_G, CHANGE); // les fonctions des encodeurs seront appelés 
  attachInterrupt(digitalPinToInterrupt(encoDB), front_D, CHANGE); // à chaque fois que la valeur de l'encodeur change.
  Serial.begin(9600);
  servo_m.write(179);
  t = millis();
  priority1 = true;
  priority2 = true;
}

void loop(){
  timer.run();    // on lance le timer pour qu'il commence à compter et appelle les fonctions au bon moment.
  advance();   
    if (priority1){
    detectionD();
      if ((millis()-t>=20) and (not phase1)){   
        phase_1();    // on commence par la phase 1 de détection puis vient la phase 2
        t += 20;      // de transport des cylindres.
      }
      else if (millis()-t>=20 and cylindre_transport == 0 and phase1){
        phase_2(cylindres_2[1],cylindres_2[2],cylindres_2[0]);
        t+=20;
      }
      else if (millis()-t>=20 and cylindre_transport == 1){
        phase_2(cylindres_2[4],cylindres_2[5],cylindres_2[3]);
        t+=20;
      }
       else if (millis()-t>=20 and cylindre_transport == 2){
        phase_2(cylindres_2[7],cylindres_2[8],cylindres_2[6]);
        t+=20;
      }
       else if (millis()-t>=20 and cylindre_transport == 3){
        phase_2(cylindres_2[10],cylindres_2[11],cylindres_2[9]);
        t+=20;
      }
    }
    else{
      motorD.stop();
      motorG.stop();
    }
}

void phase_1(){       //phase 1 de détection des cylindres où 
  if (x_robot < 1.95 and m ==0){   // le véhicule se déplace en longeant
    consigne(1.95,0);               // les cylindres. Il va donc aller au
    kp_l = 40;                      // bout du terrain et revenir.
    kd_l = 0.4;
  }
  else if (m==0){
    inite = true;
    m = 1;
    consigne(x_robot,0.14);
  }
  if (m == 1){
    if (theta_robot < theta_rad and s == 0){
      tourne = true;
    }
    else if (y_robot <= 0.14){
      s = 1;
      tourne = false;
    }
    else {
      tourne = true;
      inite = true;
      m = 2;
      consigne(0,0.14);
    }
  }
    if (m ==2){
      if ((theta_robot+0.1 < M_PI)){
    }
    else {
      m = 3;
    }
  }
  if (m==3){
    tourne = false;
  }
  if (x_robot <=0.02 and m ==3){
    m = 0;
    inite = true;
    tourne = true;
    s = 0;
    tri();
    tri_2();
    phase1 = true;
    temps = 0;
  }
}


void phase_2(float xcyl,float ycyl,int tailleCyl){   // phase 2 où le robot se déplace jusqu'au cylindre,
  kp_l = 40;                                         // le soulève et transporte jusqu'à la zone de dépot
  kd_l = 0.4;                                        // correspondant à la taille du cylindre.
  float y_zone;
  if(tailleCyl == 3){
    y_zone = -0.05;
  }
  else if(tailleCyl ==1){
    y_zone= 0.25;
  }
  if (k==0){
  consigne(xcyl, ycyl);
  k = 1;
  }
  if ((theta_rad + theta0<theta_robot-0.05 or theta_rad + theta0>theta_robot+0.05) and k==1){
    tourne = true;
  }
  else if (k == 1){
    k = 2;
    tourne = false;
  }
  if (dist_parcou < l-0.12 and k ==2){
      tourne = false;
  }
  else if (k==2){
    k=3;
    inite = true;
    consigne(-0.15,y_zone);
  }
  else if (k==3 or k==4 or k==5){
    if (temps == 0){
    temps = 1;
    }
    if (not prise){
      aller = false;
      motorD.stop();
      motorG.stop();
      if (temps != 0){
        attrapeCyl(tailleCyl);
        temps = 0;
      }
    }
    else {
      aller = true;
      if((theta_robot < theta0+theta_rad-0.05 or theta_robot > theta0+theta_rad+0.05) and k ==3){
        if (theta_robot < -M_PI){
          theta_robot += 2*M_PI;
        }
        else if (theta_robot > M_PI){
          theta_robot -= 2*M_PI;
        }
        tourne = true;
    }
    else if (k==3){
      tourne = false;
      k = 4;
      temps2 = millis();
      temps2=1;
    }
    if ((dist_parcou>l-0.10 and k == 4 and prise)){
        aller = false;
        if(temps2 !=0){
        lacheCyl();
        temps2 = 0;
      }
    }
      else if (k == 4 and not prise){
        k = 5;
        aller = true;
    }
    else if (k==5){
      tourne = false;
      inite = true;
      k = 0;
      cylindre_transport +=1;
      temps = 0;
      temps2=0;
      
    }
    
  }}}

void odometry_x(){                  // Le robot calcule sa position en x via les encodeurs.
  delta_l = (encoG() + encoD())/2;
  delta_x = delta_l*cos(theta_robot);
  x_robot += delta_x;
  
}
void odometry_y(){                  // Le robot calcule sa position en y via les encodeurs.
  delta_l = (encoG() + encoD())/2;
  
  delta_y = delta_l*sin(theta_robot);
  y_robot += delta_y; 
  
}
void odometry_theta(){                                  // Le robot calcule sa position angulaire par la 
  delta_theta = (-encoG() + encoD())/entraxe;           // différence de distance parcourue par chaque roue.
  
  theta_robot += delta_theta;
  
  
}
  
float xinit(){                 // On initialise la position en x pour calculer
  x0 = x_robot;                // la distance totale à parcourir.
  return x0;
}

float yinit(){                // On initialise la position en y pour calculer
  y0 = y_robot;               // la distance totale à parcourir.
  return y0;
}

float thetainit(){            // On initialise la position angulaire pour 
  theta0 = theta_robot;       // déterminer l'angle à parcourir.
  return theta0;
}

void consigne(float x_cible,float y_cible){                                                  // Le robot calcule la consigne totale à effectuer,
  if (inite){                                     // la distance entre lui et la cible et l'angle à effectuer.
  xinit();
  yinit();
  thetainit();
  inite = false;
  }
  l = sqrt((x0 - x_cible)*(x0 - x_cible) + ((y0 - y_cible)*(y0 - y_cible)));
  theta_rad = atan2(y_cible - y0, x_cible - x0) - theta0;
  if (theta_rad > M_PI) {
    theta_rad -= 2*M_PI;
  }
   else if (theta_rad<-M_PI){
    theta_rad += 2*M_PI;
  }
  }

void bloc(){
  double pince;
  if (phase1){
    pince = 0.11;// On sépare les régulations et les profils
  }
  else {
    pince = 0;
  }
  if (not tourne){                // lorsque le robot tourne sur lui même ou non.
    profile_l(l-pince);                 // Cette fonction est appelée toutes les 20 millisecondes.
    profile_theta(0);
    regulation_position_l();
    regulation_position_theta();
  }
  else{
    profile_theta(theta_rad);
    profile_l(0);
    regulation_position_l();
    regulation_position_theta();
  }
}



void profile_l(float l){          // Le robot calcule la position théorique du robot et l'erreur
  double tt;                      // en longueur sera la différence entre cette position et la
  float t_tot;                    // position réelle donnée par l'odométrie.
  float acceleration = 0.3;
  float vitesse = 0.1;
  float l_acc = (pow(vitesse, 2)/(2.0*acceleration));
  if (dt_l == 0){
    dt_l = 0.02;
  }
 // if (l==0){
 //   l_evo = last_l_evo;
 // }
  else if (l/2 < l_acc){
    tt = pow((l/acceleration),0.5);
    t_tot =  2*tt;
    if ((dt_l) <= tt){
      l_evo = acceleration/2*(pow(dt_l,2));
    }
    else {
      l_evo = -acceleration/2*pow(dt_l - tt, 2) + tt*acceleration*(dt_l-tt) + l/2;
    }
  }
    else{
    float l_croisiere = l - (pow(vitesse,2)/acceleration);
    tt = vitesse/acceleration;
    t_tot = 2*tt+(l_croisiere/vitesse);
    float t_croisiere = l_croisiere/vitesse;
    if (dt_l<= tt){
      l_evo = acceleration/2*pow(dt_l, 2);
    }
    if (dt_l > tt and (dt_l) <= t_tot-tt){
      l_evo = vitesse*(dt_l-tt)+l_acc;
    }
    if((dt_l) > t_tot-tt){
      l_evo = -acceleration/2*(pow(dt_l-tt-t_croisiere,2)) + vitesse*(dt_l-tt-t_croisiere) + l_croisiere + l_acc;
    }
  }
  if (dt_l+0.02 >= t_tot){
    dt_l = t_tot;
  }
  else{
    dt_l += 0.02;
}
  }

void profile_theta(float theta){                 // Le robot calcule sa position théorique en theta quand il tourne
  double vitesse_ang = 2*M_PI/5;           // et l'erreur en theta sera la différence entre la position angulaire
  double acceleration = 0.5;               // réelle donnée par l'odométrie et la position théorique.
  double theta_pp = 2*M_PI/((M_PI*2*(0.17/2))/acceleration);
  double theta_acc = pow(vitesse_ang,2)/(2*theta_acc);
  double t_tot2;
  double tt;
  if (not tourne){
    theta0 = 0;
  }
  if(dt_theta == 0){
    dt_theta = 0.02;
  }
  if (theta == 0){
    theta_evo = last_theta_evo;
  }
  else if (abs(theta/2) <= theta_acc){
    tt = pow(abs(theta)/theta_pp, 0.5);
    t_tot2 = 2*tt;
    if (theta >= 0) {
      if (dt_theta <= tt){
        theta_evo = (theta_pp/2) * (pow((dt_theta), 2));
    }
      else {
        theta_evo = -theta_pp/2 *pow(dt_theta-tt,2) + (tt*theta_pp)*(dt_theta-tt) + theta/2;
      }
    }
    else{
      if (dt_theta <= tt){
        theta_evo = -theta_pp/2 * pow((dt_theta), 2);
    }
      else {
        theta_evo = theta_pp/2 *pow(dt_theta-tt,2) + (tt*(-theta_pp))*(dt_theta-tt) + theta/2;
      }
    }
    theta_evo += theta0;
  }
  else{
    double theta_croisiere = theta - (pow(vitesse_ang,2)/theta_pp);
    tt = vitesse_ang/theta_pp;
    t_tot2 = 2*tt+(theta_croisiere/vitesse_ang);
    double t_croisiere = theta_croisiere/vitesse_ang;
    if (theta >= 0) {
      if (dt_theta < tt){
        theta_evo = theta_pp/2*pow(dt_theta, 2);
        }
      if ((dt_theta > tt) and (dt_theta < t_tot2-tt)){
        theta_evo = vitesse_ang*(dt_theta-tt)+theta_acc;
        }
      if((dt_theta) > t_tot2-tt){
        theta_evo = -theta_pp/2*(pow(dt_theta-(tt+t_croisiere),2)) + vitesse_ang*(dt_theta-(tt+t_croisiere)) + theta_croisiere + theta_acc;
        }
      }
    else{
      if (dt_theta < tt){
        theta_evo = -theta_pp/2*pow(dt_theta, 2);
        }
      if ((dt_theta > tt) and (dt_theta < t_tot2-tt)){
        theta_evo = -vitesse_ang*(dt_theta-tt)-theta_acc;
        }
      if((dt_theta) > t_tot2-tt){
        theta_evo = theta_pp/2*(pow(dt_theta-(tt+t_croisiere),2)) - vitesse_ang *(dt_theta-(tt+t_croisiere)) - theta_croisiere - theta_acc;
        }
    }
   theta_evo += theta0;
  }
  if (dt_theta +0.02 >= t_tot2 and t_tot2 >=0){
    dt_theta = t_tot2;
  }
  else{
  dt_theta += 0.02;
  }
 if (theta!=0){
    last_theta_evo = theta_evo;
  }
}

void regulation_position_l(){
  dist_parcou = sqrt((x_robot - x0)*(x_robot - x0) + ((y_robot - y0)*(y_robot - y0)));
  double erreur_l = l_evo-(dist_parcou);                                                      
  somme_erreur_l += erreur_l;                                                                
  double delta_erreur_l = erreur_l_old - erreur_l;
  double erreur_l_old = erreur_l;
  cmd_l = kp_l*erreur_l + ki_l*somme_erreur_l + kd_l*delta_erreur_l;
  
  if(cmd_l>40)
  {
      cmd_l = 40;
  }
  if (cmd_l < -40)
  {
    cmd_l = -40;
  }
}

void regulation_position_theta(){                              
  float erreur_theta = theta_evo - theta_robot;                     
  somme_erreur_theta += erreur_theta;                                
  float delta_erreur_theta = erreur_theta_old - erreur_theta;
  float erreur_theta_old = erreur_theta;
  int theta_max;
  if (tourne){
    kp_theta = 60;
    kd_theta = 0.6;
    theta_max = 60;
  }
  else {
    kp_theta = 40;
    kd_theta = 0.4;
    theta_max = 20;
  }
  
  cmd_theta = kp_theta*erreur_theta + ki_theta*somme_erreur_theta + kd_theta*delta_erreur_theta; 
  if(cmd_theta>theta_max)
  {
      cmd_theta = theta_max;
  }
  if (cmd_theta <  -theta_max)
  {   
      cmd_theta = -theta_max;
  }
}
  

void front_G(){                           // Le robot incrémente le nombre de tics parcourue par 
  int lectureA = digitalRead(encoGA);     // la roue gauche à chaque changement de position des encodeurs.
  int lectureB = digitalRead(encoGB);
  if (lectureA == lectureB){
    ++pos_G;
  }
  else {
    --pos_G;
  }
}
void front_D(){                         // Le robot incrémente le nombre de tics parcourue par la 
  int lectureA = digitalRead(encoDA);   // roue droite à chaque changement de position des encodeurs.
  int lectureB = digitalRead(encoDB);
  if (lectureA == lectureB){
    ++pos_D;
  }
  else {
    --pos_D;
  }
}
void tri(){                                                // La fonction trie les cylindres en fonction de leur temps de détection
  float dt_array[6] = {dt1,dt2,dt3,dt4,dt5,dt6};           // et regroupe les petits, moyens et grands cylindres.
  int lt_length = sizeof(dt_array) / sizeof(dt_array[0]);
  qsort(dt_array, lt_length, sizeof(dt_array[0]), sort_asc);
  for (int j=0; j<6; j++){
    for(int i =3*j+3;i<18;i+=3){
      if(dt_array[j] == cylindres[i]){
        float Asv1 = cylindres[3*j];
        float Asv2 = cylindres[3*j+1];
        float Asv3 = cylindres[3*j+2];
        cylindres[3*j] = cylindres[i];
        cylindres[3*j+1] = cylindres[i+1];
        cylindres[3*j+2] = cylindres[i+2];
        cylindres[i] = Asv1;
        cylindres[i+1] = Asv2;
        cylindres[i+2] = Asv3;
    }
  }
}
  cylindres[0] = 1;
  cylindres[3] = 1;
  cylindres[6] = 2;
  cylindres[9] = 2;
  cylindres[12] = 3;
  cylindres[15] = 3;
}

void tri_2(){                                                          // La fonction trie les cylindres en mettant en première position
  cylindres_2 = cylindres.slice(0,5,1) + cylindres.slice(12,17,1);     // le cylindre le plus proche du robot.
  for (int j=0;j<4;j++){
    int mini = 3*j;
    float distance_cyl_mini = sqrt(pow((x_robot-cylindres_2[3*j+1]),2) + pow((y_robot-cylindres_2[3*j+2]),2));
    for (int i = 3*j+3; i<12; i+=3){
      float distance_cyl = sqrt(pow((x_robot-cylindres_2[i+1]),2) + pow((y_robot-cylindres_2[i+2]),2));
      if (distance_cyl < distance_cyl_mini){
        mini = i;
        distance_cyl_mini = distance_cyl;
      }
    }
    float Asv1 = cylindres_2[3*j];
    float Asv2 = cylindres_2[3*j+1];
    float Asv3 = cylindres_2[3*j+2];
    cylindres_2[3*j] = cylindres_2[mini];
    cylindres_2[3*j+1] = cylindres_2[mini+1];
    cylindres_2[3*j+2] = cylindres_2[mini+2];
    cylindres_2[mini] = Asv1;
    cylindres_2[mini+1] = Asv2;
    cylindres_2[mini+2] = Asv3;
  }
}

int sort_asc(const void *cmp1, const void *cmp2)      // fonction de tri croissant.  
{
  float a = *((float *)cmp1);
  float b = *((float *)cmp2);
  return a < b ? -1 : (a > b ? 1 : 0);
}


void detectionD(){                                  // Fonction de détection du cylindre via le sharp de droite.
  float volts = analogRead(sharpD)*0.0048828125;     
  float dt;                                                                                               
  float distance = 13*pow(volts, -1);
  
  if ((distance <= 7.5 and distance >= 3) or (last_detecD <=7.5 and last_detecD>=3)){
    last_detecD = distance;
    if (compte_cylindre < 3){
    last_distance = -distance/100 - 0.06 + y_robot;
    } 
    else {
      last_distance = distance/100 + 0.06+y_robot;
    }
    dt = 0;
    if (t1==0){
      t1 = millis();
     }
    tf = millis();
    }
  else{
    
    if (dt == 0){
      dt = tf-t1;
      t1 = 0;
      tf = 0;
      if (dt*0.001>=0.3 and dt*0.001<=10){
        dt = dt*0.001;
        if (compte_cylindre == 0){
          dt1 = dt*vitesse;
          xcyl1 = x_robot+0.04;
          ycyl1 = last_distance; 
          cylindres.add(dt1);
          cylindres.add(xcyl1);
          cylindres.add(ycyl1);
          Serial.println("dt1 : "  +String(dt1));
          }
        else if (compte_cylindre == 1){
          dt2 = dt*vitesse;
          xcyl2 = x_robot+0.04;
          ycyl2 = last_distance;
          cylindres.add(dt2);
          cylindres.add(xcyl2);
          cylindres.add(ycyl2);
          Serial.println("dt2 : "  +String(dt2));
          }
        else if (compte_cylindre == 2){
          dt3 = dt*vitesse;
          xcyl3 = x_robot+0.04;
          ycyl3 = last_distance; 
          cylindres.add(dt3);
          cylindres.add(xcyl3);
          cylindres.add(ycyl3);
          Serial.println("dt3 : "  +String(dt3));
          }
        else if (compte_cylindre == 3){
          dt4 = dt*vitesse;
          xcyl4 = x_robot+0.04;
          ycyl4 = last_distance; 
          cylindres.add(dt4);
          cylindres.add(xcyl4);
          cylindres.add(ycyl4);
          Serial.println("dt4 : "  +String(dt4));
        }
        else if (compte_cylindre == 4){
          dt5 = dt*vitesse;
          xcyl5 = x_robot+0.04;
          ycyl5 = last_distance; 
          cylindres.add(dt5);
          cylindres.add(xcyl5);
          cylindres.add(ycyl5);
          Serial.println("dt5 : "  +String(dt5));
        }
        else if (compte_cylindre == 5){
          dt6 = dt*vitesse;
          xcyl6 = x_robot+0.04;
          ycyl6 = last_distance; 
          cylindres.add(dt6);
          cylindres.add(xcyl6);
          cylindres.add(ycyl6);
          Serial.println("dt6 : "  +String(dt6));
        }
        if (millis() -tf >=100){
        dt = 0;
        last_distance = 0;
        compte_cylindre += 1;
    }
        }
        
    }
  }
}
    
void detectionG(){                               // Fonction de détection du cylindre via le sharp gauche.
  float volts = analogRead(sharpG)*0.0048828125;   
  float dt = 0;                                                                                               
  float distance = 13*pow(volts, -1);
  if ((distance <= 7.5 and distance >= 3) or (last_detecG <=7.5 and last_detecG>=3)){
    if (compte_cylindre < 3){
    last_distance = -distance/100 + 0.09;
    }
    else {
      last_distance = distance/100 + 0.09;
    }
    last_detecG = distance;
    dt = 0;
    if (t2==0){
      t2 = millis();
     }
    tf = millis();
    }
  else{
    
    if (dt == 0){
      dt = tf-t2;
      t2 = 0;
      if (dt*0.001>=0.5 and dt*0.001<=10){
        dt = dt*0.001;
        if (compte_cylindre == 0){
          dt1 = dt;
          xcyl1 = x_robot-0.03;
          ycyl1 = last_distance; 
          cylindres.add(dt1);
          cylindres.add(xcyl1);
          cylindres.add(ycyl1);
          }
        else if (compte_cylindre == 1){
          dt2 = dt;
          xcyl2 = x_robot-0.03;
          ycyl2 = last_distance; 
          cylindres.add(dt2);
          cylindres.add(xcyl2);
          cylindres.add(ycyl2);
          }
        else if (compte_cylindre == 2){
          dt3 = dt;
          xcyl3 = x_robot-0.03;
          ycyl3 = last_distance; 
          cylindres.add(dt3);
          cylindres.add(xcyl3);
          cylindres.add(ycyl3);
          }
        else if (compte_cylindre == 3){
          dt4 = dt;
          xcyl4 = x_robot-0.03;
          ycyl4 = last_distance; 
          cylindres.add(dt4);
          cylindres.add(xcyl4);
          cylindres.add(ycyl4);
        }
        else if (compte_cylindre == 4){
          dt5 = dt;
          xcyl5 = x_robot-0.03;
          ycyl5 = last_distance; 
          cylindres.add(dt5);
          cylindres.add(xcyl5);
          cylindres.add(ycyl5);
        }
        else if (compte_cylindre == 5) {
          dt6 = dt;
          xcyl6 = x_robot-0.03;
          ycyl6 = last_distance;
          cylindres.add(dt6);
          cylindres.add(xcyl6);
          cylindres.add(ycyl6); 
        }
        if (millis() -tf >=100){
        dt = 0;
        last_distance = 0;
        compte_cylindre += 1;
    }
        }
      }
    }
}


double encoG(){                // Fonction qui renvoie la distance parcourue
  double res = coeff*pos_G;
                               // par le nombre de tics parcourus par la roue gauche.
  if (counter ==0){
    counter = 1;
  }
  else if (counter ==1){
    counter = 2;
  }
  else if (counter ==2){
   pos_G = 0; 
   counter = 0;
  }
  return res;
}

double encoD(){              // Fonction qui renvoie la distance parcourue
  double res = coeff*pos_D;
  vitesse = res/0.02*50;                           // par le nombre de tics parcourus par la roue droite.
  if (counter2 ==0){
    counter2 = 1;
  }
  else if (counter2 ==1){
    counter2 = 2;
  }
  else if (counter2 ==2){
   pos_D = 0; 
   counter2 =0;
  }
  return res;
}

void advance(){    
  if (aller){// Fonction qui envoie aux moteurs les vitesses de base augmentées
  vitD = cmd_l+cmd_theta;           // des commandes renvoyées par les régulations. Cette fonction
  vitG = cmd_l-cmd_theta;           // donne aussi le sens de rotation des moteurs.
  if (not tourne){
  if (cmd_l<0 and vitD<0){
    motorD.setSpeed(abs(-25+vitD));
    motorD.backward();
  }
  else if (cmd_l>0 and vitD>0){
    motorD.setSpeed(25+vitD);
    motorD.forward();
  }
  if (cmd_l<0 and vitG<0){
    motorG.setSpeed(abs(-25+vitG));
    motorG.backward();
  }
  else if (cmd_l>0 and vitG>0){
    motorG.setSpeed(25+vitG);
    motorG.forward();
  }
}
 else {
  vitD = cmd_theta;
  vitG = -cmd_theta;
  if (cmd_theta<0){
    motorD.setSpeed(abs(-30+vitD));
    motorD.backward();
  }
  else if (cmd_theta>0){
    motorD.setSpeed(30+vitD);
    motorD.forward();
  }
  if (cmd_theta<0){
    motorG.setSpeed(30+vitG);
    motorG.forward();
  }
  else if (cmd_theta>0){
    motorG.setSpeed(abs(-30+vitG));
    motorG.backward();
  }
 }
}
else {
  motorD.stop();
  motorG.stop();
}
}


void attrapeCyl(int tailleCyl){                 // Fonction qui attrape le cylindre en fonction de sa taille.
  if (tailleCyl == 1){ // taille petit
    if (servo_m.read() <= 170){ //monter la pince
      servo_m.write(180);
    }
    servo_f.write(155); //ouvrir la pince
    for (int position = servo_m.read(); position >=150; position--) { //descendre la pince
      servo_m.write(position);
      delay(30);
    }
    for (int position = servo_f.read(); position >= 60; position--){ //fermer la pince
      servo_f.write(position);
      delay(20);
    }
    for (int position = servo_m.read(); position <=180; position++) { //remonter la pince
      servo_m.write(position);
      delay(80); //un peu plus lent
    }
  }

  else if (tailleCyl == 3){ // taille grand
    if (servo_m.read() <= 170){ //monter la pince
      servo_m.write(180);
    }
    servo_f.write(155); //ouvrir la pince
    for (int position = servo_m.read(); position >=150; position--) { //descendre la pince
      servo_m.write(position);
      delay(15);
    }
    for (int position = servo_f.read(); position >= 80; position--){ //fermer la pince
      servo_f.write(position);
      delay(15);
    }
    for (int position = servo_m.read(); position <=180; position++) { //remonter la pince
      servo_m.write(position);
      delay(20);
    }
  }
  prise = true;
  }

void lacheCyl(){                                                       
  for (int position = servo_m.read();position >=150;position--){      // Fonction qui redescend et lâche le cylindre.
    servo_m.write(position);
    delay(15);
  }
  servo_f.write(140);
  for (int position = servo_m.read();position <=180;position++){
    servo_m.write(position);
    delay(15);
  }
  prise = false;
}
