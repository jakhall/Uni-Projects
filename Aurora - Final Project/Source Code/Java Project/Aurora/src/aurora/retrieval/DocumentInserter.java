package aurora.retrieval;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import aurora.model.Document;

/**
 * 
 * Document Inserter - Calculates and updates the vector space model whenever a document is added.
 *
 */


public class DocumentInserter extends VSMUpdater {

	private ArrayList<Term> terms = new ArrayList<Term>();
	private int maxTermId;
	
	private ArrayList<String> vsmUpdates = new ArrayList<String>();
	private ArrayList<String> vsmInserts = new ArrayList<String>();
	private ArrayList<String> termInserts = new ArrayList<String>();
	

	protected DocumentInserter(Statement statement) {
		super(statement);
	}
	
	
	//updateTerms - updates the term list from the database. 
	
	private void updateTerms(){
		terms = getTerms();
	}
	
	
	//getTerms - returns all the terms within the TERM_Table as a list of Term objects.
	
	private ArrayList<Term> getTerms(){
		ArrayList<Term> allTerms = new ArrayList<Term>();
		
		String sql = "SELECT * FROM TERM_Table ORDER BY Term";
		ResultSet rs = queryDatabase(sql);
		try {
			while(rs.next()) {
				allTerms.add(new Term(rs.getString(2), rs.getInt(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allTerms;
	}

	//Add - adds the selected document to the vector space model, updates all of the affected rows.
	
	public void add(Document doc){
		
		updateTerms();
		ArrayList<Word> words = splitter.splitText(doc.getText());
		ArrayList<TextWord> textWords = assigner.initiate(words);
		
		String sql = "UPDATE DOCUMENT_Table SET WordCount=" + textWords.size() + " WHERE DocumentID=" + doc.getID();
		updateDatabase(sql);
		
		maxTermId = getMaxTermId();
		checkTerms(textWords, doc);
		
		batchVSMInsert();
		batchVSMUpdate();
		batchTermInsert();

	}
	
	//checkTerms - checks if a term is new to the VSM or must update the terms axis.
	
	private void checkTerms(ArrayList<TextWord> newTerms, Document doc) {
		
		int index;
		
		for(TextWord n : newTerms) {
			index = Collections.binarySearch(terms, n);
			if(index >= 0) {
				addToTermAxis(terms.get(index).getString(), n, doc);
			} else {
				int termId = addNewTerm(index, n);
				updateDocVector(doc.getID(), termId, n);
			}
		}
	}
	
	//addNewTerm - inserts the new term into the TERM_Table.
	
	private int addNewTerm(int index, TextWord n) {
		maxTermId++;
		terms.add(-(index+1), new Term(n.getWord(), maxTermId ));
		termInserts.add("(" + maxTermId + ", '" + n.getWord() + "')");
		return maxTermId;
	}
	
	//updateDocVector - recalculates the TFIDF value for the selected row in the VSM_Table.
	
	private void updateDocVector(int docId, int termId, TextWord newTerm){
		double termFreq = newTerm.getTextFrequency();
		vsmInserts.add("(" + termId + ", " + docId + ", " + termFreq + ", " + calculateTFIDF(termId, docId, termFreq, 1) + ")");
	}
	
	
	//addToTermAxis - updates the TFIDF of all VSM_Table rows which contain the affected term.
	
	private void addToTermAxis(String term, TextWord n, Document doc){
		int termId = getTermId(term);
		vsmInserts.add("(" + termId + ", " + doc.getID() + ", " + n.getTextFrequency() + ", " + calculateTFIDF(termId, doc.getID(), n.getTextFrequency(), 1) + ")");
		ArrayList<Integer> documents = getRelatedDocs(termId);
		for(int docId : documents) {
			double TFIDF = calculateTFIDF(termId, docId, getTermFrequency(termId, docId), 1);
			vsmUpdates.add("(" + termId + ", " + docId + ", " + getTermFrequency(termId, docId) + ", " + TFIDF + ")");
		}
		
	}

	
	//batchVSMUpdate - updates the VSM_Table all at once
	//by inserting all updated rows into a temporary table and checking for changes. 
	
	public void batchVSMUpdate() {
		if(vsmUpdates.size() > 0) {
		String sql = "INSERT INTO UPDATE_Table VALUES ";
		for(int i = 0; i < vsmUpdates.size() - 1; i++) {
			sql = sql + vsmUpdates.get(i) + ", ";
		}
		sql = sql + vsmUpdates.get(vsmUpdates.size() - 1);
		updateDatabase(sql);
		
		 sql = "UPDATE VSM_TABLE SET TFIDF = (SELECT TFIDF from UPDATE_Table WHERE TermID = VSM_TABLE.TermID AND DocumentID = VSM_Table.DocumentID) WHERE Exists (SELECT TermID From UPDATE_TABLE WHERE TermID = VSM_Table.TermID AND DocumentID = VSM_Table.DocumentID)";
		  updateDatabase(sql);
		  sql = "DELETE FROM UPDATE_Table";
		  updateDatabase(sql);
		  vsmUpdates = new ArrayList<String>();
		}
	}
	
	//batchVSMInsert - inserts all the new rows into the VSM_Table.
	
	public void batchVSMInsert() {
		if(vsmInserts.size() > 0) {
			String sql = "INSERT INTO VSM_Table VALUES ";
			for(int i = 0; i < vsmInserts.size() - 1; i++) {
				sql = sql + vsmInserts.get(i) + ", ";
			}
			sql = sql + vsmInserts.get(vsmInserts.size() - 1);
			updateDatabase(sql);
		}
		
		vsmInserts = new ArrayList<String>();
		
	}
	
	//batchVSMInsert - inserts all the new terms into the Term_Table.
	
	public void batchTermInsert() {
		
	
		if(termInserts.size() > 0) {
			String sql = "INSERT INTO TERM_Table VALUES ";
			for(int i = 0; i < termInserts.size() - 1; i++) {
				sql = sql + termInserts.get(i) + ", ";
			}
			sql = sql + termInserts.get(termInserts.size() - 1);
			updateDatabase(sql);
		}
		termInserts = new ArrayList<String>();
		
	}
	
	
	
}
