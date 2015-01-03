%-EDIT----------------------------------------------------
% MPC-weight matrices
Q = [ ... ; ... ; ... ; ... ];  % State penalty, [pos,vel,ang,ang-vel]
R = ...;                        % Control penalty

% sampling time    
h = ...;
    
% MPC-horizon    
N = ...;
%---------------------------------------------------------

% load the optimized trajectories to matlab
d = loaddym('pendulum_result.txt');
    
[tt,u] = getvar(d,'a_ref');                % Load u
[tt,x] = getvar(d,'p');                % Load x
[tt,v_x] = getvar(d,'p_dot');            % Load v_x
[tt,theta] = getvar(d,'theta');        % Load theta
[tt,v_theta] = getvar(d,'theta_dot');    % Load v_theta

% create feed forward control signal
ff_u = [tt u];

% Initial position (according to initial pos in optimization)
p0 = -0.8;
initPosError = 0;
    
% create state trajectories for the feedback
ff_x = [tt x v_x theta v_theta];

% extract sampled data
t = [0:h:tt(end)]';
uSamp = [];
xSamp = [];
v_xSamp = [];
thetaSamp = [];
v_thetaSamp = [];

for jj = 1:length(t)
  [a,b] = min(abs(tt-t(jj)));
  if tt(b) > t(jj)
    uSamp = [uSamp;(u(b)-u(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+u(b-1)];
    xSamp = [xSamp;(x(b)-x(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+x(b-1)];
    v_xSamp = [v_xSamp;(v_x(b)-v_x(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+v_x(b-1)];
    thetaSamp = [thetaSamp;(theta(b)-theta(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+theta(b-1)];
    v_thetaSamp = [v_thetaSamp;(v_theta(b)-v_theta(b-1))/(tt(b)-tt(b-1))*(t(jj)-tt(b-1))+v_theta(b-1)];
  else 
    uSamp = [uSamp;(u(b+1)-u(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+u(b)];
    xSamp = [xSamp;(x(b+1)-x(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+x(b)];
    v_xSamp = [v_xSamp;(v_x(b+1)-v_x(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+v_x(b)];
    thetaSamp = [thetaSamp;(theta(b+1)-theta(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+theta(b)];
    v_thetaSamp = [v_thetaSamp;(v_theta(b+1)-v_theta(b))/(tt(b+1)-tt(b))*(t(jj)-tt(b))+v_theta(b)];
  end
end

%-----------------------------

% create longer horizon
uSamp = [uSamp;0*ones(500,1)];
xSamp = [xSamp;0*ones(500,1)];
v_xSamp = [v_xSamp;0*ones(500,1)];
thetaSamp = [thetaSamp;0*ones(500,1)];
v_thetaSamp = [v_thetaSamp;0*ones(500,1)];
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
