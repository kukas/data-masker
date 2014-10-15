package masking.rules;

public class PhoneNumberRule implements MaskingRule{
	
	@Override
	public String mask(String in) {
		String noSpace = in.replaceAll(" ", "");
		System.out.println("nospace: " + noSpace);
		String firstThreeDigits = noSpace.substring(0,3);
		ReplaceRule prefix = new ReplaceRule("output/PhoneNumbers.txt");
		firstThreeDigits = prefix.mask(firstThreeDigits);
		String otherDigits = ((int)(Math.random() * 999999)) + "";
		while(otherDigits.length()<6){
			otherDigits = "0" + otherDigits;			
		}
		//rand.mask(otherDigits);
		System.out.println(firstThreeDigits + " " + otherDigits);
		String out = new String(firstThreeDigits  +" "+ otherDigits);
		while(out.length()<in.length()){
			out = " " + out;			
		}
		return new String(firstThreeDigits + otherDigits);
	}

}
