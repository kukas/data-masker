package masking;

import java.util.Arrays;
import java.util.Vector;

import log.Logger;
import masking.rules.MaskingRule;
import exception.MaskingException;

public class Masker {
	public MaskerSettings maskerSettings;
	public MaskingRule[] maskingRules;
	
	
	public Masker() {

	}
	
	public Masker(String maskingSettingFile) throws MaskingException{
		maskerSettings = new MaskerSettings(maskingSettingFile);
		maskingRules = maskerSettings.getRules();
	}

	public String[][] mask(String[][] input, String maskingsSettingFile) throws MaskingException{
		MaskerSettings mSetting = new MaskerSettings(maskingsSettingFile);
		MaskingRule[] rules = mSetting.getRules();
		if (rules.length > input[0].length) {
			Logger.log("Warning: the number of rules is greater than the number of columns.");
		}

			for (int j = 0; j < input[0].length; j++) {
		for (int i = 0; i < input.length; i++) {
				input[i][j] = rules[j].mask(input[i][j]);
			}
		}

		return input;
	}
	
	public String[][] mask(String[][] input){
		if (maskingRules.length > input[0].length) {
			Logger.log("Warning: the number of rules is greater than the number of columns.");
		}
		
		//database.length -> pocet radku
		
		for (int i = 0; i < maskingRules.length; i++) //bere 1 maskingRules a zmeni podle neho cely sloupec
		{
			for (int j = 0; j < input.length; j++) //prochazi cely sloupec
			{
				input[j][i] = maskingRules[i].mask(input[j][i]);
			}
		}

		return input;
	}
	
	public Vector<Vector<Object>> getData(){
		//TODO: doplnit metodu, aby vracela data (formát viz RulesTable - 6 slopcu, pravidlo na radek)
		String[] settingStrings = maskerSettings.getSettingStrings();
		Vector<Vector<Object>> vB = new Vector<>();
		for (int i=0; i<settingStrings.length; i++){
			String[] settingLine = settingStrings[i].split("-");
			Vector<Object> vA = new Vector(Arrays.asList(settingLine));
			vB.add(vA);
		}
		return vB;
	}
	
	public boolean setData(Vector<Vector<Object>> data) throws MaskingException{ //vraci, zda se jedna o validni data
		//TODO: doplnit metodu, aby se MaskingRules nastavily podle dat (na MaskerSettings nezalezi)
		/*
		 * Ukazka vectoru:
		 * Vector<Object> vA = new Vector(Arrays.asList(new String[] { "a", "z", "d", "o", "x", "c" }));
		 * Vector<Vector<Object>> vB = new Vector<>();
		 * vB.add(vA);
		*/
		
		
		int size = data.size();
		String[] settingStrings;
		if(size >= 4){
			int size2 = data.get(4).size();
			settingStrings = new String[size2];
			for (int i=0; i<size; i++){
				String delimiter = "";
				settingStrings[i] = "";
				for (int j=0; j<size2; j++){
					settingStrings[i] += delimiter + data.get(j).get(i).toString();
					delimiter = ";";
				}
			}
		}
		else {
			return false;
		}
		
		
		maskerSettings = new MaskerSettings(settingStrings);
		
		maskingRules = maskerSettings.getRules();
		
		
		return true;
	}
}
