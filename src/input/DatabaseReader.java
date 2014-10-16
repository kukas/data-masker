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
	
	public DatabaseReader(int[] lengths, int[] offsets){
		header = new Header(lengths, offsets);
	}

	public String[][] read() {
		int[] lengths = header.getLengths();
		int[] offsets = header.getOffsets();
		System.out.println(lengths[0]);
		String[][] result = new String[input.length][offsets.length];
		int curPos;
		for (int i = 0; i < input.length; i++) {
			curPos = offsets[0];
			for (int j = 0; j < offsets.length; j++) {
				curPos = offsets[j];
				result[i][j] = input[i].substring(curPos, curPos + lengths[j]);
			}
		}
		return result;
	}

	public Header getHeader() {
		return header;
	}
}
