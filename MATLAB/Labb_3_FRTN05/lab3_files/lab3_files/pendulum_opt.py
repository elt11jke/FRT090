# Python script for solving the optimization problem in
# Laboratory Exercise 3 in FRTN05 Nonlinear Control and Servo systems
# using the software JModelica.org.

from pyjmi.common.io import ResultDymolaTextual
import numpy as np
import matplotlib.pyplot as plt
from pyjmi import transfer_optimization_problem

# Create compiler and compile the pendulum model
pend_opt = transfer_optimization_problem("pendulum", "pendulum.mop")

# Define number of elements in the discretization grid and 
# number of collocation points in each element
n_e = 150
n_cp = 3

# Initialize the optimization problem with the initial guess (stored in
# the file 'initial_guess.txt')
init_res = ResultDymolaTextual('initial_guess.txt')

# Solve the optimization problem
opts = pend_opt.optimize_options()
opts['IPOPT_options']['max_iter'] = 1000
opts['IPOPT_options']['linear_solver'] = "mumps"
opts['IPOPT_options']['tol'] = 1e-12
opts['n_e'] = n_e
opts['n_cp'] = n_cp
opts['init_traj'] = init_res
res = pend_opt.optimize(options=opts)
						
# Extract variable profiles and plot the results
a_ref = res['a_ref']
x_p = res['x_p']
y_p = res['y_p']
t = res['time']

plt.close(1)
plt.figure(1)
plt.plot(t,a_ref)
plt.grid()
plt.title('Input signal to process ($a_{ref}$)')
plt.xlabel('Time (s)')
plt.ylabel('Acceleration $m/s^2$')
        
plt.close(2)
plt.figure(2)
plt.plot(x_p,y_p)
plt.grid()
plt.title('Pendulum end-point path and obstacle')
plt.xlabel('x-coordinate (m)')
plt.ylabel('y-coordinate (m)')

x_obst = np.linspace(-0.35,-0.25)
y_obst = np.sqrt(1-((x_obst+0.3)/0.05)**2)*0.3 - 0.4

x_track = np.linspace(-1.45,0.1)
y_track = np.zeros(np.size(x_track))

plt.plot(x_obst,y_obst)
plt.plot(x_track,y_track,'--')

plt.show()
