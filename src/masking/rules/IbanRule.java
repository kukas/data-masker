package masking.rules;

import java.io.File;
import java.math.BigInteger;

import exception.MaskingException;

public class IbanRule implements MaskingRule{
		
	String countryCode ="";
	boolean countryCodeSet = false;
	String ibanFile  = "seeds/countryCodes.txt";
	
	public IbanRule() throws MaskingException{
		if(!new File(ibanFile).exists()){
			throw new MaskingException("File for iban rule doesn't exist.");
		}
	}
	
	public IbanRule(String in) throws MaskingException{
		if(in.length()<2){
			throw new MaskingException("Country code must have two characters");
		}else if(in.length()==2){
			this.countryCode =  in;
			countryCodeSet = true;
		}else{//ted to bere jako cestu k souboru
			if(!new File(in).exists()){
				throw new MaskingException("File \""+in+"\" doesn't exist.");
			}
			this.ibanFile = in;
			
		}
		
	}
	
	@Override
	public String mask(String in) throws MaskingException{
		String accNumber = in.substring(4);
		RandomDigitRule randomizeAccNumber = new RandomDigitRule(true);
		accNumber = randomizeAccNumber.mask(accNumber);
		//Logger.debug(accNumber);
		
		if(this.countryCode.length() != 2){
			ReplaceRule country = new ReplaceRule(ibanFile);
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
