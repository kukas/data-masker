package masking.rules;

import java.util.Random;

public class RandomNumberRule implements MaskingRule {

	long max;
	long min;
	boolean maxSet = false;

	public RandomNumberRule(long min, long max) {
		this.max = max;
		this.min = min;
		maxSet = true;
	}

	public RandomNumberRule() {
	}

	@Override
	public String mask(String in) {

		if (!maxSet) {
			max = (long) Math.pow(10, in.length()) - 1;
			min = 0;
		}

		String out = new String("");

		if (max > 2000000000) {
			long old = 0;
			while (out.length() < in.length()) {
				out += (int) (Math.random() * 9) + "";
				if (Long.parseLong(out) > max) {
					return new String(old + "");
				} else {
					old = Long.parseLong(out);
				}
			}
		} else {
			/*
			 * long l = Long.parseLong(out);
			 * 
			 * if(l<min || l>max){ mask(in); }
			 */

			int n;
			Random random = new Random();
			n = random.nextInt((int) (max - min)) + (int) min;
			out = n +"";
		}
		// String out = n+"";
		while (out.length() < in.length()) { // prodlouzi String na spravnou
												// delku
			out = 0 + out;
		}
		return out;
	}

}
