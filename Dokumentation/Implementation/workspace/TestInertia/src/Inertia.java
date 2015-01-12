import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.Button;
import lejos.robotics.EncoderMotor;




public class Inertia {
	

	public static void main(String[] args) throws InterruptedException {
		
		//INIT PROGRAM
	
		//Button.waitForAnyPress();
		LCD.drawString("Test inertia", 0, 0);
	
        NXTMotor m1 = new NXTMotor(MotorPort.A);
		
		NXTMotor m2 = new NXTMotor(MotorPort.B);
		
		m1.setPower(100);
		m2.setPower(100);


		m1.backward();
		
		
		Thread.sleep(100);
		
		
		 
	
		//END
		
		m1.setPower(0);
		m2.setPower(0);
		
		
		 LCD.clear();
	     LCD.drawString("Program stopped", 0, 0);
	     Thread.sleep(2000);
			
	}

}