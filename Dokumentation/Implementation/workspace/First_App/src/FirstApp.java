
import lejos.hardware.BrickFinder; 
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
//import lejos.hardware.motor.BasicMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
//import lejos.hardware.motor.Motor;
//import lejos.hardware.motor.NXTMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.DCMotor;
import lejos.utility.Delay;

public class FirstApp {

	public static void main(String[] args) {
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		
		g.drawString("Hello world", 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
		
		//Delay.msDelay(5000);
		
		FirstApp m= new FirstApp();
		
		m.test();
		
		
		
	    
	
		
	}
	
	
	

	public void test(){

		NXTMotor m1 = new NXTMotor(MotorPort.A);
		//NXTMotor m1 = new NXTMotor(MotorPort.A);
		int power = 10;

		LCD.drawString("Program 1", 0, 0);

		while(Button.ESCAPE.isUp()){




			Button.waitForAnyPress();
			power+= 10;
			LCD.clear();
			m1.setPower(power);
			double t0 = System.currentTimeMillis(); 
			LCD.drawString("FORWARD " + power,0,0);
			Button.waitForAnyPress();

			// t0 =  System.currentTimeMillis() - t0; 
			m1.setPower(0); 
			Button.waitForAnyPress();
			m1.setPower(-20); 
			Button.waitForAnyPress();
			m1.setPower(0); 

			//	              System.out.println(m1);
			//	              
			//	              double speed= ((m1.getTachoCount()/t0)*(3.1416/180))*1000;
			//	              
			//	              LCD.drawString(Double.toString(t0), 0, 3);
			//	              LCD.drawInt(m1.getTachoCount(), 0, 4);	              
			//		          LCD.drawString(Double.toString(speed),0 ,5 );
			//	              
			//		          Button.waitForAnyPress();
			//		          LCD.drawString("BACKWARD",0,0);
			//		          
			//		          m1.setPower(-25); 
			//		          
			if (power==100){
				break;
			}

		}




	}

}
