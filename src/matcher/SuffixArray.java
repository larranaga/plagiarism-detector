package matcher;

import java.util.Arrays;


public class SuffixArray {
	private final int max_n = 100050;
	private int n;
	private int m;
	public String T;
	private int[] rank, temp_rank;
	public int[] SA, temp_SA;	
	private int[] count; // para radix sort
	private int[] lcp;
	private int[] phi;
	private int[] plcp;
	
	private void countingSort(int k){
		int sum;
		int best = Math.max(300, n);
		Arrays.fill(count, 0);
		for(int i = 0; i < n; i++)
			count[i + k < n ? rank[i + k] : 0 ]++;
		for(int i = sum = 0; i < best; i++){
			int t = count[i];
			count[i] = sum;
			sum += t;
		}
		for(int i = 0; i < n; i++)
			temp_SA[ count[ SA[i] +  k  < n ? rank[ SA[i] + k ] : 0 ] ++ ] = SA[i];
		for(int i = 0; i < n; i++)
			SA[i] = temp_SA[i];
	}
	
	private void constructLcp(){
		this.phi[ SA[0]] = -1;
		int L = 0;
		for(int i = 1; i <n; i++)
			phi[SA[i]] = SA[i-1];
		for(int i = L = 0; i < n; i++){ 
			if(phi[i] == -1){
				this.plcp[i] = 0; continue;
			}
			while(this.T.charAt(i + L) == this.T.charAt(phi[i] + L)) L++;
			this.plcp[i] = L;
			L = Math.max(L-1, 0);
		}
		for(int i = 0; i < n; i ++)
			this.lcp[i] = this.plcp[SA[i]];
	}
	
	private int owner(int idx){return (idx < this.n - this.m -1) ? 1: 2;}
	
	public int longestCommonSubstring(){
		int maxLCP = -1, idx = 0;
		this.constructLcp();
		for(int i = 1; i <n; i++){
			if(this.lcp[i] > maxLCP && owner(SA[i]) != owner(SA[i-1]) ){
				maxLCP = this.lcp[i];
				idx = i;
			}
		}
		System.out.printf("\nThe LCS is '%s' with length = %d\n",
			      new String(T).substring(SA[idx], SA[idx] + maxLCP), maxLCP);
		return maxLCP;
	}
	
	public SuffixArray(String T1, String T2 ) {
		this.phi = new int[max_n];
		this.lcp = new int[max_n];
		this.rank = new int[max_n];
		this.temp_rank = new int[max_n];
		this.SA = new int[max_n];
		this.temp_SA = new int[max_n];
		this.count = new int[max_n];
		this.plcp = new int[max_n];
		
		this.T = new String(T1+"#"+T2+"$");
		
		this.n = T.length();
		this.m = T2.length();
		int r;
		
		for(int i = 0; i < n; i++) rank[i] = T.charAt(i);
		for(int i = 0; i < n; i++) SA[i] = i;
		for(int k = 1; k < n; k<<=1){

			this.countingSort(k);
			countingSort(0);
			temp_rank[SA[0]] = r = 0;
			for(int i = 1; i < n; i++){
				if(rank[ SA[i] ] == rank[SA[i-1]] && rank[SA[i] + k] == rank[SA[i - 1] + k]){
					temp_rank[SA[i]] = r;
				}
				else{
					temp_rank[SA[i]] = ++r;
				}
			}
			for(int i = 0; i <n; i++)
				rank[i] = temp_rank[i];
			if(rank[SA[n-1]] == n-1) break;
		}
	}

}
