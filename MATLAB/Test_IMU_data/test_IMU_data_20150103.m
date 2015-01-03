close all;
clear all;

load test_IMU_data_sen_0.txt;

time_data = test_IMU_data_sen_0(:,1)./1000;
acce_data = test_IMU_data_sen_0(:,2)./1000;
gyro_data = test_IMU_data_sen_0(:,3)./ 1000;


 figure();
 
 subplot(2,1,1);
 hold on all;
 
 acce_plot =  plot(acce_data,'r');
 gyro_plot = plot(gyro_data);
 
 legend( [acce_plot,gyro_plot],'acce data','gyro data');
 xlabel('time / ms');
 ylabel(' angle / degrees');

 subplot(2,1,2);
 hold on all;
 time_plot = plot(time_data) ;
 
 size_data = size(time_data);
 average_vec = [sum(time_data)./ size_data(1,1), sum(time_data)./ size_data(1,1)];
 
 average_plot = plot( [1 size_data(1,1)] ,average_vec,'r');
 legend([time_plot,average_plot] , 'time data','average data');
 title('Data from the UMI with sensitivity 0');
 ylabel('time / ms');
 
  
  
  
  