package aurora.model;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 
 * Text Parser - reads and returns text from a given text file location.
 *
 */

public class TextParser {
	
	String readText;
	
	public TextParser() {
		
	}
	
	//Read - reads in each line from a text file at the specified directory then
	//returns it as a string. 
	
	public String read(String dir) throws Exception {
		
		FileReader file = new FileReader(dir);
		BufferedReader reader = new BufferedReader(file);

		String text = ""; 
		
		String line = reader.readLine();
		
		while (line != null){
			text += line;
			line = reader.readLine();
		}
		
		return text;
	}
}
