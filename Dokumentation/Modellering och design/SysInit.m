clear all
close all

modellinertia
Cindesign

T= minreal(zpk(Cin*Pin/(1+ Cin*Pin)))

S= minreal(zpk(1/(1+ Cin*Pin)))

pole(T)


%Complementary sensitivity function plots
figure(1)
nyquist(T)

figure(2)
margin(T)

%Sensitivity function plots
figure(3)
bode(S)