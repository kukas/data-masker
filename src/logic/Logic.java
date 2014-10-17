package logic;

import input.DatabaseReader;
import input.FileReader;

import java.util.List;

import log.Logger;
import masking.Masker;
import output.DatabaseWriter;
import exception.MaskingException;

public class Logic {
	public static void main(String[] args) {
		try{
			// defaultn� nastaven� - pozdeji nebude potreba diky GUI
			String inputFile = "output/ExampleData.txt";
			String outputFile = "output/out2.txt";
			String maskingSettingsFile = "output/maskingsetting.txt";
			if (args.length > 0) {
				List<String> list = java.util.Arrays.asList(args);
				if (list.indexOf("--help") > -1 || list.indexOf("-h") > -1 || args.length < 2) {
					printHelp();
				}
				else if(args.length >= 2){
					Boolean invalidFiles = args[args.length-1].charAt(0) == '-' || args[args.length-2].charAt(0) == '-';
					if(invalidFiles){
						printHelp();
					}
					
					inputFile = args[args.length-2];
					outputFile = args[args.length-1];
					
					if (list.indexOf("-c") > -1){
						maskingSettingsFile = args[list.indexOf("-c")+1];
					}
				}
			}
			
			int lines = 100000;
			int header = 3;
			
			FileReader fReader = new FileReader(inputFile);
			DatabaseReader dReader = new DatabaseReader(fReader.readNLines(header));
			DatabaseWriter writer = new DatabaseWriter(outputFile, dReader.getHeader());
			Masker masker = new Masker(maskingSettingsFile);
			String[] input;
			String[][] database;
			try {
				writer.prepareFile();
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
			
			while((input = fReader.readNLines(lines))[0] != null){
				dReader.input = input;
				database = dReader.read();
				database = masker.mask(database);
				try {
					writer.append(database, input);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			
			writer.closeFile();
		}catch(MaskingException e){
			System.err.println("Error: "+e.getMessage());
		}
	}
	
	public static void printHelp() {
		Logger.log("Usage: <OPTIONS> <INPUT DATABASE> <OUTPUT FILE>\n" +
				"Mask database in INPUT DATABASE to OUTPUT FILE\n" +
				"	-c CONFIG FILE		obtain masking configuration from CONFIG FILE");
		System.exit(0);
	}
}
