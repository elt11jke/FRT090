

import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;

public class Inertia_Wheel_Origin {


		static final float h= 1f;

		private static final float Ku = 0.0039f;

		static final float phi11 = 1.1191f;

		static final float phi12 = 0.0520f;

		static final float phi21 = 4.8554f;

		static final float phi22 = 1.1191f;

		static final float phi33 = 1f;

		static final float gamma1 = -0.0425f;

		static final float gamma2 = -1.7323f;

		static final float gamma3 = 66.6667f;

		static final float l1 = -125.7894f;

		static final float l2 = -13.0559f;

		static final float l3 = -0.1549f;

		static float u = 0.0f ;

		static int power =0;

		static float x1,x2,x3;

		static float x1Old =0.0f ;
		static float x2Old= 0.0f;
		static float x3Old = 0.0f;
	
		//For the acce meter
		static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1); ;
		static float sample_Acce[] = new float[3]; 
		static double angle_Acce;
		static double radians;
		
		// For the gyroscope
		static  double angle_Gyro=0;
		static float angleOffset=0;
		static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
		static float angle_of_set;
		static float[] sample = new float[1];
		static double offset_gyro=0;
		static double sample_1=0;
		
		// For the complementary filter
		static float comp_filter_angle=0f;
		static double A = 0.95;
		
		//Wheel Speed
		static float speed_W=0f;
		
		  
		
		
		
		
		
		//static DataLogger dl = new DataLogger("complementary_filter_angle.txt"); 
		
		public static void main(String[] args) throws InterruptedException {
			
			//INIT PROGRAM
			
			
			LCD.drawString("Compl_Filter_Program_Test", 0, 0);
			
			LCD.drawString("Please push any buttom to calibrate", 0, 1);
			
			Button.waitForAnyPress();
			
			calc_offset_gyro();
			
			LCD.drawString("Push any button to start", 0, 1);
			
			Button.waitForAnyPress();
			
			LCD.clearDisplay();
			
			NXTMotor m1 = new NXTMotor(MotorPort.A);
				
		    NXTMotor m2 = new NXTMotor(MotorPort.B);
		    
		    float t = System.currentTimeMillis();
			float t2= 0f;
			
			//INIT PROGRAM 
		while(Button.ESCAPE.isUp()){
			
			// For the filter calculations
			complementary_filter_calc();
			
			
			
			/////////////////////////////////////////////////
			////////compute angles///////////////////////////
			/////from the complementary filters/////////////
			///////////////////////////////////////////////

			x1Old= comp_filter_angle; //==> from the complementary filters which is comp_filter_angle///// 
			
			x2Old =(float) sample_1; //==> rate from the gyro - offset = sample_1 /////
			
			
			
			/////////////////////////////////////////////////////////
			
			///////Calculations of the wheel speed/////////////////
			
			/////////////////////////////////////////////////////
			
			
			 double radians_Wheel = m1.getTachoCount() *  ((Math.PI)/180);
			
			 double t1 = System.currentTimeMillis();
			
			  x3Old = (float) (radians_Wheel/(t1-t2)); // ==> speed of the wheel in radians /sec ==> x3Old
			
			/////////////////////////////////////////////////
			
			//////////////compute control laws/////////////
			
			////////////////////////////////////////////////

			u = -1*( (l1 * x1Old) +( l2 * x2Old) +( l3 * x3Old) ) ;
			power = (int) (Ku * u) ;
//
//			if(power<5 & power>0){
//				power += 5;
//			}
//
//			if(power>-5 & power<0){
//				power -= 5;
//			}

			if(power>100){

				power=100;

			}

			if(power<-100){

				power=-100;
			}
			 m1.setPower(power);

//			if(power>0){
//
				m1.forward();
				m2.backward();
//
//			}
//
//			if(power<0){
//			
		//		m1.backward();
//				m2.forward();
			
			
			
			m1.resetTachoCount();
			
			t2 = System.currentTimeMillis();
			
			//PreCalculate the next States
			
			x1 = phi11*x1Old+ phi12*x2Old + gamma1*(u);
			
			x2 = phi21*x1Old+ phi22*x2Old + gamma2*(u);
			
			x3 = phi33*x3Old+ gamma3*(u);
			
			x1Old=x1;
			
			x2Old=x2;
			
			x3Old=x3;
			
			t=t+h;
			
			Thread.sleep(1);
			
			
			
			}
			//END
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
			
			LCD.drawString("Calibrate gyro", 0, 0);
			Button.waitForAnyPress();
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
		
		
		public static void complementary_filter_calc(){
			
				
			
			//while (! Button.ESCAPE.isDown()){
				
				get_Data_Acce();
				get_Data_Gyro();
				comp_filter_angle = (float) (A * (comp_filter_angle + sample_1 * 0.01) + (1-A)*(angle_Acce)) ;
				// Draw the result on the display 
				LCD.drawString("Test Comple_Filter_Meter", 1, 1);
		    	LCD.drawString("Comp_filter_angle:", 0, 2);
		    	LCD.drawString(""+comp_filter_angle, 0, 3);
		    	//dl.writeSample(sample_Acce[2]);
		    	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//	}
		}

	}

	
	
	
