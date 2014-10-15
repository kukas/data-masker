package logic;

import input.DatabaseReader;
import input.FileReader;
import log.Logger;
import masking.Masker;
import output.DatabaseWriter;

public class Logic {
	public static void main(String[] args) {
		//"../raw_data/out/jmenoprijmeni25.txt"
		/*FileReader fReader = new FileReader("ExampleData.txt");
		DatabaseReader dReader = new DatabaseReader(fReader.read());
		String[][] database = dReader.read();
		Masker masker = new Masker();
		database = masker.mask(database, "maskingsetting.txt");
		DatabaseWriter writer = new DatabaseWriter("out.txt", dReader.getHeader());
		try {
			writer.write(database);
		} catch (Exception e) {
			Logger.log(e.getMessage());
		}*/
		int lines = 100;
		int header = 3;
		
		FileReader fReader = new FileReader("../raw_data/out/jmenoprijmeniGB.txt");
		DatabaseReader dReader = new DatabaseReader(fReader.readNLines(header));
		DatabaseWriter writer = new DatabaseWriter("out.txt", dReader.getHeader());
		Masker masker = new Masker("maskingsetting.txt");
		String[] input;
		String[][] database;
		while((input = fReader.readNLines(lines))[0] != null){System.out.println("Hell");
			dReader.input = input;
			database = dReader.read();
			database = masker.mask(database);
			try {
				writer.write(database);
			} catch (Exception e) {
				Logger.log(e.getMessage());
			}
		}
	}
}
