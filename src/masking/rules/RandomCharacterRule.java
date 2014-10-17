package masking.rules;

import java.util.Random;

public class RandomCharacterRule implements MaskingRule {

	public int start = 0;
	public int end = 0;

	public boolean all = false;

	Random random = new Random();
	String chars;

	public RandomCharacterRule(String chars) {
		this.chars = chars;
		all = true;
	}

	public RandomCharacterRule(String chars, int start, int end) {
		this.chars = chars;
		this.start = start;
		this.end = end;
	}

	@Override
	public String mask(String in) {
		if (all) {
			start = 0;
			end = in.length() - 1;

		}
		String replaced = "";
		for (int i = 0; i < end - start + 1; i++) {
			replaced += chars.charAt(random.nextInt(chars.length()));
		}

		return in.substring(0, start) + replaced + in.substring(end + 1, in.length());
	}
}