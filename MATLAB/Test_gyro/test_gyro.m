close all;
clear all;



load sample_02.txt;

gyro_angle=sample_02(:,2);
refresh_time = sample_02(:,1);

figure();
gyro_plot=plot(gyro_angle);
hold on;
refresh_plot=plot(refresh_time,'r');
legend([gyro_plot,refresh_plot],'gyro angle / degrees','refresh time / ms');