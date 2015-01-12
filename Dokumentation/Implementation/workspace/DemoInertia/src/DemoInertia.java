

import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;

public class DemoInertia {


		static final long h= 5;
	    // sleep can only take in long which is bounded, greater than zero
		//static final long h = 5;
		
		private static final double Ku = 0.004*2;
		
		static final double phi11 =1.1266;
		static final double phi12 = 0.0781;
		static final double phi21 = 3.4454;
		static final double phi22 = 1.1266;
		static final double phi33 = 1;
		
		static final double gamma1 = -0.0455;
		static final double gamma2 = -1.2381;
		static final double gamma3 = 10.4822;

		static final double l1 = -9.8256;
		static final double l2 = -1.5094;
		static final double l3 = -0.0441;

		static double u = 0.0;

		static int power =0;

		static double x1,x2,x3;

		static double x1Old =0.0;
		static double x2Old= 0.0;
		static double x3Old = 0.0;
	
		//For the acce meter
		static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1); ;
		static float[] sample_Acce = new float[3]; 
		static double angle_Acce;
		static double radians;
		
		// For the gyroscope
		static  double angle_Gyro=0;
		static double angleOffset=0;
		static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
		static double angle_of_set;
		static float[] sample = new float[1];
		static double offset_gyro=0;
		static double sample_1=0;
		
		// For the complementary filter
		static double comp_filter_angle=0;
		static double A = 1;
		
		//Wheel Speed
		static double speed_W=0;
		
		//static DataLogger dl = new DataLogger("complementary_filter_angle.txt"); 
		
		public static void main(String[] args) throws InterruptedException{
			
			//INIT PROGRAM
	
			LCD.drawString("Initialize Calibrate", 0, 1);
			
			Button.waitForAnyPress();
			
			calc_offset_gyro();
			
			LCD.clearDisplay();
			
			LCD.drawString("Press to start", 0, 1);
			
			Button.waitForAnyPress();
			
			LCD.clearDisplay();
			
			NXTMotor m1 = new NXTMotor(MotorPort.A);
				
		    NXTMotor m2 = new NXTMotor(MotorPort.B);
		    
		    long t = System.currentTimeMillis();
			double t2= 0;
			
			//INIT PROGRAM 
		while(Button.ESCAPE.isUp()){

			// The current angle


			/////////////////////////////////////////////////
			////////compute angles///////////////////////////
			/////from the complementary filters/////////////
			///////////////////////////////////////////////
			
			get_Data_Acce();
			get_Data_Gyro();

			comp_filter_angle = (double) (A * (comp_filter_angle + sample_1 * 0.005) + (1-A)*(angle_Acce)) ;

			
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
		
	
		

	}

	