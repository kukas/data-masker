package masking.rules;

import java.util.Random;

import exception.MaskingException;

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
		this.start = start - 1;
		this.end = end - 1;
	}

	@Override
	public String mask(String in) throws MaskingException {
		if (all) {
			start = 0;
			end = in.length() - 1;
		}
		if (end > in.length() - 1) {
			throw new MaskingException(
					"The third parameter of the \"replace_with_random_characters\" rule is greater than the column length.");
		}
		if (start < 0) {
			throw new MaskingException(
					"The second parameter of the \"replace_with_random_characters\" rule must be greater than zero.");
		}

		if (end < start) {
			throw new MaskingException(
					"The third parameter of the \"replace_with_random_characters\" rule must be greater than or equal to the second one.");
		}

		String replaced = "";
		for (int i = 0; i < end - start + 1; i++) {
			replaced += chars.charAt(random.nextInt(chars.length()));
		}

		return in.substring(0, start) + replaced + in.substring(end + 1, in.length());
	}
}
