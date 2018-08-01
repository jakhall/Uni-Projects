package aurora.retrieval;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.collections4.CollectionUtils;

import aurora.model.Document;

public class DocumentUpdate {
	
	/**
	 
	private Statement stmt = null;
	private TextSplitter splitter;
	private FrequencyAssigner assigner;
	
	private ArrayList<String> vsmUpdates = new ArrayList<String>();
	private ArrayList<String> vsmInserts = new ArrayList<String>();
	private ArrayList<String> termInserts = new ArrayList<String>();
	
	public DocumentUpdate(Statement statement) {
		stmt = statement;
		splitter = new TextSplitter();
		assigner = new FrequencyAssigner();
	}
	
	
	private ResultSet queryDatabase(String sql) {
		
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}
	
	private boolean updateDatabase(String sql) {
		try {
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	private ArrayList<Term> getAllTerms() {
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
	
	
	private ArrayList<Term> getDocTerms(int docId) {
		
		Comparator<Term> c = new Comparator<Term>() {
		    public int compare(Term a, Term b) {
		        return a.getString().compareTo(b.getString());
		    };
		};
		
		ArrayList<Term> docTerms = new ArrayList<Term>();
		
		String sql = "SELECT TermID FROM VSM_Table WHERE DocumentID=" + docId;
		ResultSet rs = queryDatabase(sql);
		try {
			while(rs.next()) {
				docTerms.add(getTerm(rs.getInt(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Collections.sort(docTerms, c);
		
		return docTerms;
	}
	
	
	private Term getTerm(int termId) {
		String sql = "Select * FROM TERM_Table WHERE termId=" + termId;
		ResultSet rs = queryDatabase(sql);
		Term term = null;
		try {
			while(rs.next()) {
				term = new Term(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return term;
	}
	

	private int getTermFrequency(int termId, int docId) {
		String sql = "SELECT TF FROM VSM_Table WHERE TermID=" + termId + " AND DocumentID=" + docId;
		ResultSet rs = queryDatabase(sql);
		int freq = 0;
		
		freq = rs.getInt(1);
		
		return freq;
		
	}
	
	private int getDocFrequency(int termId) {
		String sql = "SELECT Count(*) FROM VSM_Table WHERE TermID=" + termId;
		ResultSet rs = queryDatabase(sql);
		int freq = 0;
		freq = rs.getInt(1);
		return freq;
	}
	
	public void updateDocument(Document doc){
		ArrayList<Word> splitWords = splitter.splitText(doc.getText());
		ArrayList<TextWord> textWords = assigner.initiate(splitWords);
		
		checkTerms(doc, textWords);
		String sql = "DELETE FROM VSM_Table WHERE NOT EXISTS(SELECT TermID FROM UPDATE_Table WHERE TermID = VSM_Table.TermID) AND DocumentID = 3";
		updateDatabase(sql);
		
	}
	
	
	private void checkTerms(Document doc, ArrayList<TextWord> newWords) {
		ArrayList<Term> allTerms = getAllTerms();
		ArrayList<Term> docTerms = getDocTerms(doc.getID());
		int index; 
		
		Collection changes = CollectionUtils.disjunction(docTerms, newWords);
		changes.
		
		for(TextWord word : newWords) {
			for(Term docTerm : docTerms) {
				if(docTerm.getString().equals(word.getWord())) {
					double docFreq = getTermFrequency(docTerm.getId(), doc.getID());
					if(docFreq != word.getTextFrequency()) {
						updateDocTerm(docId, termId, word.getTextFrequency());
					}
				} else {
					deleteDocTerm();
					if(getDocFrequency())
				}
			}
			
			
			index = Collections.binarySearch(docTerms, word);
			if(index >= 0) {
				double docFreq = getTermFrequency(docTerms.get(index).getId(), doc.getID());
				double newFreq = word.getTextFrequency();
				if(docFreq != newFreq) {
					updateTermAxis(docTerms.get(index).getId(), doc, newFreq);
				}
			} else {
				index = Collections.binarySearch(allTerms, word);
				if(index >= 0) {
					updateTermAxis(allTerms.get(index).getId(), doc, word.getTextFrequency());
						
					}
				}
				
				
			}
		}
	
		
		updateTermAxis(){
			
		}
		
		for(TextWord word : newWords) {
			
			if(index >= 0) {
				
			}
		
	}
	
	**/
	

}
