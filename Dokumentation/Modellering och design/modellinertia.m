close all
clear all

s=tf('s');

%Konstanter
g= 9.81;

%Parametrar som vi ändrar
m= 0.8642;
M= 3.2280;
l= 6.3540e-04;
L= 0.0520;
Jp= 0.0304;
Jw= 7.9860e-04;

%Matriser
A=[0 1 0 0; (g/Jp)*(l*m + L*M) 0 0 0; 0 0 0 1; 0 0 0 0];
B= [0; -1/Jp; 0; 1/Jw];
C=[1 0 0 0];
D=0;

%Systemet
SYSinertia= ss(A,B,C,D);
Pin = zpk(tf(SYSinertia))