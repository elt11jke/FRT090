% Sampling time
h = 0.01;

% pendulum angular frequency
w = sqrt(9.81/0.345);

% Pendulum length
l = 0.345;
g = 9.81;

% filter that differentiates angle
tcAngleVel = 1/50; % time constant
g1 = c2d(tf([1 0],[tcAngleVel 1]),h,'tustin');

% angle filter
tcAngle = 1/100; % time constant
g2 = c2d(tf(1,[tcAngle 1]),h,'tustin');

% model for acceleration following
tcAcc = 1/50; % time constant
g3 = c2d(tf(1,[tcAcc 1]),h,'tustin');

% Initial value for pendulum angle and velocity
initPendValue = [0;0];

% Initial error for the cart position
initPosError = 0;