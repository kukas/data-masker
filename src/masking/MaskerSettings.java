package masking;

import input.FileReader;
import log.Logger;
import masking.rules.*;
import exception.MaskingException;

//import masking.rules.NothingRule;

public class MaskerSettings extends Exception {
	public String[] settingStrings;

	public MaskerSettings(String settingFile) {
		FileReader fReader = new FileReader(settingFile);
		this.settingStrings = fReader.read();

	}

	public MaskerSettings(String[] settingStrings) {
		this.settingStrings = settingStrings;

	}

	public String[] getSettingStrings() {
		return settingStrings;
	}

	public MaskingRule[] getRules() throws MaskingException {
		MaskingRule[] mRules = new MaskingRule[this.settingStrings.length];
		for (int i = 0; i < this.settingStrings.length; i++) {
			mRules[i] = getRuleByString(this.settingStrings[i]);
		}
		return mRules;
	}

	public MaskingRule[] newGetRules() throws MaskingException {
		MaskingRule[] mRules = new MaskingRule[this.settingStrings.length];
		for (int i = 0; i < this.settingStrings.length; i++) {
			mRules[i] = newGetRuleByString(this.settingStrings[i]);
		}
		return mRules;
	}

	public MaskingRule getRuleByString(String s) throws MaskingException {
		String[] arrData = getArrayParams(s);// to vrati v prvnim prvku jmeno funkce a dalsi prvky jsou parametry
		String funcName = arrData[0];
		String[] arrParams = new String[arrData.length - 1];
		for (int i = 0; i < arrParams.length; i++) {
			arrParams[i] = arrData[i + 1];
		}
		return getRuleByRuleName(funcName, arrParams, s);
		// return new NothingRule();
	}

	public MaskingRule newGetRuleByString(String s) throws MaskingException {
		String[] pole = s.split(";");
		String ruleName = pole[4];
		if (pole.length > 5) {
			String[] arguments = pole[5].split(",");
			int numOfParams = arguments.length;

			return getRuleByRuleName(ruleName, arguments, s);
		}else{
			//throw new MaskingException("Wrong format setting_masking file (wrong number of parts in line)");
		}

		return getRuleByRuleName(ruleName, new String[0], s);
	}

	// originalString je jen pro moznost vyhodit chybu s popiskem...
	public MaskingRule getRuleByRuleName(String funcName, String[] arrParams, String originalString)
			throws MaskingException {
		int numOfParams = arrParams.length;
		switch (funcName) {
		case "do_nothing":
			return new NothingRule();

		case "star":
			
			if (numOfParams == 0) {
				return new StarsRule();
			} else {
				try{
					if (numOfParams == 1) {
						return new StarsRule(0, Integer.parseInt(arrParams[0]));
					} else if(numOfParams==2){
						int start = Integer.parseInt(arrParams[0]);
						int end = Integer.parseInt(arrParams[1]);
						int correctStart = start-1;//indexy
						int correctEnd = end-1;
						
						if(correctStart<0){
							throw new MaskingException("Start position in star rule must be > 0");
						}
						
						if(correctStart>correctEnd){
							throw new MaskingException("Start position is bigger than end position");
						}
						
						return new StarsRule(correctStart, correctEnd);
					}else{
						throw new MaskingException("In star rule are too much parametrs");
					}
				}catch(NumberFormatException e){
					throw new MaskingException("Bad parametrs for star rule. (Parametrs can't be convert to integer)");
				}
			}

		case "random_number":
			if (numOfParams == 2) {
				int min = 0;
				int max = 0;
				try {
					min = Integer.parseInt(arrParams[0]);
					max = Integer.parseInt(arrParams[1]);
					if(min<0){
						throw new MaskingException("Minium number must be >= 0");
					}
					
					if(min>max){
						throw new MaskingException("Maximum number must be bigger than minimum number");
					}
				} catch (NumberFormatException e) {
					throw new MaskingException("Bad parameters for " + funcName + " (parametr can't be convert to integer)");
				}
				return new RandomNumberRule(min, max);
			} else if(numOfParams==0){
				return new RandomNumberRule();
			}else{
				throw new MaskingException("Bad number of parametrs for "+funcName);
			}

		case "replace_from_seeds_file":
			if(numOfParams!=1){
				throw new MaskingException("For rule replace_from_seeds_file must be only parametr for file name.");
			}else{
				return new ReplaceRule(arrParams[0]);
			}
			

		case "random_rc":
			return new RandomRCRule();

		case "random_phone_number":
			if(numOfParams== 0){
				return new PhoneNumberRule();
			}else if(numOfParams==1){
				return new PhoneNumberRule(arrParams[0]);
			}else{
				throw new MaskingException("Wrong number of parameters for "+funcName);
			}
		case "IBAN":
			if (numOfParams == 0) {
				return new IbanRule();
			} else if (numOfParams == 1) {
				return new IbanRule(arrParams[0]);
			}else{
				throw new MaskingException("Wrong number of parametrs for "+funcName);
			}

		case "replace_with_random_digits":
			try {
				switch(numOfParams){
				case 0:
					return new RandomDigitRule(true);
				case 1:
					return new RandomDigitRule(0, Integer.parseInt(arrParams[0]));
				case 2:
					return new RandomDigitRule(Integer.parseInt(arrParams[0]), Integer.parseInt(arrParams[1]));
				default:
					throw new MaskingException("Wrong number of parameters for "+funcName);
				}
				
			} catch (NumberFormatException e) {
				throw new MaskingException("Bad parameters for " + funcName + " (Parametr can't be convert to integer)");
			}
		case "replace_with_random_characters":
			try {
				switch(numOfParams){
				case 1:
					return new RandomCharacterRule(arrParams[0]);
				case 3:
					return new RandomCharacterRule(arrParams[0], Integer.parseInt(arrParams[1]),
							Integer.parseInt(arrParams[2]));
				default:
					throw new MaskingException("Wrong number of parameters for "+funcName);
				}
				

			} catch (NumberFormatException e) {
				throw new MaskingException("Bad parameters for " + funcName + " (Parametrs can't be convert to integer)");
			}

		default:
			throw new MaskingException("Bad format line in masking settings file. Undefined rule: \"" + originalString
					+ "\"");
		}
	}

	// rozdeli radek na jednotlive parametry (napr. random_number;4;9 => array("random_number","4","9"))
	public static String[] getArrayParams(String s) throws MaskingException {
		int startPosition = s.indexOf("(");
		int endPosition = s.lastIndexOf(")");
		if (startPosition == -1 && endPosition == -1) {// nejsou zadne parametry
			return new String[] { s };
		}
		/*
		 * if (startPosition == -1 || endPosition == -1) {// zde by se mela vyhodit chyba throw new
		 * MaskingException("Bad format masking-settings file:\"" + s + "\""); }
		 */
		String funcName = s.substring(0, startPosition);
		String parametersString = s.substring(startPosition + 1, endPosition);
		String[] arrParameters = parametersString.split(",");
		String[] retArr = new String[arrParameters.length + 1];
		retArr[0] = funcName;
		for (int i = 0; i < arrParameters.length; i++) {
			retArr[i + 1] = arrParameters[i].trim();
		}
		return retArr;
	}

}
