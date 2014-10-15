package masking.rules;

public class StarsRule implements MaskingRule {

	public static void main(String[] args){
		System.out.println(new RandomNumberRule(10, 20).mask("testaaaa"));
	}
	
	@Override
	public String mask(String in) {

		String out = new String("");

		for (int i = 0; i < in.length(); i++) {
			out += "*";
		}

		return out;
	}

}
