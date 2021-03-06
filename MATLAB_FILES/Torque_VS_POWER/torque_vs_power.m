
clear all;
close all;


% M�tning med olof

torque = [80 140 200 240 270 310 350 370 400]./1000 *9.81*0.0716;
pow = 20:10:100;


plot(pow,torque);
hold all;
handledare_matning=plot(pow,torque,'-bo');
xlabel('power/ input');
ylabel('Torque / Nm');



% V�r m�tning utan Olof

torque_1 = [170 320 450 590]./1000 *9.81*0.0716;
pow_1 = 25:25:100;
egen_matning = plot(pow_1,torque_1,'-ro');

title('Torque VS power');


lengend_1 = legend([handledare_matning, egen_matning],'m�tning 1','m�tning 2','Location','northwest');


