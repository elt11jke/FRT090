function [uOut] = MPCsimulink(uIn)

% start execusion time timer
tic

% control and precition horizon, or signal that no feedback should
% be used (N == -1)
N = uIn(end);
if N == -1
  % this section is run if we do not use feedback
  tt = toc;
  uOut = [uIn(1);uIn(1);tt;0];
else
  % optimal control and state sequences over the horizon
  URef = uIn(1:N);
  xRef= uIn(N+1:2*N);
  vXRef = uIn(2*N+1:3*N);
  thetaRef = uIn(3*N+1:4*N);
  vThetaRef = uIn(4*N+1:5*N);
  % feedback measurements for the states
  pos0 = uIn(5*N+1);
  v_x0 = uIn(5*N+2);
  theta0 = uIn(5*N+3);
  v_theta0 = uIn(5*N+4);
  % state cost for the states (no crossterms, only diagonal elements)
  Qblk = uIn(5*N+5:5*N+8);
  % control cost
  Rblk = uIn(5*N+9);
  % sampling time
  h = uIn(5*N+10);
  % starting position for the cart. To set coordinates relative to
  % track (used in position constraints)
  initPos = uIn(5*N+11);
  
  % parameters
  l = 0.345;
  g = 9.81;
  
  % calculates how far each state if from optimal solution
  x0 = [pos0;v_x0;theta0;v_theta0];
  deltax0 = x0-[xRef(1);vXRef(1);thetaRef(1);vThetaRef(1)];
  
  % set up A and B matrices for optimization
  [A,B] = setupOptimization(thetaRef(1:N),h,l);

  % choose part of A and B that determines x (used in position constraints)
  Bx = B([1:4:4*N],:);
  Ax = A([1:4:4*N],:);
  
  % control cost function
  r = Rblk;
  
  % create state cost function
  Qx = zeros(4*N,1);
  QvX = zeros(4*N,1);
  Qtheta = zeros(4*N,1);
  QvTheta = zeros(4*N,1);
  Qx([1:4:end]) = Qblk(1);
  QvX([2:4:end]) = Qblk(2);
  Qtheta([3:4:end]) = Qblk(3);
  QvTheta([4:4:end]) = Qblk(4);
  Q = diag(Qx)+diag(QvX)+diag(Qtheta)+diag(QvTheta);
  
  Q = sparse(Q);
  
  % cost matrices for optimization
  BQB = B'*Q*B;
  BQB = 1/2*(BQB+BQB');
  Hqp = 2*(r*eye(size(B'*B))+BQB);
  fqp = 2*(B'*Q*A*deltax0);
  
  % optimization constraints
  Aqp = [];
  bqp = [];
  % position constraint (assumes that the cart starts a bit more than 10 cm
  % from left end of the track)
  Aqp = [Bx;-Bx];
  bqp = [(0.95+initPos)*ones(N,1)-Ax*deltax0-xRef;(0.15-initPos)*ones(N,1)+Ax*deltax0+xRef];
  
  % control constraints (abs(acc))<7
  Aqp = [Aqp;eye(N)];
  bqp = [bqp;7*ones(N,1)-URef];
  Aqp = [Aqp;-eye(N)];
  bqp = [bqp;7*ones(N,1)+URef];
  
  % introduce one soft constraint (slack decision variables in pos) to
  % avoid infeasible solutions
  Hqp = blkdiag(Hqp,100000);
  fqp = [fqp;0];
  Aqp = [Aqp [-ones(2*N,1);zeros(length(Aqp(:,1))-2*N,1)]];
  Aqp = [Aqp;[zeros(1,length(Aqp(1,:))-1) -1]];
  bqp = [bqp;0];
  
  % solve optimization problem using c-script
  deltaUOpt = qpas(Hqp,fqp,Aqp,bqp);
  
  % stop execution time timer
  tt = toc;
  
  uOut = [deltaUOpt(1)+URef(1);URef(1);tt;deltaUOpt(end)];
end
