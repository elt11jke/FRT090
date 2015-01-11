
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;




public class InertiaAngle {
	
	//For the acce meter
	static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1); ;
	static float sample_Acce[] = new float[3]; 
	static double angle_Acce;
	static double radians;
	
	// For the gyroscope
	static  double angle_Gyro=0;
	static float angleOffset=0;
	static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
	 static NXTMotor m1 = new NXTMotor(MotorPort.A);
	static float angle_of_set;
	static float[] sample = new float[1];
	static double offset_gyro=0;
	static double sample_1=0;
	
	// For the complementary filter
	static double comp_filter_angle=0;
	static double A = 0.95;
	
	  
	
	//static DataLogger dl = new DataLogger("complementary_filter_angle.txt"); 
	
	public static void main(String[] args) throws InterruptedException {
		
		//INIT PROGRAM
		LCD.drawString("Please push any buttom to begin", 0, 1);
		Button.waitForAnyPress();
		LCD.clearDisplay();
		//INIT PROGRAM ENDS
		 calc_offset_gyro();	
		 m1.setPower(100);
		 
		while(Button.DOWN.isUp()){
		// For the filter calculations
	   
		complementary_filter_calc();
		
		int i =0;

		while(i<1){
			 
          m1.backward();
		  Thread.sleep(100);
		   m1.setPower(0);
		   i++;	
		 }
		
		}
		//END
		 LCD.clear();
	     LCD.drawString("Program stopped", 0, 0);
	     Thread.sleep(2000);
			
	}
	public static void get_Data_Acce(){
		
			  acce_Meter.fetchSample(sample_Acce, 0);
			  radians = Math.acos( sample_Acce[2]/ 9.95 );
			  if(sample_Acce[2] <  -9.95 || sample_Acce[2] > 9.95		){
				  angle_Acce = 0;
			  }
			  else{
				  angle_Acce = Math.toDegrees(radians);
			  }
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
		
		LCD.clear();
		LCD.drawString("Done!", 0, 0);
		Button.waitForAnyPress();
		
	}
	
	
	public static void complementary_filter_calc() throws InterruptedException{
		
			get_Data_Acce();
			get_Data_Gyro();
			comp_filter_angle = A * (comp_filter_angle + sample_1 * 0.01) + (1-A)*(angle_Acce) ;
			// Draw the result on the display 
			LCD.drawString("Test Comple_Filter_Meter", 1, 1);
	    	LCD.drawString("Comp_filter_angle:", 0, 2);
	    	LCD.drawString(""+comp_filter_angle, 0, 3);
	    	//dl.writeSample(sample_Acce[2]);
	    	try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
		        System.out.println("FEL");
				e.printStackTrace();
			}
		}
	
}
