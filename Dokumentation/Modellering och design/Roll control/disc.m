clear all
close all
modelinertia

h=0.10;
q11 = 1/(1.57^2);
q22 = 1/(0.349^2);
q33 = 1/(16^2);
Q = [q11 0 0; 0 q22 0; 0 0 q33];
R = 1/(0.384^2);

[phi gamma]=c2d(A, B_tau, h)

[L S e]=dlqr(phi, gamma, Q, R)

