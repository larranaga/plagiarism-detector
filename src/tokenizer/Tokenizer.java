package tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
	
	private class TokenInfo{
		public final Pattern regex;
		public final int token;
		
		public TokenInfo(Pattern regex, int token) {
			this.regex = regex;
			this.token = token;
		}
	}
	
	public class Token{
		public final int token;
		public final String sequence;
		public Token(int token, String sequence){
			this.token = token;
			this.sequence = sequence;
		}
	}
	
	private LinkedList<Token> tokens;
	private LinkedList<TokenInfo> tokenInfos;
	
	public Tokenizer() {
		tokenInfos = new LinkedList<TokenInfo>();
		tokens = new LinkedList<Token>();
	}
	
	public void add(String regex, int token){
		tokenInfos.add( new TokenInfo(  Pattern.compile("^("+regex+")"), token) );
	}
	
	public void tokenize(String str){
		String s = str.trim();
		tokens.clear();
		while(!s.equals("")){
			boolean match = false;
			for(TokenInfo info: tokenInfos){
				Matcher m = info.regex.matcher(s);
				if(m.find()){
					match = true;
					String tok = m.group().trim();
					s = m.replaceFirst("").trim();
					tokens.add(new Token(info.token, tok));
					break;
				}
			}
			if(!match) throw new ParseException("Unexpected character in input: " + s);
		}
	}
	
	public String removeComments(String code) {
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
	
	public LinkedList<Token> getTokens(){
		return tokens;
	}
	
	public void clearTokens(){
		this.tokens.clear();
	}
	
}
