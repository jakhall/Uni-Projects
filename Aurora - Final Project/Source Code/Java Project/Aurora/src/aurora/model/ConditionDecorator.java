package aurora.model;

/**
 * 
 * Condition Decorator - 
 *
 */

public class ConditionDecorator extends SuffixReplacer {

	Boolean notCheck;
	
	ConditionDecorator(String s, String suffix, String replace) {
		super(s, suffix, replace);
	}
	
	
	ConditionDecorator(SuffixReplacer sr, Boolean check){
		super(sr, sr.getCheck());
		notCheck = check;
	}
	
	
	public String getReplaced(){
		
		return innerComponent.getReplaced();
		
	}
	
	public String getOriginal(){
		return innerComponent.getOriginal();
	}
	
	public String getReplacement(){
		return innerComponent.getReplacement();
	}
	
	
	public boolean Success(){
		if(success){
			return innerComponent.Success();
		}
		
		return false;
		
	}
	

	
}
