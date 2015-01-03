close all;
clear all;

load acce_data.txt

acce_refresh_time = acce_data(:,2);

plot(acce_refresh_time);
ylabel('refresh time acce / ms');
xlabel();