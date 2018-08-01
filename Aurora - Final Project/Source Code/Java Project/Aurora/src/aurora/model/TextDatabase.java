package aurora.model;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TextDatabase {
	
	ArrayList<Document> documentList = new ArrayList<Document>();
	ArrayList<TextWord> textWordList = new ArrayList<TextWord>();
	ArrayList<DocumentWord> VSM = new ArrayList<DocumentWord>();
	private int frequency;
	//private ArrayList<Term> VSM = new ArrayList<Term>(); 
	
	TextDatabase(){
		
	}

	public void addDocument(Document d){
		documentList.add(d);
		extendVSM(d);
	}

	
	private void extendVSM(Document d) {
		int index;
		
		Comparator<DocumentWord> tw = new Comparator<DocumentWord>() {
		    public int compare(DocumentWord a, DocumentWord b) {
		        return a.getWord().compareTo(b.getWord());
		    };
		};
		    
		ArrayList<DocumentWord> words = d.getWordList();
		DocumentWord newWord;
		
		for(DocumentWord word : words){
			index = Collections.binarySearch(VSM, word, tw);
			if(index >= 0){
				VSM.get(index).addDocument(d);
			} else{
				newWord = new DocumentWord(word);
				newWord.addDocument(d);
				VSM.add(-(index+1), newWord);
			}
		}
	}
	
	
	
	public void calculateDocFrequency(){
		ArrayList<Document> wordDocs;
		for(DocumentWord word : VSM){
			wordDocs = word.getDocuments();
			frequency = wordDocs.size();
			word.setDocFrequency(frequency);
		}
	}
	
	public void assignDocFrequency(Query q){
		
		int index; 
		
		Comparator<Word> c = new Comparator<Word>() {
		    public int compare(Word a, Word b) {
		        return a.getWord().compareTo(b.getWord());
		    };
		};
		
		for(DocumentWord word : q.getWordList()) {
			index = Collections.binarySearch(VSM, word, c);
			
			if(index >= 0) {
				word.setDocFrequency(VSM.get(index).getDocFrequency() + 1);
			} else {
				word.setDocFrequency(1);
			}
		}

	}
	
	public ArrayList<DocumentWord> assignDocFrequency(ArrayList<TextWord> t){
		ArrayList<DocumentWord> newList = new ArrayList<DocumentWord>();
		DocumentWord newWord = new DocumentWord();
		for(TextWord textWord : t ){
			for(DocumentWord documentWord : VSM){
				newWord = new DocumentWord(textWord);
				newWord.setDocFrequency(1);
				if(textWord.getWord().equals(documentWord.getWord())){
					newWord.setDocFrequency(documentWord.getDocFrequency() + 1);
					break;
				} 

			}
			
			newList.add(newWord);
		}
		
		return newList;
	}
	
	
	public void calculateTFIDF(){
			
			Comparator<Word> c = new Comparator<Word>() {
			    public int compare(Word a, Word b) {
			        return a.getWord().compareTo(b.getWord());
			    };
			};
			
			for(Document doc : documentList) {
				int index;
				double termFrequency;
				int documentFrequency;
				int totalDocuments;
				double TFIDF;
				ArrayList<DocumentWord> documentWords = doc.getWordList();
	
				doc.setVSM(VSM);
		
				for(DocumentWord word : documentWords) {
					index = Collections.binarySearch(doc.getVSM(), word, c);
					if(index >= 0) {
						termFrequency = word.getTextFrequency();
						documentFrequency = VSM.get(index).getDocFrequency();
						totalDocuments = documentList.size();
						TFIDF = (termFrequency/doc.getWordList().size())*(Math.log(totalDocuments/(1 + documentFrequency)));
						doc.updateVSM(index, TFIDF, termFrequency);
						word.setTFIDF(TFIDF);
				}
			}
		}
	}
	
	
	
	public void calculateTFIDF(Query q){
		
		Comparator<Word> c = new Comparator<Word>() {
		    public int compare(Word a, Word b) {
		        return a.getWord().compareTo(b.getWord());
		    };
		};
		
		int index;
		double termFrequency;
		int totalDocuments;
		double TFIDF;
		int documentFrequency = 1;
		ArrayList<DocumentWord> queryWords = q.getWordList();

		q.setVSM(VSM);
	
		for(DocumentWord word : queryWords) {
			index = Collections.binarySearch(q.getVSM(), word, c);
			if(index >= 0) {
				documentFrequency = VSM.get(index).getDocFrequency();
			} 
			
			termFrequency = word.getTextFrequency();
			totalDocuments = documentList.size();
			TFIDF = (termFrequency/q.getWordList().size())*(Math.log(totalDocuments/(1 + documentFrequency)));
			q.updateVSM(index, TFIDF, termFrequency);
		}
	}
	
	
	public void formatVSM(Profile p) {
		
		int index;
		p.setVSM(VSM);
		
		Comparator<Word> c = new Comparator<Word>() {
		    public int compare(Word a, Word b) {
		        return a.getWord().compareTo(b.getWord());
		    };
		};
		
		for(DocumentWord word : p.getWordList()) {
			index = Collections.binarySearch(VSM, word, c);
			double tfidf = 0.3;
			if(index >= 0) {
				p.updateVSM(index, tfidf);
			} 
		}
		
	}

	public ArrayList<DocumentWord> createWordList(String[] s){
		ArrayList<DocumentWord> newList = new ArrayList<DocumentWord>();
		for(String text : s) {
			TextWord textWord = new TextWord(text);
			DocumentWord documentWord = new DocumentWord(textWord);
			newList.add(documentWord);
		}
		
		return newList;
	}
	
	
	
	
	public ArrayList<DocumentWord> getVSM(){
		
		return VSM;
		
	}
	
	public ArrayList<Document> getDocuments(){
		
		return documentList;
		
	}
	
	public ArrayList<DocumentWord> getDocumentWords(){
		return VSM;
	}

}
