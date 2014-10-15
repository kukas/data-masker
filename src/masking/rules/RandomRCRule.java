package masking.rules;

import java.util.Random;
public class RandomRCRule implements MaskingRule{
	
	public RandomRCRule(){
		
		
	}
	
	@Override
	public String mask(String s){
		Random rnd = new Random();
		
		/*RandomNumberRule rnYear = new RandomNumberRule(0,100);
		RandomNumberRule rnMonth = new RandomNumberRule(1,13);
		String year = rnYear.mask("  ");
		String month = rnMonth.mask("  ");*/
		int year = rnd.nextInt(100);
		int month = rnd.nextInt(12)+1;//aby nebyla nula
		
		int[] numberOfDay = new int[] {31,28, 31,30,31,30,31,31,30,31,30,31};
		int day = rnd.nextInt(numberOfDay[month-1]+1);
		
		if(rnd.nextInt(2)==0){//u zen se pricita 50 k  mesici
			month += 50;
		}
		String yearS = fillForCorrectLength(Integer.toString(year),2,'0');
		String monthS = fillForCorrectLength(Integer.toString(month),2,'0');
		String dayS = fillForCorrectLength(Integer.toString(day),2,'0');
		String outString = yearS+monthS+dayS;
		int restNumber = (((((year * 100)+month)*100)+day)*10000)%11;
		int lastFourDigits;
		do{
			lastFourDigits = rnd.nextInt(10000);
		}while((restNumber+lastFourDigits)%11==0);
		
		outString += fillForCorrectLength(Integer.toString(lastFourDigits),4,'0');
		return outString;
	}
	
	public String fillForCorrectLength(String s,int i,char ch){
		String outS = s;
		while (outS.length() < i) { // prodlouzi String na spravnou
			// delku
			outS = ch + outS;
		}
		
		return outS;
	}
	
}
