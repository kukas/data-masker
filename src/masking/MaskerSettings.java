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
		} else {
			// throw new MaskingException("Wrong format setting_masking file (wrong number of parts in line)");
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
				try {
					if (numOfParams == 1) {
						return new StarsRule(1, Integer.parseInt(arrParams[0]));
					} else if (numOfParams == 2) {
						return new StarsRule(Integer.parseInt(arrParams[0]), Integer.parseInt(arrParams[1]));
					} else {
						throw new MaskingException("The \"star\" rule takes no, one or two parameters (" + numOfParams
								+ " given).");
					}
				} catch (NumberFormatException e) {
					throw new MaskingException("The \"star\" rule takes no, one or two integer parameters.");
				}
			}

		case "random_number":
			if (numOfParams == 2) {
				int min = 0;
				int max = 0;
				try {
					min = Integer.parseInt(arrParams[0]);
					max = Integer.parseInt(arrParams[1]);
					if (min < 0) {
						throw new MaskingException(
								"The first parameter of the \"random_number\" rule must be greater than zero.");
					}

					if (min > max) {
						throw new MaskingException(
								"The second parameter of the \"random_number\" rule must be greater than or equal to the first one.");
					}
				} catch (NumberFormatException e) {
					throw new MaskingException("The \"random_number\" rule takes no or two integer parameters.");
				}
				return new RandomNumberRule(min, max);
			} else if (numOfParams == 0) {
				return new RandomNumberRule();
			} else {
				throw new MaskingException("The \"random_number\" rule takes no or two parameters (" + numOfParams
						+ " given).");
			}

		case "replace_from_seeds_file":
			if (numOfParams != 1) {
				throw new MaskingException("The \"replace_from_seeds_file\" rule takes exactly one parameter ("
						+ numOfParams + " given).");
			} else {
				return new ReplaceRule(arrParams[0]);
			}

		case "random_rc":
			if (numOfParams == 0) {
				return new RandomRCRule();

			}else if(numOfParams==1){
				switch(arrParams[0]){
				case "yes":
					return new RandomRCRule(true);
				case "no":
					return new RandomRCRule(false);
				default:
					throw new MaskingException("The parametr for \""+funcName+"\" must be \"yes\" or \"no\".");
				}
			}else{
				throw new MaskingException("The \"random_rc\" rule takes no parameters ("+numOfParams+" given).");
			}

		case "random_phone_number":
			if (numOfParams == 0) {
				return new PhoneNumberRule();
			} else if (numOfParams == 1) {
				return new PhoneNumberRule(arrParams[0]);
			} else {
				throw new MaskingException("The \"random_phone_number\" rule takes no or one parameter.");
			}

		case "IBAN":
			if (numOfParams == 0) {
				return new IbanRule();
			} else if (numOfParams == 1) {
				return new IbanRule(arrParams[0]);
			} else {
				throw new MaskingException("The \"IBAN\" rule takes no or one parameter (" + numOfParams + " given).");
			}

		case "replace_with_random_digits":
			try {
				switch (numOfParams) {
				case 0:
					return new RandomDigitRule(true);
				case 1:
					return new RandomDigitRule(1, Integer.parseInt(arrParams[0]));
				case 2:
					return new RandomDigitRule(Integer.parseInt(arrParams[0]), Integer.parseInt(arrParams[1]));
				default:
					throw new MaskingException(
							"The \"replace_with_random_digits\" rule takes no, one or two parameters (" + numOfParams
									+ " given).");
				}

			} catch (NumberFormatException e) {
				throw new MaskingException(
						"The \"replace_with_random_digits\" rule takes no, one or two integer parameters.");
			}
		case "replace_with_random_characters":
			try {
				switch (numOfParams) {
				case 1:
					return new RandomCharacterRule(arrParams[0]);
				case 3:
					return new RandomCharacterRule(arrParams[0], Integer.parseInt(arrParams[1]), Integer.parseInt(arrParams[2]));
				default:
					throw new MaskingException(
							"The \"replace_with_random_characters\" rule takes one or three parameters (" + numOfParams
									+ " given).");
				}

			} catch (NumberFormatException e) {
				throw new MaskingException(
						"The \"replace_with_random_characters\" rule takes one or three parameters (the first one string, the second and the third one integers)");
			}

		default:
			throw new MaskingException("Incorrect rule format: " + originalString + ".");
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
