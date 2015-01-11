function F = fun_cart_pend(x,u,w,x0)

F = [x0+u*(-x(1)^2+x(2)^2-x(3)^2+1/2*x(4)^2);
     2*x(1)-2*x(2)+2*x(3)-x(4);
     cos(w*x(4))-2*cos(w*(x(4)-x(1)))+2*cos(w*(x(4)-x(2)))-2*cos(w*(x(4)-x(3)))+1;
     -sin(w*x(4))+2*sin(w*(x(4)-x(1)))-2*sin(w*(x(4)-x(2)))+2*sin(w*(x(4)-x(3)))];