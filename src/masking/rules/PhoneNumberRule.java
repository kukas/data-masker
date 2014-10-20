package masking.rules;

import input.FileReader;

import java.io.File;
import java.util.Random;

import exception.MaskingException;

public class PhoneNumberRule implements MaskingRule{
	public String fileName;
	private ReplaceRule replaceRule;
	public PhoneNumberRule()  throws MaskingException{
		String file = "seeds/phoneNumbers.txt";
		if(new File(file).exists()){
			this.fileName = file;
		}else{//soubor neexistuje
			throw new MaskingException("Default seeds file for \"random_phone_number\" rule not found. Please specify the seeds file in the parameter.");
		}
		replaceRule = new ReplaceRule(this.fileName);
	}
	
	public PhoneNumberRule(String file) throws MaskingException{
		if(new File(file).exists()){
			this.fileName = file;
		}else{//soubor neexistuje
			throw new MaskingException("The \"random_phone_number\" rule was given an invalid parameter (takes the path to the seeds file) or the seeds file does not exist.");
		}
		
		replaceRule = new ReplaceRule(this.fileName);
	}
	
	@Override
	public String mask(String in) throws MaskingException{
		String noSpace = in.replaceAll(" ", "");
		String firstThreeDigits = noSpace.substring(0,2);
		ReplaceRule prefix = replaceRule;
		firstThreeDigits = prefix.mask(firstThreeDigits);
		String otherDigits = ((int)(Math.random() * 999999)) + "";
		while(otherDigits.length()<6){
			otherDigits = "0" + otherDigits;
		}
		String out = new String(firstThreeDigits  +" "+ otherDigits);
		while(out.length()<in.length()){
			out = " " + out;			
		}
		return new String(firstThreeDigits + otherDigits);
	}

}
