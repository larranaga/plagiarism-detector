
public class Guillermo {
	//magic
	public static int factoooorial(int numero){
		
		
		  if(numero == 0 || numero == 1){
			  
			  return 1;
			  
		  }
		  
		  return numero * factorial(numero-1);
	  }
	  public static void main(){
		  	System.out.println(factooooorial(2));
	  }
}
