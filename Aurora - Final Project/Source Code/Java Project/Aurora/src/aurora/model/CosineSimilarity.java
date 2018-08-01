package aurora.model;
import java.util.ArrayList;

public class CosineSimilarity {
	
	private double dotProduct;
	private double vector1Norm;
	private double vector2Norm;
	private double similarity;
	
	private Vector vector1;
	private Vector vector2;
	
	public double equate(Document d, Query q){
		
		vector1 = createVector(q.getVSM());
		vector2 = createVector(d.getVSM());
	
		vector1Norm = vector1.norm();
		vector2Norm = vector2.norm();

		dotProduct = vector1.dotProduct(vector2);


		similarity =  dotProduct/(vector1Norm*vector2Norm);
		
		return similarity;
		
	}
	
	
	public double equate(Document d, Document d2){
		
		vector1 = createVector(d2.getVSM());
		vector2 = createVector(d.getVSM());
		
		dotProduct = vector1.dotProduct(vector2);
		vector1Norm = vector1.norm();
		vector2Norm = vector2.norm();

		similarity =  dotProduct/(vector1Norm*vector2Norm);
		
		return similarity;
		
	}
	
	
	public double equate(Profile p, Document d){
		
		vector1 = createVector(p.getVSM());
		vector2 = createVector(d.getVSM());
		
		dotProduct = vector1.dotProduct(vector2);
		vector1Norm = vector1.norm();
		vector2Norm = vector2.norm();

		similarity =  dotProduct/(vector1Norm*vector2Norm);
		
		return similarity;
		
	}


	private Vector createVector(ArrayList<DocumentWord> w){
		Vector vector;
		vector = new Vector();
		vector.addWeights(w);
		return vector;
	}
	
	
	public double getVector1Norm(){
		return vector1Norm;
	}
	
	public double getVector2Norm(){
		return vector2Norm;
	}
	
	public double getDotProduct(){
		return dotProduct;
	}
	
	public double getSimilarity(){
		return similarity;
	}
	
	

}
