package masking.rules;

import java.math.BigInteger;

import log.Logger;

public class IbanRule implements MaskingRule{
		
	String countryCode ="";
	boolean countryCodeSet = false;
	
	public IbanRule(String countryCode){
		this.countryCode =  countryCode;
		countryCodeSet = true;
	}
	public IbanRule(){
	}
	
	@Override
	public String mask(String in) {
		String accNumber = in.substring(4);
		RandomDigitRule randomizeAccNumber = new RandomDigitRule(true);
		accNumber = randomizeAccNumber.mask(accNumber);
		//Logger.debug(accNumber);
		
		if(this.countryCode.length() != 2){
			ReplaceRule country = new ReplaceRule("seeds/countryCodes.txt");
			countryCode = country.mask("00");
		}
		
		//ReplaceRule country = new ReplaceRule("seeds/countryCodes.txt");
		
		//countryCode = "CZ";
		String countryCodeConverted = (((int)countryCode.charAt(0)) - 55) + "";
		countryCodeConverted += (((int)countryCode.charAt(1)) - 55) + "";
		//Logger.debug(countryCodeConverted);
		BigInteger check = new BigInteger(accNumber + countryCodeConverted);
		int mod = (check.mod(new BigInteger("97"))).intValue();
		mod = 98 - mod;
		String checkDigit = mod +"";
		if(checkDigit.length() < 2){
		checkDigit = "0"+ checkDigit;	
		}
		
		
		String out = countryCode + checkDigit + accNumber;
		//Logger.debug(out);
		if(!countryCodeSet){
			countryCode= "";
		}
		
		return out;
	}

}
