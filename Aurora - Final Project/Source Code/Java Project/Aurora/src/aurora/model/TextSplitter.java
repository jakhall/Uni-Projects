package aurora.model;
import java.util.ArrayList;

/**
 * 
 * Text Splitter - Used to split the text up into individual terms.
 *
 */

public class TextSplitter {
	
	
	public TextSplitter(){
	}
	
	//splitText - creates and stores a new Word object every time it finds a space or "-" character.
	//then returns all words as an array list.
	
	public ArrayList<Word> splitText(String s){
		ArrayList<Word> wordList = new ArrayList<Word>();
		String[] wordArr = s.replaceAll("[^a-zA-Z -]", "").toLowerCase().split("\\s+");
		for(String word : wordArr){
			Word w = new Word(word);
			wordList.add(w);
		}
		
		return wordList;
	}
}
