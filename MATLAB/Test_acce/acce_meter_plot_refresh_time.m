<<<<<<< HEAD
close all;
clear all;

load acce_data.txt

acce_refresh_time = acce_data(:,2);

[~,loc]=max(acce_refresh_time);
acce_refresh_time(loc) = [];
[~,loc]=max(acce_refresh_time);
acce_refresh_time(loc) = [];
[~,loc]=max(acce_refresh_time);
acce_refresh_time(loc) = [];
[~,loc]=max(acce_refresh_time);
acce_refresh_time(loc) = [];


plot(1:30:size(acce_refresh_time)*30,acce_refresh_time);
title('Refresh time versus time');
ylabel('time / ms');
xlabel('time');
=======
close all;
clear all;

load acce_data.txt

acce_refresh_time = acce_data(:,2);

plot(acce_refresh_time);
ylabel('refresh time acce / ms');
xlabel();
>>>>>>> a78826412a21b9eb46e5d2f680bc0a777918c47d
