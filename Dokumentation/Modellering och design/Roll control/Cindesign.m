%Design av Regulatorn för inertia hjulet



%Parametrar Standard

%K=34.8082;
%Ti=42.4085;
%Td=2.5938;

%Cin = K*(1 + 1/(s*Ti) + Td*s);


%Parametrar efter tuning

Kt= 34.8082;
Tit= 42.4085;
Tdt= 2.5938;
N=328.9617;

Cin = Kt + Tit*(1/s) + Tdt*(N/(1+N*(1/s)));