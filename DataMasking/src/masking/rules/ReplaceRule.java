package masking.rules;

import input.FileReader;

import java.util.Random;

public class ReplaceRule implements MaskingRule {
	
	String[] seed;
	Random random;
	public ReplaceRule(String seedFile){
		FileReader f = new FileReader(seedFile);
		seed = f.read();
		random = new Random();
	}
	
	@Override
	public String mask(String in) {
		return seed[random.nextInt(seed.length)];
	}
}
