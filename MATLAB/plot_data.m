data = csvread('C:\Users\frt090_B\Desktop\sample.txt');

figure(2)
subplot(311)
hold on
plot(1:size(data,1), data(:,2) + 600)
plot(1:size(data,1), 0*(1:size(data,1)))

subplot(312)
plot(data(:, 3))


subplot(313)
hold on
plot(data(:, 5)/1000)
%plot(1:size(data,1), data(:,2)/10, 'r')