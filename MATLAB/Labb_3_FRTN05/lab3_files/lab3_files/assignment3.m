%-EDIT----------------------------------------------------
% constraints and conditions
a_max = ...;   % [m/s] Maximal acceleration reference to inner controller
p0 = -...;   % [m], Initial position (use negative). The track is 1m.

% initial value for pendulum (angle, angleVel)
initPendValue = [0;0];

% Initial error for the cart position
initPosError = 0;
%--------------------------------------------------------

% calculate optimal switching times
opt = optimset('MaxFunEvals',10000);
[tSwitch,fval3,exitflag3] = fsolve(@(x) fun_cart_pend(x,a_max,w,p0),[0.3;0.6;0.9;1.2],opt)

% simulate the system with optimal switching times to get state trajectories
sim('state_traj',[0 tSwitch(4)])

% create feed forward control signal
refU = [0 a_max;
	tSwitch(1) -a_max;
	tSwitch(2) a_max;
	tSwitch(3) -a_max;
        tSwitch(4) 0];

% create state trajectories for the feedback
ff_x = [simout_sim.time simout_sim.signals.values(:,1:4)];

% set N = -1 to signal to MPC-controller that only feedforward is
% to be used (before rest of MPC-controller initialized)
N = -1;

% set matrices to zero that is used in assignment4
Q = 0;
R = 0;
refX = [0 0;20 0];
refvX = [0 0;20 0];
refTheta = [0 0;20 0];
refvTheta = [0 0;20 0];
initPosError = 0;

% model for acceleration following
g3 = c2d(tf(1,[tcAcc 1]),h,'tustin');
