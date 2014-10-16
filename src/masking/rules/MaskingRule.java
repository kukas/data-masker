package masking.rules;

import exception.MaskingException;

public interface MaskingRule {
	
	public String mask(String in) throws MaskingException;
}

