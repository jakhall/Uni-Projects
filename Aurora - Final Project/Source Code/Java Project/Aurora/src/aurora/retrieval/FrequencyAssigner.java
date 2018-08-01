package aurora.retrieval;
import java.util.ArrayList;

import aurora.model.WordStemmer;

/**
 * 
 * Frequency Assigner - Used to stem and group words, assigns each word a frequency.
 *
 */

public class FrequencyAssigner {
	
	
	public FrequencyAssigner(){

	}
	
	//Initiate - Will stem each word in the list, group them up into similar terms
	//adds the corresponding frequency of each, then returns a new list of updated text words.
	
	public ArrayList<TextWord> initiate(ArrayList<Word> w){
		int wordIndex;
		double scale;
		WordStemmer stemmer = new WordStemmer();
		ArrayList<TextWord> textWordList = new ArrayList<TextWord>();
		for(Word word : w){
			wordIndex = -1;
			word.setWord(stemmer.Stem(word.getWord()));
			for(TextWord textWord : textWordList){
				if(word.getWord().equals(textWord.getWord())){
					wordIndex = textWordList.indexOf(textWord);
				}
			}
			
			scale = 1;
					
			if(wordIndex != -1){
				
				textWordList.get(wordIndex).addTextFrequency(scale);
			} else {
				textWordList.add(new TextWord(word, scale));
			}
		}
		return textWordList;
	}
}
