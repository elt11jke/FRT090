close all;
clear all;

load data_time_parallel_threds_two_sensors.txt;

acce_data_time = data_time_parallel_threds_two_sensors(5:end,1)./1000;
gyro_data_time = data_time_parallel_threds_two_sensors(5:end,2)./1000;

 figure();
 hold on all;
 acce_plot_time =  plot(acce_data_time,'r');
 gyro_plot_time = plot(gyro_data_time);
 legend( [acce_plot_time,gyro_plot_time],'acce data time','gyro data time');
 ylabel('time / ms');
 title('Time taken with parallel threds with two sensors');