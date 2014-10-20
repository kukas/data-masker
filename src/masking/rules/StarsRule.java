package masking.rules;

import exception.MaskingException;

public class StarsRule implements MaskingRule {
	int start;
	int length;
	boolean all = false;
	public StarsRule(int start, int length) {
		this.start = start-1;
		this.length = length-1;
	}

	public StarsRule() {
		this.all = true;
	}
	@Override
	public String mask(String in) throws MaskingException {
		if (start < 0) {
			throw new MaskingException(
					"The first parameter of the \"star\" rule must be greater than or equal to zero.");
		}

		if (length<0) {
			throw new MaskingException(
					"The second parameter of the \"star\" rule must be greater than or equal to the second one.");
		}

		String out = new String("");

		for (int i = 0; i < in.length(); i++) {	
			if ((i >= start && i <= start+length) || all){
				out += "*";
			}
			else {
				out += in.charAt(i);
			}
		}

		return out;
	}

}
