package masking.rules;

import exception.MaskingException;

public class StarsRule implements MaskingRule {
	int start;
	int end;
	boolean all = false;
	public StarsRule(int start, int end) {
		this.start = start-1;
		this.end= end-1;
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

		if (start>end) {
			throw new MaskingException(
					"The second parameter of the \"star\" rule must be greater than or equal to the first one.");
		}

		String out = new String("");

		for (int i = 0; i < in.length(); i++) {	
			if ((i >= start && i <= end) || all){
				out += "*";
			}
			else {
				out += in.charAt(i);
			}
		}

		return out;
	}

}
