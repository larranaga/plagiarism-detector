package detector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import matcher.*;
import tokenizer.*;

public class Main {
	
	private final static int IDTOKEN = 4;
	private final static int NUMBERTOKEN = 5;
	private final static int STRINGTOKEN = 6;
	
	public static Tokenizer tokenizer;
	
	public static HashMap<String, Integer> ids;
	public static BufferedReader reader;
	
	public static ArrayList<String> prepared;
	public static ArrayList<String> names;

	
	public static void main(String[] args) throws IOException {
		
		init();
		ArrayList<File> files = new ArrayList<File>();
		
		prepared = new ArrayList<String>();
		
		for(File file: files){
			reader = new BufferedReader(new FileReader(file));
	        
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while((line = reader.readLine()) != null){
	        	sb.append(line);
	        	sb.append("\n");
	        }
	        reader.close();
	        tokenizer.tokenize(tokenizer.removeComments(sb.toString()));
	        prepared.add(prepareFile());
	        names.add(file.getName());
	        tokenizer.clearTokens();
		}
		
		for(int i = 0; i < prepared.size(); i++){
			for(int j = i +1; j< prepared.size(); j++){
				double correlation = detectCopy(prepared.get(i), prepared.get(j));
				cout(correlation, i, j);
			}
		}
	}
	
	public static void cout(double correlation, int file1, int file2){
		System.out.printf("Los archivos %s y %s tienen una correlaciòn de %f % \n", names.get(file1), names.get(file2), correlation);
		if(correlation >= 79.0 ){
			System.out.println("detectado un posible plagio!!!!");
		}
	}
	
	public static double detectCopy(String T1, String T2){
		SuffixArray sa = new SuffixArray(T1, T2);
		int coincidence = sa.longestCommonSubstring();
		double percentage = coincidence * 100.0 / (double)(Math.max(T1.length(), T2.length()));
		return percentage;
	}
	
	public static String prepareFile(){
		String tokenizedFile = new String();
		int ntoken = 0;
		StringBuilder sb = new StringBuilder();
        for(Tokenizer.Token x: tokenizer.getTokens()){
        	if(x.token == IDTOKEN){
        		if(!ids.containsKey(x.sequence)){
        			ids.put(x.sequence, ntoken);
        			ntoken++;
        		}
        		sb.append("id"+ids.get(x.sequence));
        	}
        	else if(x.token == NUMBERTOKEN){
        		sb.append("number");
        	}
        	else if(x.token == STRINGTOKEN){
        		sb.append("string");
        	}
        	else{
        		sb.append(x.sequence);
        	}
        }
        tokenizedFile = sb.toString();
        return tokenizedFile;
	}
	
	public static void init(){
		tokenizer = new Tokenizer();
		ids = new HashMap<String, Integer>();
		tokenizer.add("abstract|assert|String|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|superstrictfp|switch|synchronizedthis|throw|throws|transient|try|void|volatile|while|System|out|println", 1);
		tokenizer.add("\\(", 2);
        tokenizer.add("\\{", 2);
        tokenizer.add("\\)", 2);
        tokenizer.add("\\}", 2);
        tokenizer.add("\\[", 2);
        tokenizer.add("\\]", 2);
        tokenizer.add("\\;", 2);
        tokenizer.add("\\:", 2);
        tokenizer.add("\\,", 2);
        tokenizer.add("\\=", 3);
        tokenizer.add("\\<", 3);
        tokenizer.add("\\+", 3);
        tokenizer.add("\\-", 3);
        tokenizer.add("\\.", 3);
        tokenizer.add("[0-9]+\\.?[0-9]*", NUMBERTOKEN);
        tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", IDTOKEN);
        tokenizer.add("[\"\'].*[\"\']", STRINGTOKEN);
	}
	
}
