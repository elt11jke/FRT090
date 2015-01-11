close all;
clear all;


offset = -37*(10^-6);

load test_zero_sample_data_01_better_mesurement.txt;

acce_data = test_zero_sample_data_01_better_mesurement(:,2).*(10^-3);
gyro_data = test_zero_sample_data_01_better_mesurement(:,3).*(10^-3);
control_data = test_zero_sample_data_01_better_mesurement(:,4).*(10^-3);
comp_data = test_zero_sample_data_01_better_mesurement(:,5).*(10^-3);



		    


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