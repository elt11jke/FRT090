close all;
clear all;


offset = -4;

load test_zero_sample_data_02.txt;

acce_data = test_zero_sample_data_02(:,2);
gyro_data = test_zero_sample_data_02(:,3);
control_data = test_zero_sample_data_02(:,4);
comp_data = test_zero_sample_data_02(:,5);



		    


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