function F = fun_cart(x,a_max,p0)

% x is the unknown vector with the optimal switching times, use x(1) as
% t_1 and x(2) as t_f. a_max is the maximal control magnitude and p0 is the
% initial position.

F = [p0 + a_max*(2*x(1)*x(2)-x(1)^2-1/2*x(2)^2);
     a_max*(2*x(1)-x(2))];
