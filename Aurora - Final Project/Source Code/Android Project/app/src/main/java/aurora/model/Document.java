package aurora.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 *
 * Document - Stores all information about a specific users document.
 *
 */

public class Document extends Vector implements Serializable, Comparable<Document> {


    private static final long serialVersionUID = -7294652124482841060L;

    private int documentID;
    private String documentText;
    private String textLocation;
    private String imageLocation;
    private byte[] documentImage;
    private Profile author = new Profile();
    private int totalWords;
    private double similarity;
    private String lastUpdated;
    private ArrayList<DocumentWord> wordList = new ArrayList<DocumentWord>();
    private ArrayList<DocumentWord> VectorSpaceModel = new ArrayList<DocumentWord>();
    private String title = "Untitled";

    public Document(String s){
        documentText = s;
    }

    Document(String s, ArrayList<TextWord> l){
        documentText = s;
        for(TextWord tw : l){
            wordList.add(new DocumentWord(tw));
        }
    }

    Document(int id, String t, String s){
        documentID = id;
        title = t;
        documentText = s;
    }

    Document(int id, String t){
        documentID = id;
        title = t;
    }


    public int getID() {
        return documentID;
    }

    public void setID(int documentID) {
        this.documentID = documentID;
    }

    public void setWordList(ArrayList<DocumentWord> l){
        wordList = l;
    }

    public void updateDocFrequency(int f, int key){
        wordList.get(key).setDocFrequency(f);
    }

    public ArrayList<DocumentWord> getWordList(){
        return wordList;
    }

    public void setVSM(ArrayList<DocumentWord> v){
        for(DocumentWord word : v){
            DocumentWord newWord = new DocumentWord(word);
            VectorSpaceModel.add(newWord);
        }

    }


    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public ArrayList<DocumentWord> getVSM(){
        return VectorSpaceModel;
    }

    public void updateVSM(int index, double tfidf, double termFreq){
        VectorSpaceModel.get(index).setTFIDF(tfidf);
        VectorSpaceModel.get(index).setTextFrequency(termFreq);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return documentText;
    }

    public void setText(String documentText) {
        this.documentText = documentText;
    }

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    @Override
    public int compareTo(Document d) {
        return  this.getID() - d.getID() ;
    }

    public String getTextLocation() {
        return textLocation;
    }

    public void setTextLocation(String textLocation) {
        this.textLocation = textLocation;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public byte[] getImage() {
        return documentImage;
    }

    public void setImage(byte[] documentImage) {
        this.documentImage = documentImage;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


}