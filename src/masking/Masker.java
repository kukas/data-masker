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

	public Masker(String maskingSettingFile) throws MaskingException {
		maskerSettings = new MaskerSettings(maskingSettingFile);
		maskingRules = maskerSettings.newGetRules();
	}

	public Masker(Vector<Vector<Object>> data) throws MaskingException {
		setData(data);
	}

	public String[][] mask(String[][] input, String maskingsSettingFile) throws MaskingException {
		MaskerSettings mSetting = new MaskerSettings(maskingsSettingFile);
		MaskingRule[] rules = mSetting.newGetRules();
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
	
	private String padTo(String in, int length){
		while(in.length()<length){
			in+=" ";
		}
		return in;
	}

	public String[][] mask(String[][] input) throws MaskingException {
		if (maskingRules.length > input[0].length) {
			Logger.log("Warning: the number of rules is greater than the number of columns.");
		}

		// database.length -> pocet radku

		for (int i = 0; i < maskingRules.length; i++) // bere 1 maskingRules a zmeni podle neho cely sloupec
		{
			for (int j = 0; j < input.length; j++) // prochazi cely sloupec
			{
				input[j][i] = padTo(maskingRules[i].mask(input[j][i]), input[j][i].length());
			}
		}

		return input;
	}

	public Vector<Vector<Object>> getData() {
		// TODO: doplnit metodu, aby vracela data (formát viz RulesTable - 6 slopcu, pravidlo na radek)
		String[] settingStrings = maskerSettings.getSettingStrings();
		Vector<Vector<Object>> vB = new Vector<>();
		for (int i = 0; i < settingStrings.length; i++) {
			String[] settingLine = settingStrings[i].split(";");
			Vector<Object> vA = new Vector(Arrays.asList(settingLine));
			vB.add(vA);
		}
		return vB;
	}

	public boolean setData(Vector<Vector<Object>> data) throws MaskingException { // vraci, zda se jedna o validni data
		// TODO: doplnit metodu, aby se MaskingRules nastavily podle dat (na MaskerSettings nezalezi)
		/*
		 * Ukazka vectoru: Vector<Object> vA = new Vector(Arrays.asList(new String[] { "a", "z", "d", "o", "x", "c" }));
		 * Vector<Vector<Object>> vB = new Vector<>(); vB.add(vA);
		 */

		int columns= data.get(0).size();
		int rows = data.size();
		String[] settingStrings;
		if (columns >= 6) {

			settingStrings = new String[rows];
			for (int row = 0; row < rows; row++) {
				String delimiter = "";
				settingStrings[row] = "";
				for (int column = 0; column < columns; column++) {
					String whitespace = " ";
					if (column == columns - 1) {
						whitespace = "";
					}
					settingStrings[row] += delimiter
							+ ((data.get(row).get(column) == null) ? whitespace : data.get(row).get(column).toString());
					delimiter = ";";
				}
				
				// settingStrings[i] = "a;a;3;4;do_nothing;";
			}

		} else {
			return false;
		}

		maskerSettings = new MaskerSettings(settingStrings);
		maskingRules = maskerSettings.newGetRules();

		return true;
	}
}
