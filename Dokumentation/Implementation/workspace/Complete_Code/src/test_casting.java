
public class test_casting {

	static float i;
	static int j;
	static int j2;
	static float tempi;
	
	public static void main(String[] args){
		
		i = 0.05f;
		
		j = (int) (i*100);
		tempi= i*10;
		System.out.println("det blev " + j);
	}
}
