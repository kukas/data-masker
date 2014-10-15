package output;

import input.Header;

import java.io.IOException;
import java.io.PrintWriter;

import log.Logger;

public class DatabaseWriter {
	private String path;
	private Header header;
	public DatabaseWriter(String outFile, Header dHeader){
		path = outFile;
		header = dHeader;
	}
	
	public void write(String[][] data) throws IOException{
		String[] headerString = header.getLines();
		int[] headerLengths = header.getLengths();

		PrintWriter writer = new PrintWriter(path);
		
		for (int i=0; i<headerString.length; i++) {
			writer.println(headerString[i]);
		}
		
		writer.println("");
		
		int colCount = data[0].length;
		for (int i=0; i<data.length; i++) {
			String newLine = "";
			String delimiter = "";
			for (int j=0; j<colCount; j++) {
				newLine += delimiter + String.format("%1$"+headerLengths[j]+ "s", data[i][j]);
				delimiter = " ";
				// newLine[j] = StringUtils.leftPad(data[i][j], headerLengths[j], ' ');
			}
			writer.println(newLine);
		}

		writer.close();
	}
}
