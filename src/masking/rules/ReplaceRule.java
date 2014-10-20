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
		String jmeno = seed[random.nextInt(seed.length)];
		if (jmeno.length() > in.length()){
			jmeno = jmeno.substring(0, in.length());
		}
		return jmeno;
	}
}
