%-EDIT----------------------------------------------------
% constraints and conditions
a_max = ...;   % [m/s] Maximal acceleration reference to inner controller
p0 = -...;     % [m], Initial position (use negative). The track is 1m.

% MPC-weight matrices
Q = [ ... ; ... ; ... ; ...];  % State penalty, [pos,vel,ang,ang-vel]
R = ...;                       % Control penalty

% sampling time
h = ...;

% MPC-horizon
N = ...;

% inital values in the pendulum states [angle,angleVel]
initPendValue = [0;0];

% Initial error for the cart position
initPosError = 0;
%---------------------------------------------------------

w = sqrt(9.81/0.345);
% calculate optimal switching times
opt = optimset('MaxFunEvals',10000);
[tSwitch,fval3,exitflag3] = fsolve(@(x) fun_cart_pend(x,a_max,w,p0),[0.3;0.6;0.9;1.2],opt)

% simulate the system with optimal switching times to get state trajectories
sim('state_traj',[0 tSwitch(4)])

% create feed forward control signal
ff_u = [0 a_max;
	tSwitch(1) -a_max;
	tSwitch(2) a_max;
	tSwitch(3) -a_max;
        tSwitch(4) 0;
	tSwitch(4) 0];

% create state trajectories for the feedback
ff_x = [simout_sim.time simout_sim.signals.values(:,1:4)];

% create tSwitch such that the cart is at rest at the end in
% when switchingtimes are multiples of h
tSwitch(5) = tSwitch(4);
tSwitch(1:3) = round(tSwitch(1:3)/h)*h;
dtSwitch = diff(tSwitch(1:3))
tSwitch(4) = tSwitch(3)+(tSwitch(1)-dtSwitch(1)+dtSwitch(2))

% paramters
g = 9.81;
l = 0.345;
initPosError = 0;

% Creation of matrices needed for the MPC-controller

tt = ff_x(:,1);
x = ff_x(:,2);
v_x = ff_x(:,3);
theta = ff_x(:,4);
v_theta = ff_x(:,5);


% extract sampled data
t = [0:h:tt(end)]';
iterU = 1;
uSamp = zeros(length(t),1);
for jj = 1:length(t)
  if t(jj) < tSwitch(iterU)-h/2
    uSamp(jj) = ff_u(iterU,2);
  else
    iterU = iterU+1;
    uSamp(jj) = ff_u(iterU,2);
  end
end

xSamp = [];
v_xSamp = [];
thetaSamp = [];
v_thetaSamp = [];

for jj = 1:length(t)
  [a,b] = min(abs(tt-t(jj)));
  if tt(b) > t(jj)
    xSamp = [xSamp;(x(b)-x(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+x(b-1)];
    v_xSamp = [v_xSamp;(v_x(b)-v_x(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+v_x(b-1)];
    thetaSamp = [thetaSamp;(theta(b)-theta(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+theta(b-1)];
    v_thetaSamp = [v_thetaSamp;(v_theta(b)-v_theta(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+v_theta(b-1)];
  else 
    xSamp = [xSamp;(x(b+1)-x(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+x(b)];
    v_xSamp = [v_xSamp;(v_x(b+1)-v_x(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+v_x(b)];
    thetaSamp = [thetaSamp;(theta(b+1)-theta(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+theta(b)];
    v_thetaSamp = [v_thetaSamp;(v_theta(b+1)-v_theta(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+v_theta(b)];
  end
end


% create longer horizon
uSamp = [uSamp;0*ones(N+2,1)];
xSamp = [xSamp;0*ones(N+2,1)];
v_xSamp = [v_xSamp;0*ones(N+2,1)];
thetaSamp = [thetaSamp;0*ones(N+2,1)];
v_thetaSamp = [v_thetaSamp;0*ones(N+2,1)];
t = [0:length(uSamp)-1]*h;

refU = struct('time',t(1:end-N)','signals',[]);
refX = struct('time',t(1:end-N)','signals',[]);
refvX = struct('time',t(1:end-N)','signals',[]);
refTheta = struct('time',t(1:end-N)','signals',[]);
refvTheta = struct('time',t(1:end-N)','signals',[]);

refUVal = struct('values',[]);
refXVal = struct('values',[]);
refvXVal = struct('values',[]);
refThetaVal = struct('values',[]);
refvThetaVal = struct('values',[]);


for jj = 1:length(t)-N
  refUVal.values(jj,:) = uSamp(jj:jj+N-1);
  refXVal.values(jj,:) = xSamp(jj:jj+N-1);
  refvXVal.values(jj,:) = v_xSamp(jj:jj+N-1);
  refThetaVal.values(jj,:) = thetaSamp(jj:jj+N-1);
  refvThetaVal.values(jj,:) = v_thetaSamp(jj:jj+N-1);
end

refU.signals = refUVal;
refX.signals = refXVal;
refvX.signals = refvXVal;
refTheta.signals = refThetaVal;
refvTheta.signals = refvThetaVal;

% remove timeinstants where consecutive state and ctrl ref are the
% same (from b+1 till end)
[a,b] = min(abs(tt(end)-t));
refU.time = [refU.time(1:b+1);20];
refX.time = [refU.time(1:b+1);20];
refvX.time = [refU.time(1:b+1);20];
refTheta.time = [refU.time(1:b+1);20];
refvTheta.time = [refU.time(1:b+1);20];

refU.signals.values = [refU.signals.values(1:b+1,:);refU.signals.values(b+1,:)];
refX.signals.values = [refX.signals.values(1:b+1,:);refX.signals.values(b+1,:)];
refvX.signals.values = [refvX.signals.values(1:b+1,:);refvX.signals.values(b+1,:)];
refTheta.signals.values = [refTheta.signals.values(1:b+1,:);refTheta.signals.values(b+1,:)];
refvTheta.signals.values = [refvTheta.signals.values(1:b+1,:);refvTheta.signals.values(b+1,:)];


% filter that differentiates angle
g1 = c2d(tf([1 0],[tcAngleVel 1]),h,'tustin');

% angle filter
g2 = c2d(tf(1,[tcAngle 1]),h,'tustin');

% model for acceleration following
g3 = c2d(tf(1,[tcAcc 1]),h,'tustin');
