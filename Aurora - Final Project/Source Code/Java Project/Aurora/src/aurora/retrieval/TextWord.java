package aurora.retrieval;

import aurora.model.Term;

/**
 * 
 * Text Word - An extension to the Word object that also stores the frequency of
 * the word in a given document/text.
 *
 */

public class TextWord extends Word {


	private static final long serialVersionUID = -6641304283149847456L;
	
	
	protected double textFrequency;
	
	
	TextWord(){
		super();
	}
	
	TextWord(String s) {
		super(s);
		textFrequency = 1;
	}
	
	TextWord(Word w) {
		super(w.getWord());
		textFrequency = 1;
	}
	
	TextWord(Word w, Double f) {
		super(w.getWord());
		textFrequency = f;
	}
	
	
	TextWord(TextWord tw) {
		super(tw.getWord());
		textFrequency = tw.getTextFrequency();
	}
	
	public void addTextFrequency(double f){
		textFrequency += f;
	}
	
	public double getTextFrequency(){
		return textFrequency;
	}


	public void setTextFrequency(double f){
		textFrequency = f;
	}

	public int compareTo(Term t) {
		return this.getWord().compareTo(t.getString());
	}

}
