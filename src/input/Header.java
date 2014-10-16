package input;

public class Header {
	String[] lines;
	int[] lengths;
	int[] offsets;
	
	public Header(String[] lines){
		String[] split = lines[1].split(" ");
		
		lengths = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			lengths[i] = split[i].length();
		}
		this.lines = lines;
	}
	
	public Header (String[] names,int[] lengths){
		this.lengths = lengths;
		String headerLine = "";
		for(int i = 0; i < this.lengths.length; i++){
			headerLine += names[i];
			for(int j = 0; j < this.lengths[i]-names[i].length(); j++){
				headerLine += " ";
			};
		};
		this.lines = new String[2];
		this.lines[0] = headerLine;
		this.lines[1] = "";
	}
	
	public Header (int[] lengths){
		this.lengths = lengths;
		this.lines = new String[2];
		this.lines[0] = "";
		this.lines[1] = "";
	}
	
	public Header(int[] lengths, int[] offsets){
		if(lengths.length != offsets.length){
			System.out.println("Different number of offsets and lengths, adding lengths");
			this.lengths = new int[offsets.length];
			for(int i = 0; i < lengths.length;i++){
				this.lengths[i] = lengths[i];
			};
			for(int i = lengths.length; i < offsets.length; i++){
				this.lengths[i] = 0;
			};
		}
		else{
			this.lengths = lengths;
			this.offsets = offsets;
		}
	}
	
	public String[] getLines(){
		return lines;
	}
	
	public int[] getOffsets(){
		return offsets;
	}
	
	public int[] getLengths(){
		return lengths;
	}
}
