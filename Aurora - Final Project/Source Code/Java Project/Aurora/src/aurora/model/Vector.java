package aurora.model;
import java.util.ArrayList;

public class Vector {
	
	private double[] weights;
	
	public Vector(){
	}
	
	public void addWeights(double[] d){
		weights = d;
	}
	
	public void addWeights(ArrayList<DocumentWord> w){
		weights = new double[w.size()];
		for(int i = 0; i < w.size(); i++) {
			weights[i] = w.get(i).getTFIDF();
		}
	}
	
	public double dotProduct(Vector v2){
		double product = 0;
		for(int i = 0; i < weights.length; i++){
			if((weights[i] > 0) && (v2.getWeights()[i] > 0)){
				product += (weights[i] * v2.getWeights()[i]);
			}
		}
		return product;
	}
	
	
	
	public double[] getWeights(){
		return weights;
	}
	
	
	public double norm(){
		double norm = 0;
		for(double TFIDF: weights){
			if(TFIDF > 0){
				norm = norm + Math.pow(TFIDF, 2);
			}
		}
	
		return Math.sqrt(norm);
		
	}
	
	
}
