package matcher;

public class Test {

	public static void main(String[] args) {
		SuffixArray sa = new SuffixArray("casa", "cabra");
		for(int i = 0; i < sa.T.length(); i++){
			System.out.printf("%d \t  %s \n", sa.SA[i], sa.T.substring(sa.SA[i]));
		}
		sa.longestCommonSubstring();

	}

}
