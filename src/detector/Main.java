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
	private final static int FILENUM = 1;
	private final static int IDTOKEN = 4;
	private final static int NUMBERTOKEN = 5;
	private final static int STRINGTOKEN = 6;
	
	public static Tokenizer tokenizer;
	
	public static HashMap<String, Integer> ids;
	public static BufferedReader reader;
	public static ArrayList<String> tokenizedFile;

	
	public static void main(String[] args) throws IOException {
		
		init();
		File file = new File("Input.java");   
        reader = new BufferedReader(new FileReader(file));
        
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
        	sb.append(line);
        	sb.append("\n");
        }
        reader.close();
        tokenizer.tokenize(tokenizer.removeComments(sb.toString()));
        prepareFile();
	}
	
	public static void prepareFile(){
		int ntoken = 0;
        for(Tokenizer.Token x: tokenizer.getTokens()){
        	if(x.token == IDTOKEN){
        		if(!ids.containsKey(x.sequence)){
        			ids.put(x.sequence, ntoken);
        			ntoken++;
        		}
        		tokenizedFile.add("id"+ids.get(x.sequence));
        	}
        	else if(x.token == NUMBERTOKEN){
        		tokenizedFile.add("number");
        	}
        	else{
        		tokenizedFile.add(x.sequence);
        	}
        }
	}
	
	public static void init(){
		tokenizer = new Tokenizer();
		ids = new HashMap<String, Integer>();
		tokenizedFile= new ArrayList<String>();
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
