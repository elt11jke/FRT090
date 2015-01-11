
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;

public class Kenan_Inertia_Wheel {


		static final long h= 10;
	    // sleep can only take in long which is bounded, greater than zero
		//static final long h = 5;
		
		private static final float Ku = 0.0042f;
		
		static final float phi11 = 1.0061f;
		static final float phi12 = 0.0100f;
		static final float phi21 = 1.2220f;
		static final float phi22 = 1.0061f;
		static final float phi33 = 1f;
		
		static final float gamma1 = -0.0003f;
		static final float gamma2 = -0.0677f;
		static final float gamma3 = 1.3976f;

		static final float l1 = -47.4379f;
		static final float l2 = -4.4343f;
		static final float l3 = -0.0240f;
		
		      
		
		static final float a = 1.214454545454f;
	
		
		

		static float u = 0.0f;

		static int power = 0;

		static float x1,x2,x3;

		static float x1Old =0.0f;
		static float x2Old= 0.0f;
		static float x3Old = 0.0f;
	
		//For the acce meter
		static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1); ;
		static float[] sample_Acce = new float[3]; 
		static double angle_Acce;
		static double radians;
		
		// For the gyroscope
		static  float angle_Gyro=0;
		static float angleOffset=0;
		static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
		static float angle_of_set;
		static float[] sample = new float[1];
		static float offset_gyro=0f;
		static float sample_1=0f;
		
		// For the complementary filter
		static float comp_filter_angle=0;
		static float A = 0.95f;
		static float count;
		
		//Wheel Speed
		static float speed_W=0;
		
		
		
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
			
			count=0;
			
			//INIT PROGRAM 
			DataLogger dl = new DataLogger("sample.txt"); 
		while(Button.ESCAPE.isUp()){
			dl.writeSample(1);
			// The current angle

//			LCD.drawString(""+comp_filter_angle, 0, 6);

			/////////////////////////////////////////////////
			////////compute angles///////////////////////////
			/////from the complementary filters/////////////
			///////////////////////////////////////////////
			
			acce_Meter.fetchSample(sample_Acce, 0);
			  radians = Math.atan( ( sample_Acce[1]/10 ) / (sample_Acce[2]/10 ) );
			  angle_Acce = Math.toDegrees(radians);

			  gyro.fetchSample(sample, 0);
				 sample_1 = sample[0]  - offset_gyro; 

			comp_filter_angle = (float) (A * (comp_filter_angle + sample_1 * 0.005) + (1-A)*(angle_Acce)) ;

			
			x1Old= comp_filter_angle; //==> from the complementary filters which is comp_filter_angle///// 
			
			x2Old = sample_1; //==> rate from the gyro - offset = sample_1 /////
	
			/////////////////////////////////////////////////////////
			///////Calculations of the wheel speed/////////////////
			/////////////////////////////////////////////////////
		
			 float radians_Wheel = (float) (m1.getTachoCount() *  ((Math.PI)/180));
			 m1.resetTachoCount();
			 
			  x3Old = (float) (radians_Wheel/(h*0.001)); // ==> speed of the wheel in radians /sec ==> x3Old
			
			/////////////////////////////////////////////////
			//////////////compute control laws/////////////
			////////////////////////////////////////////////

		     u = -1*( (l1 * x1Old) +( l2 * x2Old) +( l3 * x3Old) ) ;
			
			
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

        //	LCD.clear(4);	
		//  LCD.drawString(""+power, 0, 4);
		    
		//	m1.resetTachoCount();
	
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
		    m1.stop();
		    m2.stop();
		    m1.close();
		    m2.close();
			LCD.clear();
		    LCD.drawString("Program stopped", 0, 0);
		     
				
		}
	
		
		
		public static void calc_offset_gyro(){
			
			//LCD.drawString("Calibrate gyro", 0, 0);
			float temp_offset =0;
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

	