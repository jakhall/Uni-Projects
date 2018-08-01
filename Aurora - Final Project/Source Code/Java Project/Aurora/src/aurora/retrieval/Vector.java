package aurora.retrieval;
import java.util.ArrayList;

import aurora.model.DocumentWord;

/**
 * 
 * Vector - An object to represent the Documents, Profiles and Queries within the vector space model.
 *
 */

public class Vector {
	
	private double[] weights;
	
	public Vector(){
	}
	
	
	//stores the weights as an array.
	
	public Vector(double[] d){
		weights = d;
	}
	
	//addWeights - sets the values of the weights array.
	
	public void addWeights(double[] d){
		weights = d;
	}
	
	public double[] getWeights(){
		return weights;
	}
	
	public void addWeights(ArrayList<DocumentWord> w){
		weights = new double[w.size()];
		for(int i = 0; i < w.size(); i++) {
			weights[i] = w.get(i).getTFIDF();
		}
	}
	

	//dotProduct - calculates and returns the dot product of two vectors.
	
	public double dotProduct(Vector v2){
		double product = 0;
		for(int i = 0; i < weights.length; i++){
			if((weights[i] > 0) && (v2.getWeights()[i] > 0)){
				product += (weights[i] * v2.getWeights()[i]);
			}
		}
		return product;
	}


	//norm - calculates and returns the normal of the vector.
	
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
