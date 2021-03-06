%Modell för inverted pendulum (inertia hjulet)

s=tf('s');

%Konstanter
g= 9.81;

%Parametrar som beror på enhjulingen
m= 0.70;
M= 0.10;
l= 0.15;
L= 0.30;
R= 0.20;
Jp= m*(l^2);
Jw= (1/2)*M*(R^2);

%Övriga parametrar
a= m*l*g;
b= (Jp + 2*Jw)/2;


%Matriser
A=[0 1 0; a/Jw 0 0 ; -(a/Jp) 0 0];
B_tau= [0; -(1/Jw); -(b/(Jw*Jp))];
B_d = [0; 1; 0];
B = [B_tau B_d];
C= [1 0 0];
D=0;

%Systemet
SYSinertia= ss(A,B,C,D);
Pin1 = zpk(tf(SYSinertia))

%State feedback
Q=eye(3);
R=[1 0; 0 100];
lr=1;

[L,S,E] = lqr(SYSinertia,Q,R)

%Systemet med state feedback
SYSinertia = ss(A-B*L,B*lr,C,D);
Pin= zpk(tf(SYSinertia))
