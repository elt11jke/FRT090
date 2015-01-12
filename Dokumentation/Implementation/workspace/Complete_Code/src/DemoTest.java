
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;

public class DemoTest {


		static final long h= 50;
	    // sleep can only take in long which is bounded, greater than zero
		//static final long h = 5;
		
		private static final double Ku = 0.004*2;
		
		static final double phi11 = 1.1433;
		static final double phi12 = 0.1047;
		static final double phi21 = 2.9329;
		static final double phi22 = 1.1433;
		static final double phi33 = 1;
		static final double phi43 = 0.1;
		static final double phi44 = 1;
		
		static double gamma1 = -0.0346;
		static double gamma2 = -0.7073;
		static double gamma3 = 13.9762;
		static double gamma4 = 0.6988;
		
		static final double gamma11 = -0.0346;
		static final double gamma12 = -0.7073;
		static final double gamma13 = 13.9762;
		static final double gamma14 = 0.6988;
		
		static final double gamma21 = -0.1708;
		static final double gamma22 = -3.4947;
		static final double gamma23 = 0;
		static final double gamma24 = 0;

		static final double l1 = -15.2447;
		static final double l2 = -2.8808;
		static final double l3 = -0.0526;
		static final double l4 = -0.0722;
		
		//static final double l1 = -4.4379;
		//static final double l2 = -40.4343;
		
		static final float a = 1.214454545454f;

		
		
//		static final float phi11 = 1.0;
//		static final float phi12 = 0.0100;
//		static final float phi21 = 1.2220;
//		static final float phi22 = 1.0061;
//		static final float phi33 = 1;
//		
//		static final float gamma1 = -0.0003;
//		static final float gamma2 = -0.0677;
//		static final float gamma3 = 1.3976;
//
//		static final float l1 = -238.2744;
//		static final float l2 = -21.7795;
//		static final float l3 = -0.4382;
	
		
		

		static double u = 0.0;

		static int power =0;

		static double x1,x2,x3,x4;

		static double x1Old =0.0;
		static double x2Old= 0.0;
		static double x3Old = 0.0;
		static double x4Old = 0.0;
	
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
		static double A = 0.95;
		static int count;
		
		//Wheel Speed
		static double speed_W=0;
		
		
		
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
			count=0;
			
			//INIT PROGRAM 
			DataLogger dl = new DataLogger("sample.txt"); 
		while(Button.ESCAPE.isUp()){

			// The current angle

//			LCD.drawString(""+comp_filter_angle, 0, 6);

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
			
			  x4Old = (double) radians_Wheel;
			  x3Old = (double) (radians_Wheel/(t1-t2)); // ==> speed of the wheel in radians /sec ==> x3Old
			
			/////////////////////////////////////////////////
			//////////////compute control laws/////////////
			////////////////////////////////////////////////


			u = -1*( (l1 * x1Old) +( l2 * x2Old) +( l3 * x3Old)+(l4 * x4Old) ) ;

				  

			
//			LCD.drawString("tau "+u, 0, 5);
			
			//LCD.drawString(""+x1Old, 0, 1);
			//LCD.drawString(""+x2Old, 0, 2);
			//LCD.drawString(""+x3Old, 0, 3);
			
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

			m1.forward();
			m2.backward();	

//			LCD.clear(4);	
//			LCD.drawString(""+power, 0, 4);
		    
		//	m1.resetTachoCount();
	
			t2 = System.currentTimeMillis();
			
			//PreCalculate the next States
			
			
			if((x1Old<0.05 & x1Old>0) | (x1Old>-0.05 & x1Old<0)){
				gamma1=gamma21;
				gamma2=gamma22;
				gamma3=gamma23;
				gamma4=gamma24;
			} else{
				gamma1=gamma11;
				gamma2=gamma12;
				gamma3=gamma13;
				gamma4=gamma14;
			}
			
			x1 = phi11*x1Old+ phi12*x2Old + gamma1*(u);
			x2 = phi21*x1Old+ phi22*x2Old + gamma2*(u);
			x3 = phi33*x3Old+ gamma3*(u);
			x4 = phi43*x3Old+ phi44*x4Old + gamma4*(u);
			
			
			x1Old=x1;
			x2Old=x2;
			x3Old=x3;
			x4Old=x4;
			
			
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
			
			dl.writeSample(1);
			
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

	
	
	