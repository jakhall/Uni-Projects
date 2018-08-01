package aurora.model;

public class QueryMatch implements Comparable<QueryMatch> {

	private Query theQuery;
	private Document theDocument;
	private double similarity;
	

	QueryMatch(){
		
	}
	
	QueryMatch(Query q, Document d, double s){
		theQuery = q;
		theDocument = d;
		similarity = s;
	}
	
	QueryMatch(Query q, Document d){
		similarity = 0;
	}

	
	public Query getQuery() {
		return theQuery;
	}

	public void setQuery(Query theQuery) {
		this.theQuery = theQuery;
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
	public int compareTo(QueryMatch q) {
		return Double.compare(q.getSimilarity(), this.getSimilarity());
	}
	

}
