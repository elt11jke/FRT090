% Specify the time between each update of the plot in the
% animation. tt = 0 means no animation.
ttt = h;

% to clear prevoius frame in animation set cl = 1
% to not clear set cl = 0
cl = 1;

figure
hold all
axis equal
  
for jj=2:1:length(simout.time)
  if cl == 1
    clf
  end
  plot(simout.signals.values(2:end,1)-0.4.* ...
       sin(simout.signals.values(2:end,3)),-0.4* ...
       cos(simout.signals.values(2:end,3)),'k')
  hold on;
  plot(ff_x(2:end,2)-0.4*sin(ff_x(2:end,4)),-0.4*cos(ff_x(2:end, ...
						  4)),'k--')
  line([simout.signals.values(jj,1) simout.signals.values(jj,1)- ...
	0.4*sin(simout.signals.values(jj,3))],[0 -0.4* ...
		    cos(simout.signals.values(jj,3))])
  axis equal
  if ttt > 0
    pause(ttt)
  end
end

legend('Actual path','Optimal path')
