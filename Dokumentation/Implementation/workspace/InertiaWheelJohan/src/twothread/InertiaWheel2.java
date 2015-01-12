package twothread;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;

public class InertiaWheel2 implements Runnable {

	static final long h= 5;
    // sleep can only take in long which is bounded, greater than zero
	//static final long h = 5;
	
	private static final double Ku = 0.004*2;
	
	static final double phi11 = 1.0012;
	static final double phi12 = 0.005;
	static final double phi21 = 0.4673;
	static final double phi22 = 1.0012;
	static final double phi33 = 1;
	
	static final double gamma1 = -0.000425;
	static final double gamma2 = -0.1667;
	static final double gamma3 = 6.6667;

	static final double l1 = -24.0034;
	static final double l2 = -2.6953;
	static final double l3 = -0.0219;

	static double u = 0.0;

	static int power =0;

	static double x1,x2,x3;

	static double x1Old =0.0;
	static double x2Old= 0.0;
	static double x3Old = 0.0;

	//For the acce meter
	//static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1);
	
	
	static MindsensorAbsoluteIMU acce_Meter = new MindsensorAbsoluteIMU(SensorPort.S1);
	
	static float[] sample_Acce = new float[3]; 
	static double angle_Acce;
	static double radians;
	
	// For the gyroscope
	static  double angle_Gyro=0;
	static double angleOffset=0;
	static double angle_of_set;
	static float[] sample = new float[1];
	static double offset_gyro=0;
	static double sample_1=0;
	
	// For the complementary filter
	static double comp_filter_angle=0;
	static double A = 0.95;
	
	//Wheel Speed
	static double speed_W=0;

	static NXTMotor m1;
	static NXTMotor m2;

	Boolean b;


	public InertiaWheel2(Boolean b){
		this.b=b;
	}



	public static void main(String args[]){

		LCD.drawString("Initialize Calibrate", 0, 1);

		Button.waitForAnyPress();

		calc_offset_gyro();

		LCD.clearDisplay();

		LCD.drawString("Press to start", 0, 1);

		Button.waitForAnyPress();

		LCD.clearDisplay();

		m1 = new NXTMotor(MotorPort.A);

		m2 = new NXTMotor(MotorPort.B);



		InertiaWheel2 c = new InertiaWheel2(true);
		InertiaWheel2 u = new InertiaWheel2(false);
		Thread t1 = new Thread(c);
		Thread t2 = new Thread(u);
		t1.setPriority(9);
		t2.setPriority(4);
		t1.start();
		t2.start();
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(b){
			acce_Calc();
		} else {
			try {
				power_calc();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void acce_Calc(){

		while(Button.ESCAPE.isUp()){
			get_Data_Acce();
			get_Data_Gyro();
			comp_filter_angle = (double) (A * (comp_filter_angle + sample_1 * 0.005) + (1-A)*(angle_Acce)) ;
			// Draw the result on the display 
			//dl.writeSample(sample_Acce[2]);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void get_Data_Acce(){

		acce_Meter.fetchSample(sample_Acce, 0);
		radians = Math.atan( ( sample_Acce[1]/10 ) / (sample_Acce[2]/10 ) );
		angle_Acce = Math.toDegrees(radians);
	}
	public static void get_Data_Gyro(){

		gyro.fetchSample(sample, 0);
		sample_1 = sample[0]  - offset_gyro; 
	}

	public static void calc_offset_gyro(){

		//LCD.drawString("Calibrate gyro", 0, 0);
		double temp_offset =0;
		int count = 100;
		for(int i =0;i<count ; i++){
			gyro.fetchSample(sample, 0);
			temp_offset = temp_offset + sample[0];
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		offset_gyro= temp_offset/count;

	}

	public static void power_calc() throws InterruptedException{

		long t = System.currentTimeMillis();
		double t2= 0;

		while(Button.ESCAPE.isUp()){

			// The current angle

			LCD.drawString(""+comp_filter_angle, 0, 6);

			/////////////////////////////////////////////////
			////////compute angles///////////////////////////
			/////from the complementary filters/////////////
			///////////////////////////////////////////////

			
			x1Old= comp_filter_angle; //==> from the complementary filters which is comp_filter_angle///// 
			
			x2Old = sample_1; //==> rate from the gyro - offset = sample_1 /////
	
			/////////////////////////////////////////////////////////
			///////Calculations of the wheel speed/////////////////
			/////////////////////////////////////////////////////
		
			 double radians_Wheel = m1.getTachoCount() *  ((Math.PI)/180);
			 
		     m1.resetTachoCount();
			 
			 double t1 = System.currentTimeMillis();
			
			  x3Old = (double) (radians_Wheel/(t1-t2)); // ==> speed of the wheel in radians /sec ==> x3Old
			
			/////////////////////////////////////////////////
			//////////////compute control laws/////////////
			////////////////////////////////////////////////

			u = -1*( (l1 * x1Old) +( l2 * x2Old) +( l3 * x3Old) ) ;
			LCD.drawString("tau "+u, 0, 5);
			
			LCD.drawString(""+x1Old, 0, 1);
			LCD.drawString(""+x2Old, 0, 2);
			LCD.drawString(""+x3Old, 0, 3);
			
			power = (int) ( (u) / Ku) ;

			if(power>100){
				power= 100;
			}

			if(power<-100){

				power= -100;
			}
			//LCD.drawInt(power, 0, 5);
			 m1.setPower(power);
			 m2.setPower(power);

			m2.forward();
			m1.backward();	

			LCD.clear(4);	
			LCD.drawString(""+power, 0, 4);
		    
		//	m1.resetTachoCount();
	
			t2 = System.currentTimeMillis();
			
			//PreCalculate the next States
			
			x1 = phi11*x1Old+ phi12*x2Old + gamma1*(u);
			x2 = phi21*x1Old+ phi22*x2Old + gamma2*(u);
			x3 = phi33*x3Old+ gamma3*(u);
			
			x1Old=x1;
			x2Old=x2;
			x3Old=x3;
			
			//LCD.clearDisplay();

			t=  t + h ;
			long duration= t - System.currentTimeMillis();
			
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			}
			//END
		     m1.close();
		     m2.close();
			 LCD.clear();
		     LCD.drawString("Program stopped", 0, 0);
		     
				
		}
}

