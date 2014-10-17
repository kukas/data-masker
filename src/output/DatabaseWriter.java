package output;

import input.Header;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DatabaseWriter {
	private String path;
	private Header header;
	private PrintWriter writer;

	public DatabaseWriter(String outFile, Header dHeader) {
		path = outFile;
		header = dHeader;
	}

	public void write(String[][] data) throws IOException {
		// String[] headerString = header.getLines();
		int[] headerLengths = header.getLengths();

		/*
		 * PrintWriter writer = new PrintWriter(path);
		 * 
		 * for (int i=0; i<headerString.length; i++) { writer.println(headerString[i]); }
		 * 
		 * writer.println("");
		 */
		int colCount = data[0].length;
		for (int i = 0; i < data.length; i++) {
			String newLine = "";
			String delimiter = "";
			for (int j = 0; j < colCount; j++) {
				newLine += delimiter + String.format("%1$" + headerLengths[j] + "s", data[i][j]);
				delimiter = " ";
				// newLine[j] = StringUtils.leftPad(data[i][j], headerLengths[j], ' ');
			}
			writer.println(newLine);
		}

		writer.close();
	}

	public void prepareFile() throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));

		/*String[] headerString = header.getLines();

		for (int i = 0; i < headerString.length; i++) {
			writer.println(headerString[i]);
		}

		writer.println("");*/
	}

	public void append(String[][] data, String[] input) {
		int[] headerLengths = header.getLengths();
		int[] headerOffsets = header.getOffsets();

		for (int i = 0; i < data.length; i++) {
			String newLine = input[i];

			int lineLength = newLine.length();
			for (int j = 0; j < headerLengths.length; j++) {
				int offset = headerOffsets[j];
				int length = headerLengths[j];

				newLine = newLine.substring(0, offset) + data[i][j] + newLine.substring(offset+length);
			}

			writer.println(newLine);
		}
	}

	public void closeFile() {
		writer.close();
	}
}
