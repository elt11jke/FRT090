close all;
clear all;

load all_param_sample_1.txt;
offset = -3;

acce_data = all_param_sample_1(:,2);
gyro_data = all_param_sample_1(:,3);
control_data = all_param_sample_1(:,4);
comp_data = all_param_sample_1(:,5);


 figure();
 hold on all;
 acce_plot =  plot(acce_data,'.');
 gyro_plot = plot(gyro_data);
 comp_data = plot(comp_data);
 ylabel('angle / degrees');
 xlabel('time / ms');
 
 legend( [acce_plot,gyro_plot,comp_data],'acce data','gyro data','comp data');

hold off all;

figure();
hold on all;
control_data = plot(control_data);
legend([control_data],'control data' );
xlabel('time / ms');
ylabel('control signal');

