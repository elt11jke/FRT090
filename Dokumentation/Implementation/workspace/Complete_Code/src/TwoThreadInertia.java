

import java.util.ArrayList;
import java.util.List;







import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;
import lejos.robotics.SampleProvider;

public class TwoThreadInertia extends Thread {

	static MindsensorsAbsoluteIMU  sensor = new MindsensorsAbsoluteIMU(SensorPort.S1);
	static SampleProvider AcceMode;
	static SampleProvider RateMode;
	static List<Integer> acceTime = new ArrayList<Integer>();
	static List<Integer> gyroTime = new ArrayList<Integer>();
	static DataLogger dl;
	
		static final long h= 30;
	    // sleep can only take in long which is bounded, greater than zero
		//static final long h = 5;
		
		private static final float Ku = 0.0042f*((1/0.3f));
		
		static final float phi11 = 1.0192f;
		static final float phi12 = 0.0302f;
		static final float phi21 = 1.2844f;
		static final float phi22 = 1.0192f;
		static final float phi33 = 1f;
		
		static final float gamma1 = -0.0112f;
		static final float gamma2 = -0.7507f;
		static final float gamma3 = 7.8431f;

		static final float l1 = -13.7278f;
		static final float l2 = -2.1648f;
		static final float l3 = -0.0750f;
		
		      
		
		static final float a = 1.214454545454f;
	
		
		

		static float u = 0.0f;

		static int power = 0;

		static float x1,x2,x3;

		static float x1Old =0.0f;
		static float x2Old= 0.0f;
		static float x3Old = 0.0f;
	
		//For the acce meter
		
		static float[] sample_Acce = new float[3]; 
		static double rad_Acce;
		static double radians;
		
		// For the gyroscope
		static  float angle_Gyro=0;
		static float angleOffset=0;
		static float angle_of_set;
		static float[] sample = new float[1];
		static float offset_gyro=0f;
		static float offset_acce=0f;
		static float sample_1=0f;
		
		// For the complementary filter
		static float comp_filter_angle=0;
		static float A = 0.95f;
		static float count;
		
		//Wheel Speed
		static float speed_W=0;
		
		static long time;
		static long tAfterLogWrite;
		static long[] data;
		
		static List<Integer> listGlobalTime = new ArrayList<Integer>();
		static List<Integer> listTimeAcce = new ArrayList<Integer>();
		static List<Integer> listTimeGyro = new ArrayList<Integer>();
		static List<Integer> listTimeComp = new ArrayList<Integer>();
		static List<Integer> listPrecStates = new ArrayList<Integer>();
		static List<Integer> list_gyro_angle = new ArrayList<Integer>();
		
		
		// The values of acce_sample, gyro_sample, control signal, compl_filter, offset
		static List<Integer> listAcce = new ArrayList<Integer>();
		static List<Integer> listGyro = new ArrayList<Integer>();
		static List<Integer> listControl = new ArrayList<Integer>();
		static List<Integer> listComp = new ArrayList<Integer>();
		static List<Integer> listWheel = new ArrayList<Integer>();
		
		// The values of the offset
		
		static List<Integer> listoffset = new ArrayList<Integer>();
		static List<Integer> listoffset_acce = new ArrayList<Integer>();
		
		
		
		Boolean b;
		
		static NXTMotor m1;
		static NXTMotor m2;


		public TwoThreadInertia(Boolean b){
			this.b=b;
		}



		public static void main(String args[]) throws InterruptedException{
			
			LCD.drawString("Hej",0,0);
			
			Thread.sleep(2000);
			
			AcceMode= sensor.getAccelerationMode();
			RateMode= sensor.getRateMode();
			dl = new DataLogger("sample2t.txt"); 

			LCD.drawString("Initialize Calibrate", 0, 1);

			Button.waitForAnyPress();

			//calc_offset_gyro();

			LCD.clearDisplay();

			LCD.drawString("Press to start", 0, 1);

			Button.waitForAnyPress();

			LCD.clearDisplay();

			m1 = new NXTMotor(MotorPort.A);

			m2 = new NXTMotor(MotorPort.B);



			TwoThreadInertia c = new TwoThreadInertia(true);
			TwoThreadInertia u = new TwoThreadInertia(false);
			Thread t1 = new Thread(c);
			Thread t2 = new Thread(u);
			t1.setPriority(5);
			t2.setPriority(5);
			t1.start();
			t2.start();
			
			Thread.sleep(10000);
			
			int smallest_size = 0;
			
			int acce_Size= acceTime.size();		
			int gyro_Size= gyroTime.size();	
			
			if (acce_Size> gyro_Size){
				smallest_size= gyro_Size;
			} else {
				smallest_size= acce_Size;
			}
			
			for(int a = 0; a < smallest_size  ; a++){
				dl.writeSingleSample(acceTime.get(a));
				dl.writeSingleSample(gyroTime.get(a));
				dl.newLine();			
			}
		}



		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(b){
				acce_Calc();
				
			} else {
				try {
					gyro_Calc();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}

		public static void acce_Calc(){
			
			int t1=0;
			int t2=0;

			while(Button.ESCAPE.isUp()){
				
				t2=(int) System.currentTimeMillis();
				
				float[] sampleAcce = new float[AcceMode.sampleSize()];
				
				AcceMode.fetchSample(sampleAcce, 0);
				
				t1=(int) System.currentTimeMillis();
				acceTime.add( t1-t2 );
				
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




		

		public static void gyro_Calc() throws InterruptedException{

			int t1=0;
			int t2=0;

			while(Button.ESCAPE.isUp()){
				
				t2=(int) System.currentTimeMillis();
				
				float[] sampleGyro= new float[RateMode.sampleSize()];	
				
				RateMode.fetchSample(sampleGyro,0);
				
				t1=(int) System.currentTimeMillis();
				gyroTime.add( t1-t2 );

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		


	
		
		

	}
}

	
