package masking.rules;

import input.FileReader;

import java.io.File;
import java.util.Random;

import exception.MaskingException;

public class ReplaceRule implements MaskingRule {
	
	String[] seed;
	Random random;
	public ReplaceRule(String seedFile) throws MaskingException{
		if(new File(seedFile).exists()){
			FileReader f = new FileReader(seedFile);
		
			seed = f.read();
			random = new Random();
		}else{//soubor neexistuje
			throw new MaskingException("Seed file doesn't exist");
		}
	}
	
	@Override
	public String mask(String in) {
		return seed[random.nextInt(seed.length)];
	}
}
