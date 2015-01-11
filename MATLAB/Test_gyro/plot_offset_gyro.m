close all;
clear all;



load sample_offset_01.txt;
load sample_offset_02.txt;
load sample_offset_03.txt;
load sample_offset_04.txt;
load sample_offset_05.txt;

offset_1=sample_offset_01(:,2);
offset_2=sample_offset_02(:,2);
offset_3=sample_offset_03(:,2);
offset_4=sample_offset_04(:,2);
offset_5=sample_offset_05(:,2);

refresh_time_1 = sample_offset_01(:,1);
refresh_time_2 = sample_offset_02(:,1);
refresh_time_3 = sample_offset_03(:,1);
refresh_time_4 = sample_offset_04(:,1);
refresh_time_5 = sample_offset_05(:,1);

% remove a to big sample
[~,location]=max(refresh_time_5);
refresh_time_5(location)=[ ];
[~,location]=max(refresh_time_4);
refresh_time_4(location)=[ ];
[~,location]=max(refresh_time_3);
refresh_time_3(location)=[ ];
[~,location]=max(refresh_time_2);
refresh_time_2(location)=[ ];
[~,location]=max(refresh_time_1);
refresh_time_1(location)=[ ];



figure();
subplot(2,1,1);
hold on all;
title('Offset versus time');
offset_1_plot = plot(1:1:size(offset_1),offset_1,'r');
offset_2_plot = plot(1:1:size(offset_2),offset_2,'y');
offset_3_plot = plot(offset_3,'k');
offset_4_plot = plot(offset_4,'g');
offset_5_plot = plot(offset_5,'m');
xlabel('time');
ylabel('angle per time unit / degrees per second');
legend([offset_1_plot,offset_2_plot,offset_3_plot,offset_4_plot,offset_5_plot],'offset 1 / degrees','offset 2 / degrees','offset 3 / degrees','offset 4 / degrees','offset 5 / degrees');


subplot(2,1,2);
hold on all;
title('Refresh time versus time');
refresh_plot_1=plot(refresh_time_1,'r');
refresh_plot_2=plot(refresh_time_2,'y');
refresh_plot_3=plot(refresh_time_3,'k');
refresh_plot_4=plot(refresh_time_4,'g');
refresh_plot_5=plot(refresh_time_5,'m');

xlabel('time');
ylabel('time / ms');



legend([refresh_plot_1,refresh_plot_2,refresh_plot_3,refresh_plot_4,refresh_plot_5],'refresh time 1 / ms','refresh time 2 / ms','refresh time 3 / ms','refresh time 4 / ms','refresh time 5 / ms');