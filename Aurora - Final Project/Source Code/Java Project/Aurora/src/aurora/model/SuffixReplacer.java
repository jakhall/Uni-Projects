package aurora.model;

public class SuffixReplacer {
	
	protected String original;
	protected String replaced;
	protected String replacement;
	protected String suffix;
	protected boolean notCheck;
	protected boolean success;
	protected SuffixReplacer innerComponent;

	
	
	SuffixReplacer(){
		
	}
	
	
	SuffixReplacer(String str, String suf, String rep){
		
		String string = str;
		original = str;
		suffix = suf;
		replacement = rep;
		
		if (string.endsWith(suffix)){
			string = string.substring(0, string.length() - suffix.length());
			string = string + rep;
			success = true;
		} else {
			success = false;
			replaced = original;
		}
		
		replaced = string;
	}

	
	SuffixReplacer(SuffixReplacer sr, Boolean check){
		innerComponent = sr;
		notCheck = check;
	}
	
	
	
	
	public Boolean getCheck(){
		
		return notCheck;
	}
	
	
	public String getReplaced(){
		return replaced;
		
	}
	
	public String getReplacement(){
		return replacement;
		
	}
	
	public String getSuffix(){
		
		return suffix;
		
	}
	
	public boolean Success(){
		
		return success;
		
	}
	
	public String getOriginal(){
		return original;
		
	}
	
}
