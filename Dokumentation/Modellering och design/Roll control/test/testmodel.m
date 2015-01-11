clear all
close all

%Modell för inverted pendulum (inertia hjulet)

s=tf('s');

%Konstanter
g= 9.81;

%Parametrar som vi ändrar
m= 0.771;
M= 0.636;
l= 0.21;
L= 0.41;
R= 0.15;
Jp= m*(l^2);
Jw= (1/2)*M*(R^2);

a= (g/(Jp+Jw+M*(L^2)))*(l*m + L*M);
b1= 1/(Jp+Jw + M*(L^2));
b2= 1/Jw;
ki=1/20;

%Tillst�nd
%x1: Pendelns vinkel
%x2: Pendelns vinkelhastighet
%x3: Motorns vinkel
%x4: Motorns vinkelhastighet

%Matriser
A=[0 1 0 0; a 0 0 0; 0 0 0 0; 0 0 1 0];
B_tau= [0; -b1; b2; 0];
B_d = [0; 1; 0; 0];
B= [B_tau B_d]
Cangle=[1 0 0 0];
Call= eye(4);
D=zeros(4,2);

%Systemet
C=Call;
SYSinertia= ss(A,B,C,D);
Pin = zpk(tf(SYSinertia));


% %State feedback (funkar inte)
% Q=eye(3);
% R=[1 0; 0 100];
% lr=1;
% 
% [L,S,E] = lqr(SYSinertia,Q,R);
% 
% %Systemet med state feedback
% SYSinertia = ss(A-B*L,B*lr,C,D);
% PinSF= zpk(tf(SYSinertia))

%State feedback
q11 = 0;
q22 = 0;
q33 = 0;
q44 = 30;
Q = [q11 0 0 0; 0 q22 0 0; 0 0 q33 0; 0 0 0 q44];
R = 1000;
ang = [1 0 0 0];

[L,S,E] = lqr(A,B_tau,Q,R)
PinSF = zpk(tf(ss(A-B_tau*L,B,C,D)));

Asf = A-B_tau*L;

x10=0;
x20=0;
x30=0;
x40=0;