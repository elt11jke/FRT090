close all;
clear all;

load data_all_param_two_sensors.txt;
offset_gyro = -5446/1000;
ooset_acce = 25/1000;

load data_all_param_two_sensors.txt;

time_data =data_all_param_two_sensors(:,1);

acce_data = data_all_param_two_sensors(:,2);
gyro_data =data_all_param_two_sensors(:,3) ;
control_data =data_all_param_two_sensors(:,4) ;
compl_data =data_all_param_two_sensors(:,5) ;
power_data =data_all_param_two_sensors(:,6) ;

offset_data_gyro = data_all_param_two_sensors(:,7);
offset_data_acce =data_all_param_two_sensors(:,8) ;

figure();
hold on;
plot_time= plot();

