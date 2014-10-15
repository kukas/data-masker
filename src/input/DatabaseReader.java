package input;
import input.Header;

public class DatabaseReader {
	public String[] input;
	Header header;

	public DatabaseReader(String[] input) {
		String[] headerLines = new String[2];
		headerLines[0] = input[0];
		headerLines[1] = input[1];
		header = new Header(headerLines);
		//this.input = new String[input.length - 3];
		//System.arraycopy(input, 3, this.input, 0, this.input.length); //preskoci prvni tri radky (hlavicka)
	}

	public String[][] read() {
		int[] lengths = header.getLengths();
		String[][] result = new String[input.length][lengths.length];
		int curPos;
		for (int i = 0; i < input.length; i++) {
			curPos = 0;
			for (int j = 0; j < lengths.length; j++) {
				result[i][j] = input[i].substring(curPos, curPos + lengths[j]);
				curPos += lengths[j] + 1;
			}
		}
		return result;
	}

	public Header getHeader() {
		return header;
	}
}
