import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
//import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;



public class Gyroscope {
	
	// Global variables
	   // HiTechnic sampel period is approx. 300 sampel/1 sek
	  static  float angle_Gyro=0;
	  static float angleOffset=0;
	  static HiTechnicGyro gyro;
	  static float angle_of_set;
	  static DataLogger dl = new DataLogger("Data_Gyro.txt");
	  static float[] sample = new float[1];
	  
	  
	public static void main(String[] args) throws InterruptedException {
		
	
		   LCD.drawString("Gyroscope_Program_Test", 0, 0);
		
		   Button.waitForAnyPress();
	        gyro = new HiTechnicGyro(SensorPort.S2 );
	       
	  
	     
	
	       get_Data_Gyro();
	       dl.close();
	       LCD.clear();
	       LCD.drawString("Program stopped", 0, 0);
	       Thread.sleep(2000);
		
		
	}
	
	public static void get_Data_Gyro(){
		while (! Button.ESCAPE.isDown())
	       {		   
	           
	    	   gyro.fetchSample(sample, 0);
	    	   
	    	   //calibrate the sample, deal with the offset assuming it is constant
	    	   sample[0] = sample[0];
	    	  angle_Gyro += (sample[0] * 0.003333333 ) ;
	    	  
	    	   
	    	   
	    	   LCD.drawString("TestGyro", 3, 1);
	    	   LCD.drawString("Angle:", 0, 3);
	    	   LCD.drawString(""+angle_Gyro, 10, 4);
	    	   
	    	   LCD.drawString("sample", 0, 5);
	    	   LCD.drawString(""+sample[0], 10, 6);
	           dl.writeSample(sample[0]);
	          
	       
	       }
	      
		
		
	}
		
	
	
	
	/* public static float calibrate(){
		 
		 float[] sampleCal = new float[1];
		 
		 for(int i=1; i<100; i++){
		 
			 gyro.fetchSample(sampleCal, 0);
		     angleOffset=  (angleOffset + sampleCal[0]);
	 }
		 
		 angleOffset= angleOffset/100;
		 
		 return angleOffset;
	 }
	 */

}
