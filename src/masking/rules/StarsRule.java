package masking.rules;

public class StarsRule implements MaskingRule {
	int start;
	int length;
	boolean all = false;
	public StarsRule(int start, int length) {
		this.start = start;
		this.length = length;
	}

	public StarsRule() {
		this.all = true;
	}
	@Override
	public String mask(String in) {

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
