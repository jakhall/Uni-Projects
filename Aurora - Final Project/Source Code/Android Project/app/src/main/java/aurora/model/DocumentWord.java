package aurora.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class DocumentWord extends TextWord {


    private static final long serialVersionUID = -6929884833934088847L;


    private int docFrequency;
    private double TFIDF = 0;

    private HashMap<Document, Integer> documentMap = new HashMap<Document, Integer>();
    private ArrayList<Document> documents = new ArrayList<Document>();

    DocumentWord(){
        super();
    }


    DocumentWord(TextWord tw) {
        super(tw);
        docFrequency = 1;
        TFIDF = 0;
    }


    DocumentWord(DocumentWord dw) {
        super(dw);
        theWord = dw.getWord();
        documents = dw.getDocuments();
        textFrequency = dw.getTextFrequency();
        docFrequency = dw.getDocFrequency();
        TFIDF = dw.getTFIDF();
    }

    public void addDocument(Document doc, int key){
        documentMap.put(doc, key);
    }

    public void addDocument(Document doc){
        documents.add(doc);
    }

    public ArrayList<Document> getDocuments(){
        return documents;
    }

    public void addDocFrequency(int f){
        docFrequency += f;
    }

    public void setDocFrequency(int f){
        docFrequency = f;
    }

    public int getDocFrequency(){
        return docFrequency;
    }

    public void setTFIDF(double v){
        TFIDF = v;
    }

    public double getTFIDF(){
        return TFIDF;
    }


    public int compareTo(Object o) {
        double compFreq = ((DocumentWord)o).getTFIDF();
        return  (int)(compFreq - this.TFIDF);
    }

}