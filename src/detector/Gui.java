package detector;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import matcher.SuffixArray;
import tokenizer.Tokenizer;

import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class Gui {
	OpenFile of = new OpenFile();
	private final static int IDTOKEN = 4;
	private final static int NUMBERTOKEN = 5;
	private final static int STRINGTOKEN = 6;
	
	public static Tokenizer tokenizer;
	
	public static HashMap<String, Integer> ids;
	public static BufferedReader reader;
	
	public static ArrayList<String> prepared;
	public static ArrayList<String> names;
	ArrayList<File> files;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		init();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	
	public static void cout(double correlation, int file1, int file2){
		System.out.printf("Los archivos "+ names.get(file1)+ " y "+ names.get(file2)+ " tienen una correlaciòn de %f porciento \n" , correlation);
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
	
	public static void init(){
		prepared = new ArrayList<String>();
		names = new ArrayList<String>();
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
        
        tokenizer.add("[^a-zA-Z]", 2);
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 555, 366);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 519, 220);
		frame.getContentPane().add(textArea);

		
		JButton btnNewButton = new JButton("Open File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try{
					of.PickMe();
					
					
				}catch (Exception e){
					e.printStackTrace();
					
				}
				textArea.setText("archivo abierto exitosamente");
			}
		});
		btnNewButton.setBounds(24, 263, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnShowFiles = new JButton("Tokenize");
		btnShowFiles.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				files = of.getFiles();
				for(File file: files){
					try {
						reader = new BufferedReader(new FileReader(file));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        
			        StringBuilder sb = new StringBuilder();
			        String line;
			        try {
						while((line = reader.readLine()) != null){
							sb.append(line);
							sb.append("\n");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        try {
						reader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        tokenizer.tokenize(tokenizer.removeComments(sb.toString()));
			        prepared.add(prepareFile());
			        names.add(file.getName());
			        tokenizer.clearTokens();
				}
				textArea.setText("Archivos tokenizados exitosamente");
			}
		});
		btnShowFiles.setBounds(205, 263, 89, 23);
		frame.getContentPane().add(btnShowFiles);
		
		JButton btnDetectPlagiarism = new JButton("Detect Plagiarism");
		btnDetectPlagiarism.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuilder ans = new StringBuilder();
				for(int i = 0; i < prepared.size(); i++){
					for(int j = i +1; j< prepared.size(); j++){
						double correlation = detectCopy(prepared.get(i), prepared.get(j));
						ans.append("Los archivos "+ names.get(i)+ " y "+ names.get(j)+ " tienen una correlacion de "+ correlation + " porciento \n");
						if(correlation >= 69.0 ){
							ans.append("detectado un posible plagio!!!! \n");
						}
						ans.append("\n");
					}
				}
				textArea.setText(ans.toString());
			}
		});
		btnDetectPlagiarism.setBounds(375, 263, 115, 23);
		frame.getContentPane().add(btnDetectPlagiarism);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(512, 11, 17, 220);
		frame.getContentPane().add(scrollBar);
		
		
	}
}
