package tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class Tester {
	
	private final int FILENUM = 1;

	public static void main(String[] args) throws IOException {
		
		File file = new File(args[0]);
		
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.add("abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|superstrictfp|switch|synchronizedthis|throw|throws|transient|try|void|volatile|while	", 1);
		tokenizer.add("\\(", 2);
        tokenizer.add("\\{", 2);
        tokenizer.add("\\)", 2);
        tokenizer.add("\\}", 2);
        tokenizer.add("\\[", 2);
        tokenizer.add("\\]", 2);
        tokenizer.add("\\;", 2);
        tokenizer.add("\\:", 2);
        tokenizer.add("\\,", 2);
        tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", 3);
        tokenizer.add("[^A-Za-z]", 3);
        
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine()) != null){
        	sb.append(line);
        	sb.append("\n");
        }
        String lines[] = null;
        lines = removeComments(sb.toString()).split("\n");
        
	}
	
	
	
	public static String removeComments(String code) {
	  	   StringBuilder newCode = new StringBuilder();
	  	   try (StringReader sr = new StringReader(code)) {
	  	       boolean inBlockComment = false;
	  	       boolean inLineComment = false;
	  	       boolean out = true;

	  	       int prev = sr.read();
	  	       int cur;
	  	       for(cur = sr.read(); cur != -1; cur = sr.read()) {
	  	           if(inBlockComment) {
	  	               if (prev == '*' && cur == '/') {
	  	                   inBlockComment = false;
	  	                   out = false;
	  	               }
	  	           } else if (inLineComment) {
	  	               if (cur == '\r') { 
	  	                   sr.mark(1);
	  	                   int next = sr.read();
	  	                   if (next != '\n') {
	  	                       sr.reset();
	  	                   }
	  	                   inLineComment = false;
	  	                   out = false; 
	  	               } else if (cur == '\n') {
	  	                   inLineComment = false;
	  	                   out = false;
	  	               }
	  	           } else {
	  	               if (prev == '/' && cur == '*') {
	  	                   sr.mark(1); 
	  	                   int next = sr.read();
	  	                   if (next != '*') {
	  	                       inBlockComment = true; 
	  	                   }
	  	                   sr.reset(); 
	  	               } else if (prev == '/' && cur == '/') {
	  	                   inLineComment = true;
	  	               } else if (out){
	  	                   newCode.append((char)prev);
	  	               } else {
	  	                   out = true;
	  	               }
	  	           }
	  	           prev = cur;
	  	       }
	  	       if (prev != -1 && out && !inLineComment) {
	  	           newCode.append((char)prev);
	  	       }
	  	   } catch (IOException e) {
	  	       e.printStackTrace();
	  	   }
	  	   return newCode.toString();
	  	}

}
