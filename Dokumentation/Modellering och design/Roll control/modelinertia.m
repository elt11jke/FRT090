%Modell för inverted pendulum (inertia hjulet)

s=tf('s');

%Konstanter
g= 9.81;

%Parametrar som vi ändrar
m= 0.70;
M= 0.15;
l= 0.15;
L= 0.30;
R= 0.10;
Jp= m*(l^2);
Jw= (1/2)*M*(R^2);

a= (g/Jp)*(l*m + L*M);
b1= 1/(Jp+Jw + M*(L^2));
b2= 1/Jw;
ki=1/20;

%Tillst�nd
%x1: Pendelns vinkel
%x2: Pendelns vinkelhastighet
%x3: Motorns vinkel
%x4: Motorns vinkelhastighet

%Matriser
A=[0 1 0; a 0 0; 0 0 0];
B_tau= [0; -b1; b2];
B_d = [0; 1; 0];
B= [B_tau B_d]
Cangle=[1 0 0];
Call= eye(3);
D=zeros(3,2);

%Systemet
C=Call;
SYSinertia= ss(A,B,C,D);
Pin = zpk(tf(SYSinertia));


%State feedback
Q=eye(3);
R=[1 0; 0 100];
lr=1;

[L,S,E] = lqr(SYSinertia,Q,R);

%Systemet med state feedback
SYSinertia = ss(A-B*L,B*lr,C,D);
PinSF= zpk(tf(SYSinertia))