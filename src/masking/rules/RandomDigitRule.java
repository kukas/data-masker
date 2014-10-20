package masking.rules;

import java.util.Random;

import exception.MaskingException;

public class RandomDigitRule implements MaskingRule {

	public int start = 0;
	public int end = 0;

	public int length = 0;

	public boolean all = false;

	Random random = new Random();

	public RandomDigitRule(int start, int end) {
		this.start = start - 1;
		this.end = end - 1;
		length = end - start + 1;
	}

	public RandomDigitRule(boolean all) {
		this.all = true;
	}

	@Override
	public String mask(String in) throws MaskingException {
		if (end > in.length() - 1) {
			throw new MaskingException(
					"The second parameter of the \"replace_with_random_digits\" rule is greater than the column length.");
		}
		if (start < 0) {
			throw new MaskingException(
					"The first parameter of the \"replace_with_random_digits\" rule must be greater than zero.");
		}
		if (end < start) {
			throw new MaskingException(
					"The second parameter of the \"replace_with_random_digits\" rule must be greater than or equal to the first one.");
		}

		if (all) {
			start = 0;
			end = in.length() - 1;
			length = end - start + 1;
		}
		String randomCyphers = "";
		for (int i = 0; i < length; i++) {
			randomCyphers += random.nextInt(10);
		}
		;
		if (end == in.length() - 1)
			return in.substring(0, start) + randomCyphers;

		return in.substring(0, start) + randomCyphers + in.substring(end + 1, in.length());
	}

}
