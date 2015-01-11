function [t var var_nbr status] = getvar(data,vname);
% 
% [T VAR] = GETVAR(DATA,VNAME)
%
% Retrieves a variable and the corresponding time vector
% from a data struct created by loaddym or loadplt.
%
% Johan Åkesson 2006

var_nbr = 0;

for i=1:size(data.name,1),
  
  if (strcmp(vname,strtrim(data.name(i,:))))
    var_nbr = i;
    break;
  end
  
  
end

if (var_nbr==0)
%  error('Variable not found');
   status = 0;
   t = -1;
   var = -1;
   return
end
status = 1;

mat_nbr = data.dataInfo(var_nbr,1);
col_nbr = data.dataInfo(var_nbr,2);

mat_name = strcat('data_',num2str(mat_nbr));

t = data.(mat_name)(:,1);
var = data.(mat_name)(:,col_nbr);