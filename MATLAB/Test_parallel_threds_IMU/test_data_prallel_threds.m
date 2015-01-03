close all;
clear all;

load data_parallel_threds_IMU.txt;

acce_data_time = data_parallel_threds_IMU(:,1);
gyro_data_time = data_parallel_threds_IMU(:,2);

 figure();
 hold on all;
 acce_plot_time =  plot(acce_data_time,'r');
 gyro_plot_time = plot(gyro_data_time);
 legend( [acce_plot_time,gyro_plot_time],'acce data time','gyro data time');
 ylabel('time / ms');
 title('Time taken with parallel threds AbsoluteIMU');