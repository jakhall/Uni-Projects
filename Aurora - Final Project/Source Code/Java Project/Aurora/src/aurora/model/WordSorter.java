package aurora.model;
import java.util.ArrayList;

public class WordSorter {
	
	
	private ArrayList<DocumentWord> wordList;
	
	
	WordSorter(){
	}
	
	public ArrayList<DocumentWord> sort(ArrayList<DocumentWord> l){
		wordList = l;
		quicksort(0, wordList.size() - 1);
		return wordList;
	}

	
	
private void quicksort(int low, int high) {
    int i = low, j = high;
    
    DocumentWord pivot = wordList.get(low + (high-low)/2);

    while (i <= j) {
    	
        while (wordList.get(i).getTFIDF() > pivot.getTFIDF()) {
            i++;
        }
        
        while (wordList.get(j).getTFIDF() < pivot.getTFIDF()) {
            j--;
        }
        
        if (i <= j) {
            exchange(i, j);
            i++;
            j--;
        }
    }
    
    if (low < j){
        quicksort(low, j);
    }
    
    if (i < high){
        quicksort(i, high);
    }
    
}

private void exchange(int i, int j) {
    DocumentWord temp = wordList.get(i);
    wordList.set(i, wordList.get(j));
    wordList.set(j, temp);

	}
}