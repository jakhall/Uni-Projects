package aurora.retrieval;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import aurora.model.Document;
import aurora.model.Profile;


/**
 * 
 * Vector Space Model - A controller that handles all documents, profiles and queries being added/updated/removed.
 *
 */

public class VectorSpaceModel {
	
	private Connection conn;
	private Statement stmt;
	
	private DocumentRemover remover;
	private DocumentInserter inserter;
	private ProfileInserter profiler;
	private VSMQuery vsmQuery;

	//stores the connection and passes it to the updater objects. 
	
	public VectorSpaceModel(Connection c){
			conn = c;
			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			inserter = new DocumentInserter(stmt);
			remover = new DocumentRemover(stmt);
			profiler = new ProfileInserter(stmt);
			vsmQuery = new VSMQuery(stmt);
			
		}
	
	
	//addDocument - Adds a new document to the VSM.
	
	public void addDocument(Document doc) {
		inserter.add(doc);
	}
	
	
	//viewDocument - informs the profiler that a document has been viewed.
	
	public void viewDocument(Document doc, Profile user) {
		profiler.view(doc, user);
	}
	
	//removeDocument - Removes a selected document from the VSM.
	
	public void removeDocument(Document doc) {
		remover.remove(doc);
	}
	
	
	//updateDocument - Removes then re-adds a document to the VSM.
	
	public void updateDocument(Document doc) {
		remover.remove(doc);
		inserter.add(doc);
	}
	
	
	//addUserDocument - Informs the profiler that a new document has been added.
	
	public void addUserDocument(Document doc, Profile user) {
		profiler.add(doc, user);
	}
	
	//getQueryVector - returns a string query as a VSM vector object.
	
	public double[] getQueryVector(String query) {
		return vsmQuery.calculate(query);
	}
	

	//getDocumentVector - returns a document as a VSM vector object.
	
	public double[] getDocumentVector(Document doc) {
		return vsmQuery.getDocumentVector(doc.getID());
	}
	
	
	//getProfileVector - returns a profile as a VSM vector object.
	
	public double[] getProfileVector(Profile user) {
		return vsmQuery.getProfileVector(user.getUserID());
	}

	
	
}
