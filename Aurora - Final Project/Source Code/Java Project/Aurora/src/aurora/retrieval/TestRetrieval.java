package aurora.retrieval;
import java.sql.*;
import java.util.ArrayList;

import aurora.model.DBConnection;
import aurora.model.Document;
import aurora.model.ProfileDatabase;
import aurora.model.TextParser;

public class TestRetrieval {
	
	
	private static ProfileDatabase db;
	private static VectorSpaceModel vsm;
	
	
	public static void main(String args[]) throws Exception{
		
		Connection conn = null;
		Statement stmt = null;
		
		conn = DBConnection.dbConnector();
		
		stmt = conn.createStatement();
		
		vsm = new VectorSpaceModel(conn);
		db = new ProfileDatabase(conn);
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		TextParser parser = new TextParser();
		
		CosineSimilarity cosine = new CosineSimilarity(vsm);
		
		//Load text files
		
		
		
		stringList.add(parser.read("resources/dogs.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/lizards.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/rabbits.txt").replaceAll("'", ""));
		
		
		
		stringList.add(parser.read("resources/1.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/2.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/3.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/4.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/5.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/6.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/7.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/8.txt").replaceAll("'", ""));
		stringList.add(parser.read("resources/9.txt").replaceAll("'", ""));
		
		
		
		ArrayList<Document> documents = new ArrayList<Document>();
		
		
		
		for(String string : stringList) {
			documents.add(new Document(string));
		}
		

		for(int i = 0; i < documents.size(); i++) { 
			documents.set(i, addDocument(documents.get(i)));
			
		}

		
		String query = "the lizards";
		cosine.setQuery(query);
		
		System.out.println("Querying VSM:");
		for(int i = 0; i < documents.size(); i++) {
			double similarity = cosine.equate(documents.get(i));
			System.out.println("Similarity to document " + i + ": " + similarity);
		}
		
	
		for(Document doc : documents){
			System.out.println("Removing document from vsm");
			vsm.removeDocument(doc);
			System.out.println("Removing document from database");
			db.removeDocument(doc.getID());
		}
		
	
		
		/**
		for(Document doc : documents) {
			vsm.removeDocument(doc);
		}
		
		for(Document doc : documents) {
			db.removeDocument(doc.getID());
		}
		**/
		
		/**
		String query = "energy batteries coffee lizards";
		
		for(int i = 415; i < 424; i++){
			Document doc = db.getDocument(i);
			double similarity = cosine.equate(doc, query);
			System.out.println("Doc: " + doc.getID() + " Similarity: " + similarity);
		}
		**/
	
		System.out.println("Complete, have a cookie!");
	}
	
	
	private static int getLastAdded(){
		String sql = "SELECT MAX(DocumentID) FROM DOCUMENT_Table";
		int index = 0;
		ResultSet rs = db.queryDatabase(sql);
		try {
			while(rs.next()) {
				index = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return index;
	}
	
	
	private static Document addDocument(Document doc){
		System.out.println("Adding Document to database");
		db.addNewDocument(doc);
		int index = getLastAdded();
		doc = db.getDocument(index);
		System.out.println("Adding Document to vsm");

		vsm.addDocument(doc);
		return doc;
	}
	
	
}