%-EDIT--------------------------------------------------
% constraints and conditions
a_max = 1;   % [m/s] Maximal acceleration reference to inner controller
p0 = -1.5;     % [m], Initial position (use negative). The track is 1m.

% Switching time and final time
t_1 = sqrt((-p0)/(a_max));     % [s]
t_f = 2*t_1;     % [s]

% Initial error for the cart position
initPosError = 0;
%-------------------------------------------------------
%%Calculate optimal switching times
opt = optimset('MaxFunEvals',10000);
[tSwitch,fval3,exitflag3] = fsolve(@(x) fun_cart(x,a_max,p0),[t_1;t_f],opt)

% Store switch and final times for simulation to work
t = [tSwitch]

h=0.001;
% Remove dynration following
g3 = c2d(tf(1),h);
% 
% % Just to make it work
% g2 = c2d(tf(1),h);
% g1 = c2d(tf(1),h);
% g=9.81;
% l=1;
