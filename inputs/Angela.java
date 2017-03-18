public final class Angela {
	public static int f(int n){
		  if(n == 0 || n == 1){
			  return 1;
		  }
		  
		  return n * f(n-1);
	  }
	  public static void main(){
		  	System.out.println(f(2));
	  }}
}
