

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.sensor.MindsensorsAbsoluteIMU;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;
import lejos.robotics.SampleProvider;

public class RealCodeInertia2015 {


		static final long h= 30;
	    // sleep can only take in long which is bounded, greater than zero
		//static final long h = 5;
		
		private static final float Ku = 0.0042f*((1/0.05f));
		
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
		static MindsensorsAbsoluteIMU sensor = new MindsensorsAbsoluteIMU(SensorPort.S1);
		static SampleProvider AcceMode;
		static SampleProvider RateMode;
		static List<Integer> acceTime = new ArrayList<Integer>();
		static List<Integer> gyroTime = new ArrayList<Integer>();
		static DataLogger dl;
		
		
		static float[] sample_Acce = new float[3]; 
		static double rad_Acce;
		static double radians;
		
		// For the gyroscope
		static  float angle_Gyro=0;
		static float angleOffset=0;
		//static HiTechnicGyro gyro =new HiTechnicGyro(SensorPort.S2 ); 
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
		
		
		
		
		
		public static void main(String[] args) throws InterruptedException{
			AcceMode= sensor.getAccelerationMode();
			RateMode= sensor.getRateMode();
			//INIT PROGRAM
	
			LCD.drawString("Initialize Calibrate", 0, 1);
			
			Button.waitForAnyPress();
			
			calc_offset_gyro();
			calc_offset_acce();
			
			LCD.clearDisplay();
			
			LCD.drawString("Press to start", 0, 1);
			
			Button.waitForAnyPress();
			
			LCD.clearDisplay();
			
			NXTMotor m1 = new NXTMotor(MotorPort.A);		
		    NXTMotor m2 = new NXTMotor(MotorPort.B);
		    
			count=0;
			
			//INIT PROGRAM 
			DataLogger dl = new DataLogger("sample.txt"); 
			
			//SampleProvider accelerationProvider= accelerationSensor.getAccelerationMode();
			


			long t = System.currentTimeMillis();
			
			long t_before;
			
		while(Button.ESCAPE.isUp()){
			
			time = System.currentTimeMillis();
		
	

			/////////////////////////////////////////////////
			////////compute angles///////////////////////////
			/////from the complementary filters/////////////
			///////////////////////////////////////////////
			
			
			 t_before = System.currentTimeMillis();
			 
				float[] sampleAcce = new float[AcceMode.sampleSize()];
				
				AcceMode.fetchSample(sampleAcce, 0);
			
			   

				
			  rad_Acce =  (sampleAcce[0]/9.81)- offset_acce ;
			  listAcce.add( (int) (sampleAcce[1] * 1000f)  ) ;  // TODO Needs to be converted back in the plot script
			  //double rad_Acce = Math.toRadians(angle_Acce);
			  //long timeAccel = System.currentTimeMillis() - t_before;
			  
			  
			  
			  // The gyro time
			 // t_before = System.currentTimeMillis();

				float[] sampleGyro= new float[RateMode.sampleSize()];	
				
				RateMode.fetchSample(sampleGyro,0);
		      sample_1 = sampleGyro[0]  - offset_gyro; 
		      double rad_gyro = Math.toRadians(sample_1);
		      
		      listGyro.add((int) ( sample_1*(1000f) ) );   	// TODO Needs to be converted back in the plot script
		      
		      
		      //long timeGyro =System.currentTimeMillis()-t_before ;
		      
			
		      
		      // The complementary filter
		      //t_before = System.currentTimeMillis();
			  comp_filter_angle = (float) (A * (comp_filter_angle + rad_gyro * 0.04) + (1-A)*(rad_Acce)) ;
			  listComp.add((int)  (   comp_filter_angle * (1000f)) ); // TODO Needs to be converted back in the plot script
	
			x1Old= comp_filter_angle; //==> from the complementary filters which is comp_filter_angle///// 
			
			x2Old = sample_1; //==> rate from the gyro - offset = sample_1 /////
			
			
			//LCD.drawString("Angle", 0, 0);
			//LCD.drawString("Ang vel", 1, 0);
			
			
		
			/////////////////////////////////////////////////////////
			///////Calculations of the wheel speed/////////////////
			/////////////////////////////////////////////////////
			
			 t_before = System.currentTimeMillis();
			 float radians_Wheel = (float) (m1.getTachoCount() *  ((Math.PI)/180));
			 //m1.resetTachoCount();
			 long time_wheel = System.currentTimeMillis()- t_before;
			  x3Old = (float) (radians_Wheel/(0.04)); // ==> speed of the wheel in radians /sec ==> x3Old
			listWheel.add((int)(x3Old));
			/////////////////////////////////////////////////
			//////////////compute control laws/////////////
			////////////////////////////////////////////////
			  
		     u = -1*( (l1 * x1Old) +( l2 * x2Old) +( l3 * x3Old) ) ;
		     listControl.add((int) ( u* (1000f)  ) ); // TODO Needs to be converted back in the plot script
		
			
			power = (int) ( (u) / Ku) ;
			if(power>100){
				power= 100;
			}

			if(power<-100){

				power= -100;
			}
			 // LCD.drawString(Integer.toString(m1.getTachoCount()), 0, 0);
				
			
			 t_before = System.currentTimeMillis();
			 m1.setPower(power);
			 m2.setPower(power);
			

			m2.backward();
			m1.backward();	
			long set_power_time =System.currentTimeMillis();
			
      
	
			//PreCalculate the next States
			
			x1 = phi11*x1Old+ phi12*x2Old + gamma1*(u);
			x2 = phi21*x1Old+ phi22*x2Old + gamma2*(u);
			x3 = phi33*x3Old+ gamma3*(u);
			
			x1Old=x1;
			x2Old=x2;
			x3Old=x3;
			long prec_states_time = System.currentTimeMillis()- set_power_time ;
			
			
            list_gyro_angle.add((int) (comp_filter_angle*(1000f)) );  // TODO Needs to be converted back in the plot script
			
		

			t=  t + h ;
			long duration= t - System.currentTimeMillis();
			
			if (duration > 0) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			
			
			long time2 = System.currentTimeMillis() - time ;
			
			
			listGlobalTime.add((int)time2);
			
			
			
			//listTimeGyro.add((int)timeGyro);
			
		//	listTimeComp.add((int)timeComp);
			
	//		listPrecStates.add((int)prec_states_time);
			
			
			
			//Button.waitForAnyPress();
			}
		
			m1.setPower(0);
			m2.setPower(0);
		
		   for(int a = 0; a < listGlobalTime.size()  ; a++){
			   
		    dl.writeSingleSample(listGlobalTime.get(a));
		    //dl.writeSingleSample(list_gyro_angle.get(a));
		     //dl.writeSingleSample(listTimeAcce.get(a));
		    //dl.writeSingleSample(listTimeGyro.get(a));
		    //dl.writeSingleSample(listTimeComp.get(a));
		    //dl.writeSingleSample(listPrecStates.get(a));
		    dl.writeSingleSample(listAcce.get(a));
		    dl.writeSingleSample(listGyro.get(a));
		    dl.writeSingleSample(listControl.get(a));
		    dl.writeSingleSample(listComp.get(a));
		     //dl.writeSingleSample(listWheel.get(a));
		     
		     if(listoffset.size() > a  ){
		    	 dl.writeSingleSample(listoffset.get(a));
		    	
		    	 
		     }else{
		    	 dl.writeSingleSample(0);
		    	
		     }
		     
		     if(listoffset_acce.size() > a  ){
		    	
		    	 dl.writeSingleSample(listoffset_acce.get(a));
		    	 
		     }else{
		    	 dl.writeSingleSample(0);
		    	 
		     }
		    
		    //dl.newLine();
		    
		   //}
		   
		   // write the offset in the end
		   dl.newLine();
		   }
		   dl.newLine();
		   dl.writeSingleSample((int) (offset_gyro*(1000f))  );		// TODO Needs to be converted back in the plot script
		   dl.newLine();
		   dl.writeSingleSample((int) (offset_acce*(1000f))  );
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
				float[] sampleGyro= new float[RateMode.sampleSize()];	
				
				RateMode.fetchSample(sampleGyro,0);
				  listoffset.add((int) (sampleGyro[0] *(1000f) ) ); // TODO Needs to be converted back in the plot script
				  temp_offset = temp_offset + sampleGyro[0];
				  try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				  }
			
			offset_gyro= temp_offset/count;
			
		}
		
public static void calc_offset_acce(){
			
			
			//float[] sample_acce =  new float[3]; 

			float temp_offset = 0;
			int count = 100;
			for(int i =0;i<count ; i++){
				float[] sampleAcce = new float[AcceMode.sampleSize()];
				
				AcceMode.fetchSample(sampleAcce, 0);
				  temp_offset = temp_offset + sampleAcce[0];
				  int sample_acce_temp_write=(int) (sampleAcce[1] * 1000f);
				  listoffset_acce.add( (sample_acce_temp_write) ); // TODO Needs to be converted back in the plot script
				  LCD.drawString(Float.toString(temp_offset), 0, 0);
				  try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				  }
			offset_acce = temp_offset/count;
			
		}
	}

	
