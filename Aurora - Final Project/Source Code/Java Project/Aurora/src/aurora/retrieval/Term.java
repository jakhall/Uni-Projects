package aurora.retrieval;

/**
 * 
 * Term - An object to store the term word and the corresponding term weighting.
 *
 */


public class Term implements Comparable<Word> {
	
	private String termString;
	private int termId;
	
	public Term(String term, int id) {
		termString = term;
		termId = id;
	}

	public String getString() {
		return termString;
	}

	public void setString(String termString) {
		this.termString = termString;
	}

	public int getId() {
		return termId;
	}

	public void setId(int termId) {
		this.termId = termId;
	}

	@Override
	public int compareTo(Word w) {
		return this.getString().compareTo(w.getWord());
	}

	
	
}
