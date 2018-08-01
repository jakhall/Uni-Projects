package aurora.model;
import java.util.ArrayList;

public class Query extends Vector {
	FrequencyAssigner assigner = new FrequencyAssigner();
	private ArrayList<DocumentWord> wordList = new ArrayList<DocumentWord>();
	private ArrayList<DocumentWord> VectorSpaceModel = new ArrayList<DocumentWord>();
	
	Query(ArrayList<TextWord> l){
		for(TextWord tw : l){
			wordList.add(new DocumentWord(tw));
		}
	}
	
	public ArrayList<DocumentWord> getWordList(){
		return wordList;
	}
	
	public void setWordList(ArrayList<DocumentWord> d){
		wordList = d;
	}
	
	
	public void setVSM(ArrayList<DocumentWord> v) {
		for(DocumentWord word : v) {
			DocumentWord newWord = new DocumentWord(word);
			VectorSpaceModel.add(newWord);
		}
	}

	public void updateVSM(int index, double tfidf, double termFreq) {
		if(index >= 0) {
			VectorSpaceModel.get(index).setTextFrequency(termFreq);
			VectorSpaceModel.get(index).setTFIDF(termFreq);
		}
	}
	
	public ArrayList<DocumentWord> getVSM(){
		return VectorSpaceModel;
	}

}
