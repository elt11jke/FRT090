import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

// import for the accelerometer
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;



public class Accelerometer {
	
	static HiTechnicAccelerometer acce_Meter;
	static DataLogger dl = new DataLogger("ev3_acce_Meter_data.txt"); 
	static float sample_Acce[] = new float[3]; 
	static double angle_Acce;
	static double radians;
	
	public static void main(String[] args) throws InterruptedException {
	// Define define the acce_Meter
		acce_Meter = new HiTechnicAccelerometer(SensorPort.S1);
		
		LCD.drawString("Acce_Meter_Program_Test", 0, 0);
		
		   Button.waitForAnyPress();
		
		get_Data_Acce();
		
	
		   dl.close();
	       LCD.clear();
	       LCD.drawString("Program stopped", 0, 0);
	       Thread.sleep(2000);
	
	}
	
	
	public static void get_Data_Acce(){
		while (! Button.ESCAPE.isDown()){
			  acce_Meter.fetchSample(sample_Acce, 0);
			
			  
			  // calc of the angle
			  
			  radians = Math.atan( ( sample_Acce[1]/10 ) / (sample_Acce[2]/10 ) );
			 
			  
			  angle_Acce = Math.toDegrees(radians);
			  
			   LCD.drawString("Test Acce_Meter", 3, 1);
	    	   LCD.drawString("Angle:", 0, 2);
	    	   LCD.drawString(""+angle_Acce, 8, 2);
	    	   
	    	   LCD.drawString("x", 0, 3);
	    	   LCD.drawString(""+sample_Acce[0], 8, 3);
	          
	           
	           LCD.drawString("y", 0, 4);
	    	   LCD.drawString(""+sample_Acce[1], 8, 4);
	           
	           
	           LCD.drawString("z", 0, 5);
	    	   LCD.drawString(""+sample_Acce[2], 8, 5);
	           dl.writeSample(sample_Acce[2]);
	        }
	}
	
}
