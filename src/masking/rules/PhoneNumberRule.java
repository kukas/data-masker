package masking.rules;

import exception.MaskingException;

public class PhoneNumberRule implements MaskingRule{
	
	@Override
	public String mask(String in) throws MaskingException{
		String noSpace = in.replaceAll(" ", "");
		String firstThreeDigits = noSpace.substring(0,3);
		ReplaceRule prefix = new ReplaceRule("seeds/PhoneNumbers.txt");
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
