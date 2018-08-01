package aurora.model;

import java.io.Serializable;

/**
 * 
 * Word - Stores the string object of a specific word in a document.
 *
 */

public class Word implements Serializable {
	
	private static final long serialVersionUID = 4134493568156659800L;
	
	protected String theWord;
	
	Word(){

	}
	
	protected Word(String s){
		theWord = s;
	}
	
	public void setWord(String s){
		theWord = s;
	}
	
	public String getWord(){
		return theWord;
	}
}
