package input;

public class Header {
	String[] lines;
	int[] lengths;
	
	public Header(String[] lines){
		String[] split = lines[1].split(" ");
		
		lengths = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			lengths[i] = split[i].length();
		}
		this.lines = lines;
	}
	
	public String[] getLines(){
		return lines;
	}
	
	public int[] getLengths(){
		return lengths;
	}
}
