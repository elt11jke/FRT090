import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.Button;




public class Comp_Filter {
	

	static final long h= 10;
	
	//For the acce meter
	static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1); ;
	static float sample_Acce[] = new float[3]; 
	static double angle_Acce;
	static double radians;
	
	static float angle_gyroscope_extra=0;
	
	// For the gyroscope
	static  double angle_Gyro=0;
	static float angleOffset=0;
	static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
	static float angle_of_set;
	static float[] sample = new float[1];
	static double offset_gyro=0;
	static double sample_1=0;
	
	// For the complementary filter
	static double comp_filter_angle=0;
	static double A = 0.95;
	
	static float angle_gyro=0;
	
	
	
	
	  
	
	
	
	
	
	static DataLogger dl_1 = new DataLogger("acce_angle.txt"); 
	static DataLogger dl_2 = new DataLogger("gyroscope_angle.txt"); 
	static DataLogger dl_3 = new DataLogger("gyroscope_angle_extra.txt"); 
	static DataLogger dl_4 = new DataLogger("complementary_filter_angle.txt"); 
	
	
	
	public static void main(String[] args) throws InterruptedException {
		
		//INIT PROGRAM
		LCD.drawString("Compl_Filter_Program_Test", 0, 0);
		LCD.drawString("Please push any buttom to begin", 0, 1);
		Button.waitForAnyPress();
		LCD.clearDisplay();
		//INIT PROGRAM ENDS

		// For the filter calculations
		complementary_filter_calc();
		//END
		 LCD.clear();
	     LCD.drawString("Program stopped", 0, 0);
	     Thread.sleep(2000);
			
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
		
			calc_offset_gyro();
			LCD.clearDisplay();
			LCD.drawString("calibrate done", 0, 2);
			LCD.drawString("Programmed started", 0, 5);
			
		long t =  System.currentTimeMillis();
		while (! Button.ESCAPE.isDown()){
			
			get_Data_Acce();
			get_Data_Gyro();
			angle_gyro  =  angle_gyro + (float) ( (float) sample_1 * 0.01) ;
			comp_filter_angle = A * (comp_filter_angle + sample_1 * 0.01) + (1-A)*(angle_Acce) ;
			
			angle_gyroscope_extra = (float) (comp_filter_angle + sample_1 * 0.01) ;
	    	dl_1.writeSample((float) angle_Acce);
	    	dl_2.writeSample((float) angle_gyro);
	    	
	    	dl_3.writeSample(angle_gyroscope_extra);
	    	dl_4.writeSample((float) comp_filter_angle);
	    	
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
		LCD.drawString("Programmed stopped", 0, 3);
		
	}

}
