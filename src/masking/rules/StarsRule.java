package masking.rules;

public class StarsRule implements MaskingRule {
	int start;
	int end;
	boolean all = false;
	public StarsRule(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public StarsRule() {
		this.all = true;
	}
	@Override
	public String mask(String in) {

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
