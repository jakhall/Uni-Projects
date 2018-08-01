package aurora.model;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main {
	
	public static void main(String args[]) throws Exception{
		
		ArrayList<String> stringList = new ArrayList<String>();
		TextParser parser = new TextParser();
		Connection conn = null;
		Statement stmt = null;
		
		conn = DBConnection.dbConnector();
		
		stmt = conn.createStatement();
		
		String sql = "SELECT username FROM Test_Table";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		ArrayList<String> users = new ArrayList<String>();
		
		while(rs.next()) {
			users.add(rs.getString(1));
		}
		
		for(String username : users) {
		System.out.println(username);
		}
		
		//Load text files
		stringList.add(parser.read("resources/1.txt"));
		stringList.add(parser.read("resources/2.txt"));
		stringList.add(parser.read("resources/3.txt"));
		stringList.add(parser.read("resources/4.txt"));
		stringList.add(parser.read("resources/5.txt"));
		stringList.add(parser.read("resources/6.txt"));
		stringList.add(parser.read("resources/7.txt"));
		stringList.add(parser.read("resources/8.txt"));
		stringList.add(parser.read("resources/9.txt"));

		
		TextSplitter splitter = new TextSplitter();
		FrequencyAssigner assigner = new FrequencyAssigner(); 
		TextDatabase database = new TextDatabase();
		WordSorter sorter = new WordSorter();
		CosineSimilarity similarity = new CosineSimilarity();
		
		
		int k = 0;
		
		for(String string : stringList){
			
		//Split documents into word list
		ArrayList<Word> wordList = splitter.splitText(string);
		
		//Assign frequency of each word
		ArrayList<TextWord> textWordList = assigner.initiate(wordList);
		
		//Form document from word list
		Document document = new Document(string, textWordList);
		
		document.setTitle(String.valueOf(k));
		//Add the document to the database
		database.addDocument(document);
		
		k++;
		}
		
		//Calculate the document frequency of each word
		database.calculateDocFrequency();
		
		//Calculate the TFIDF of each word

		database.calculateTFIDF();

		ArrayList<Document> docList = database.getDocuments();
		
	
		ArrayList<DocumentWord> wordList;

		
		//Display stemming results
		/**
		System.out.println(stemmer.Stem("caresses"));
		System.out.println(stemmer.Stem("ponies"));
		System.out.println(stemmer.Stem("ties"));
		System.out.println(stemmer.Stem("caress"));
		System.out.println(stemmer.Stem("cat");
		System.out.println(stemmer.Stem("singing"));
		System.out.println(stemmer.Stem("motoring"));
		System.out.println(stemmer.Stem("sing"));
		System.out.println(stemmer.Stem("sauces"));
		
		**/

		//Display TFIDF values for each document
		/**
		for(int i = 0; i < docList.size(); i++){
			System.out.println("Document : " + (i + 1));
			System.out.println();
			wordList = docList.get(i).getWordList();
			wordList = sorter.sort(wordList);
			
			for(int j = 0; j < 10; j++){
				System.out.println(wordList.get(j).getWord() + " " + wordList.get(j).getTFIDF());
			}
			
			System.out.println();
		}
		
		
		**/
		
		
		
		
		Profile user = new Profile("User1", "1234");
		
		String[] list = {"nectarine", "tree", "lizard"};
		
		ArrayList<DocumentWord> userWords = database.createWordList(list);
		
		user.addWords(userWords);
		
		database.formatVSM(user);
		
		ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();
		
		ArrayList<DocumentWord> vsm = user.getVSM();
		
		for(Document doc : database.getDocuments()) {
			double similarity1 = similarity.equate(user, doc);
			if(similarity1 > 0) {
				suggestions.add(new Suggestion(user, doc, similarity1));
			}
		}
		
		suggestions.sort(null);
		System.out.println("Suggestions: ");
		for(Suggestion suggestion : suggestions) {
			System.out.println("Document " + suggestion.getDocument().getTitle() + ": " + suggestion.getSimilarity());
		}
		
		//String queryString = "arctic coffee women frog lizard german";
	
		
		String queryString = BIO.getString();
		
		Query query;

	
		while(!queryString.equals("end")){
			ArrayList<QueryMatch> matches = new ArrayList<QueryMatch>();
			ArrayList<Word>	querySplit = splitter.splitText(queryString);
			
			int i = 0;
			for(Document docs : database.getDocuments()){
				ArrayList<TextWord> textQuery = assigner.initiate(querySplit);
				query = new Query(textQuery);
				database.calculateTFIDF(query);
				double similarity1 = similarity.equate(database.getDocuments().get(i), query);
				if(similarity1 > 0) {
					matches.add(new QueryMatch(query, docs, similarity1));
				}
				i++;
				//System.out.println("Dot Product: " + similarity.getDotProduct() + " Norm 1: " + similarity.getVector1Norm()
				//+ " Norm 2: " + similarity.getVector2Norm());
		
			}
			
			matches.sort(null);
			for(QueryMatch match : matches) {
				System.out.println("Document " + match.getDocument().getTitle() + ": " + match.getSimilarity());
			}
			queryString = BIO.getString();
			
		}
		
		
		
	}

}
