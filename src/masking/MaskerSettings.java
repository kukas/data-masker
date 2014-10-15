package masking;

import input.FileReader;
import log.Logger;
import masking.rules.*;
//import masking.rules.NothingRule;

public class MaskerSettings extends Exception {
	public String[] settingStrings;
	public MaskerSettings(String settingFile){
		FileReader fReader = new FileReader(settingFile);
		this.settingStrings = fReader.read();
		
	}
	
	public MaskingRule[] getRules(){
		MaskingRule[] mRules = new MaskingRule[this.settingStrings.length];
		for(int i = 0; i < this.settingStrings.length;i++){
			Logger.debug("radek "+(i+1));
			mRules[i] = getRuleByString(this.settingStrings[i]);
		}
		return mRules;
	}
	
	public MaskingRule getRuleByString(String s){
		String[] arrParams = getArrayParams(s);//to vrati v prvnim  prvku jmeno funkce a dalsi prvky jsou parametry
		int numOfParams = arrParams.length-1;
		switch(arrParams[0]){
			case "do_nothing":
				return new NothingRule();

			case "star":
				if (numOfParams == 0){
					return new StarsRule();
				}
				else {
					if(numOfParams == 1){
						return new StarsRule(0,Integer.parseInt(arrParams[1]));
					} else {
						return new StarsRule(Integer.parseInt(arrParams[1]), Integer.parseInt(arrParams[2]));
					}
				}
				

			case "random_number":
				if(numOfParams==2){
					int min = 0;
					int max = 0;
					try{
						min = Integer.parseInt(arrParams[1]);
						max = Integer.parseInt(arrParams[2]);
					}catch(Exception e){
						Logger.log("Bad parametrs for random number");
					}
					return new RandomNumberRule(min,max);
				}else{
					return new RandomNumberRule();
				}
				
			case "ReplaceFromSeedsFile" :
				return new ReplaceRule(arrParams[1]);
	
			case "random_rc":
				return new RandomRCRule();
				
			case "PhoneNumberRule":
				return new PhoneNumberRule();

				
			case "ReplaceWithRandomDigits" : 
				if(numOfParams == 0){
					return new RandomDigitRule(true);
				}else {if(numOfParams == 1){
					return new RandomDigitRule(0,Integer.parseInt(arrParams[1]));
				} else {
					return new RandomDigitRule(Integer.parseInt(arrParams[1]), Integer.parseInt(arrParams[2]));
				}}
			
			default:
				Logger.log("Bad format line in masking settings file. / "+s);
		}
		return new NothingRule();
	}
	
	//rozdeli radek na jednotlive parametry (napr. random_number;4;9 => array("random_number","4","9"))
	public static String[] getArrayParams(String s){
		int startPosition = s.indexOf("(");
		int endPosition = s.lastIndexOf(")");
		if(startPosition==-1 && endPosition == -1){//nejsou zadne parametry
			return new String[]{s};
		}
		if(startPosition==-1 || endPosition == -1){//zde by se mela vyhodit chyba 
			//throw new Exception();
		}
		String funcName = s.substring(0,startPosition);
		String parametersString = s.substring(startPosition+1, endPosition);
		String[] arrParameters = parametersString.split(",");
		String[] retArr = new String[arrParameters.length+1];
		retArr[0] = funcName;
		for(int i = 0; i < arrParameters.length; i++){
			retArr[i+1] = arrParameters[i];
		}
		return retArr;
	}

	
	
	
}
