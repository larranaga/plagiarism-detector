package detector;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javafx.stage.FileChooser;

public class OpenFile {
	
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	ArrayList<File> files = new ArrayList<File>();
	public void PickMe() throws Exception{
		if( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			
			File file = fileChooser.getSelectedFile();
			
			files.add(file);
			Scanner input = new Scanner(file);
			
			while( input.hasNext()){
				sb.append(input.nextLine());
				sb.append("\n");
				
			}
			input.close();
			
		}
		else{
			sb.append("No se ha seleccionado");
		}
		
	}
	
	public ArrayList<File> getFiles(){
		return this.files;
	}
	
	
	
}
