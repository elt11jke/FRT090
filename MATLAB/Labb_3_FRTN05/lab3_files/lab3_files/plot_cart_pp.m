a = -1.1*a_max:0.002:1.1*a_max;
aRev = 1.1*a_max:-0.002:-1.1*a_max;
figure;
hold on;
for C = 2*abs(p0):-abs(p0)/3:-abs(p0)*2
  plot(C+a.^2/2/a_max,a,'r-.','LineWidth',2)
  plot(C-aRev.^2/2/a_max,aRev,'k:','LineWidth',2)
  %arrowh(C+a.^2/2/a_max,a,'r',100,[60]);
  %arrowh(C-aRev.^2/2/a_max,aRev,'k',100,[60]);
end
axis([-abs(p0)*1.1 abs(p0)*1.1 -a_max*0.6 a_max*0.6])
ylabel('velocity, p_2')
xlabel('position, p_1')

plot(simout.signals.values(:,1),simout.signals.values(:,2), ...
     'k')

