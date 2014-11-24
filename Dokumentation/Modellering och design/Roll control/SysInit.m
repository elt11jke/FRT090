clear all
close all

modelinertia
Cindesign

x10=deg2rad(2);
x20=0;
x30=0;

T= minreal(zpk(Cin*PinSF(1,1)/(1+ Cin*PinSF(1,1))))

S= minreal(zpk(1/(1+ Cin*PinSF(1,1))))

pole(T)


%Complementary sensitivity function plots
figure(1)
nyquist(T)

figure(2)
margin(T)

%Sensitivity function plots
figure(3)
bode(S)