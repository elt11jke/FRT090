import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.sensor.I2CSensor;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import lejos.hardware.sensor.MindsensorsAccelerometer;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;




public class MySensor234{
	
	
	//static HiTechnicAccelerometer acce_Meter = new HiTechnicAccelerometer(SensorPort.S1); ;
	static List<Integer> listTime = new ArrayList<Integer>();
	static List<Integer> listAcce = new ArrayList<Integer>();
	static List<Integer> listGyro = new ArrayList<Integer>();
	//static List<Integer> listAcce3 = new ArrayList<Integer>();
	
	//static List<Integer> listGyro = new ArrayList<Integer>();
	
	
	//static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
	//static List<Float> listSample = new ArrayList<Float>();
	static DataLogger dl = new DataLogger("test.txt");
	static MindsensorsAbsoluteIMU  sensor = new MindsensorsAbsoluteIMU(SensorPort.S1);

	
	
	
	public static void main(String args[]){
			 
		// get a port instance
		//Port port = LocalEV3.get().getPort("S1");

		// Get an instance of the Ultrasonic EV3 sensor
		
		// get an instance of this sensor in measurement mode
		SampleProvider AcceMode= sensor.getAccelerationMode();
		SampleProvider RateMode=sensor.getRateMode();

		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sampleAcce = new float[AcceMode.sampleSize()];
		float[] sampleGyro= new float[RateMode.sampleSize()];		
		sensor.setGyroFilter(0);

		// fetch a sample
	  while(Button.ESCAPE.isUp()){	
		  
		long time = System.currentTimeMillis();
		
		
		AcceMode.fetchSample(sampleAcce, 0);
		
		RateMode.fetchSample(sampleGyro,0);
		
		 long time2 = System.currentTimeMillis() - time; 
		 
		
	     listTime.add((int)(time2 * 1000f )); 
	     listAcce.add((int) (sampleAcce[0] * 1000f)  );
	     listGyro.add((int) (sampleGyro[0] * 1000f)  );
	 
	  }
	   for(int a = 0; a < listTime.size()  ; a++){
		   
		    dl.writeSingleSample(listTime.get(a));
		   dl.writeSingleSample( (listAcce.get(a)   ) );	
		    dl.writeSingleSample( (listGyro.get(a)   ) );	
		  	
		    
		    dl.newLine();
		   
		
		   }
	  
	}
	
	
	
	
}