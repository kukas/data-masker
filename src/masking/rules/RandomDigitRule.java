package masking.rules;

import java.util.Random;

public class RandomDigitRule implements MaskingRule {

	public int start = 0;
	public int end = 0;
	
	public int length = 0;
	
	public boolean all = false;
	
	Random random = new Random();
	
	public RandomDigitRule(int start, int end){
		this.start = start;
		this.end = end;
		length = end-start+1;
	}
	
	public RandomDigitRule(boolean all){
		this.all = true;
	}
	
	@Override
	public String mask(String in) {
		if(all){
			start = 0;
			end = in.length()-1;
			length = end-start+1;
		}
		System.out.print(start);
		System.out.println(end);
		String randomCyphers = "";
		for(int i = 0; i < length; i++){
			randomCyphers += random.nextInt(10);
		};
		if(end == in.length()-1)
			return in.substring(0, start)+randomCyphers;
			
		return in.substring(0, start)+randomCyphers+in.substring(end, in.length());
	}

}
