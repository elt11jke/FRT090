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



figure();
offset_1_plot = plot(offset_1);
hold on all;
offset_2_plot = plot(offset_2);
offset_3_plot = plot(offset_3);
offset_4_plot = plot(offset_4);
offset_5_plot = plot(offset_5);



refresh_plot_1=plot(refresh_time_1,'r');
refresh_plot_2=plot(refresh_time_2,'r');
refresh_plot_3=plot(refresh_time_3,'r');
refresh_plot_4=plot(refresh_time_4,'r');
refresh_plot_5=plot(refresh_time_5,'r');

xlabel('time / ms');
ylabel('degrees');



legend([offset_1_plot,offset_2_plot,offset_3_plot,offset_4_plot,offset_5_plot,refresh_plot_1,refresh_plot_2,refresh_plot_3,refresh_plot_4,refresh_plot_5],'offset_1 / degrees','offset_2 / degrees','offset_3 / degrees','offset_4 / degrees','offset_5 / degrees','refresh time_1 / ms','refresh time_2 / ms','refresh time_3 / ms','refresh time_4 / ms','refresh time_5 / ms');