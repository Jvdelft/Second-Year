% courbes U-v projet

clear all; clc;
U = [20:10:40, 50:20:250];
vd = [0; 25.80; 42.43; 58.42; 94.35; 119.10; 148.04; 177.86; 210.53; 240.30; 269.17; 298.82; 329.00; 357.82]
vg = [5.06; 23.91; 42.03; 53.28; 83.77; 114.09; 138.55; 160.65; 193.50; 216.00; 248.49; 274.76; 302.67; 327.45]
plot(U,vd,'b-*',U,vg, 'r-*');
title('Graphe des courbes vitesse-tension des deux moteurs');
xlabel('Tension [6* 255^{-1}V]');
ylabel('Vitesse [mm.s^{-1}]');
legend('Moteur droit','Moteur gauche');
set(gcf,'color','w');