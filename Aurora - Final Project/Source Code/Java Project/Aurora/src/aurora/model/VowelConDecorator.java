package aurora.model;

public class VowelConDecorator extends ConditionDecorator {
	
	VowelConDecorator(String s, String suffix, String replace) {
		super(s, suffix, replace);
	}
	
	VowelConDecorator(SuffixReplacer sr, Boolean check){
		super(sr, sr.getCheck());
		notCheck = check;
	}
	
	
	public String getReplaced(){
		
		String replaced = innerComponent.getReplaced();
		String replacement = innerComponent.getReplacement();
	
		String subReplaced = replaced.substring(0, replaced.length() - replacement.length());
		
		if(!notCheck){
			if(containsVowel(subReplaced)){
				success = innerComponent.Success();
				return replaced;
			}
		} else {
			if(!containsVowel(subReplaced)){
				success = innerComponent.Success();
				return replaced;
			}
		}
		
		success = false;
		return innerComponent.getOriginal();
	}
	
	
	public SuffixReplacer getComponent(){
		return innerComponent;
	}

	
	
	private boolean isVowel(char c){
		
		if("eaiou".indexOf(c) >= 0){
			return true;
		}
		
		return false;
	}
	
	
	private boolean containsVowel(String s){
		
		for(int i = s.length() - 1; i > 0; i--){
			if(isVowel(s.charAt(i))){
				return true;
			} else if(s.charAt(i) == 'y') {
				if(i < 1){
					return false;
				} else if(!isVowel(s.charAt(i - 1))){
					return true;
				}
				
			}
		}
		
		return false;
		
	}
	
}
