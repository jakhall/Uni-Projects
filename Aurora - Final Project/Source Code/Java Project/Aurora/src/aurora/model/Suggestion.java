package aurora.model;

/**
 * 
 * Suggestion - an object to store the similarity between a user and document vector.
 *
 */
public class Suggestion implements Comparable<Suggestion>{


	private Profile theUser;
	private Document theDocument;
	private double similarity;
	

	Suggestion(){
		
	}
	
	Suggestion(Profile p, Document d, double s){
		theUser = p;
		theDocument = d;
		similarity = s;
	}
	
	Suggestion(Query q, Document d){
		similarity = 0;
	}

	
	public Profile getUser() {
		return theUser;
	}

	public void setUser(Profile theUser) {
		this.theUser = theUser;
	}

	public Document getDocument() {
		return theDocument;
	}

	public void setDocument(Document theDocument) {
		this.theDocument = theDocument;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	@Override
	public int compareTo(Suggestion s) {
		return Double.compare(s.getSimilarity(), this.getSimilarity());
	}
	

}
