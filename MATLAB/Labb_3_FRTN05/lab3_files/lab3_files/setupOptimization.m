function [A,B] = setupOptimization2(theta,h,l)

g = 9.81;

N = length(theta);

% double integrator linear, can be set directly
AMatrices = zeros(4*N,4);
BMatrices = zeros(4,N);
A = zeros(4*N,4);

for jj = 1:N
  w = sqrt(g/l*cos(theta(jj)));
  b = 1/l*cos(theta(jj));
  Phi = [1 h 0 0;0 1 0 0;0 0 cos(w*h) 1/w*sin(w*h);0 0 -w*sin(w*h) cos(w*h)];
  Gamma = [h^2/2;h;-b/w/w*(cos(w*h)-1);b/w*sin(w*h)];
  if jj == 1
    A(1:4,1:4) = Phi; 
    AMatrices(1:4,1:4) = Phi\eye(4);
    BMatrices(:,1) = Gamma;
  else
    AMatrices((jj-1)*4+1:jj*4,1:4) = Phi\eye(4);
    BMatrices(:,jj) = Gamma;
    A((jj-1)*4+1:jj*4,1:4) = Phi*A((jj-2)*4+1:(jj-1)*4,1:4);
  end 
end
B = zeros(4*N,N);
BTemp = A*AMatrices(1:4,1:4);
B(:,1) = BTemp*BMatrices(:,1);
for jj = 2:N
  BTemp = [zeros((jj-1)*4,4);BTemp((jj-1)*4+1:end,:)*AMatrices((jj-1)*4+1:jj*4,1:4)];
  B(:,jj) = BTemp*BMatrices(:,jj);
end


