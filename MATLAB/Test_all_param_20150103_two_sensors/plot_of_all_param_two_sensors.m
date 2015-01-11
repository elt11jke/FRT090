close all;
clear all;

load data_all_param_two_sensors.txt;
offset_gyro = -5446/1000;
ooset_acce = 25/1000;

load data_all_param_two_sensors.txt;

time_data =data_all_param_two_sensors(:,1);

acce_data = data_all_param_two_sensors(:,2)./1000;
gyro_data = data_all_param_two_sensors(:,3)./1000 ;
control_data =data_all_param_two_sensors(:,4)./1000 ;
compl_data =data_all_param_two_sensors(:,5)./1000 ;
power_data =data_all_param_two_sensors(:,6)./1000 ;

offset_data_gyro = data_all_param_two_sensors(:,7)./1000;
offset_data_acce =data_all_param_two_sensors(:,8)./1000 ;

figure;
plot_time = plot(time_data);
xlabel('');
ylabel('time / ms');
title('The update time of the program');

figure;
subplot(2,1,1);
plot_compl = plot(compl_data);
title('Complementary filter');
xlabel('time / ms');
ylabel('angle / degrees');
subplot(2,1,2);
hold on all;
plot_control = plot(control_data,'r');
plot_power = plot(power_data);
legend([plot_control,plot_power],'control data','power data');
xlabel('time / ms');
ylabel('strenght / no quantity');
title('The controls')



figure;
subplot(2,2,1)
plot_acce = plot(   acce_data,'r');
xlabel('time / ms');
ylabel('angle / degrees');
subplot(2,2,2);
plot_gyro = plot(   gyro_data,'y');

xlabel('time / ms');
ylabel('angle / degrees');
title('');