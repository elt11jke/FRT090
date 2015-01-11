clear all
close all
testmodel

h=0.10;
q11 = 0;
q22 = 0;
q33 = 0;
q44 = 30;
Q = [q11 0 0 0; 0 q22 0 0; 0 0 q33 0; 0 0 0 q44];
R = 1000;

[phi gamma]=c2d(A, B_tau, h)

[L S e]=dlqr(phi, gamma, Q, R)

