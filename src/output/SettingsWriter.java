package output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class SettingsWriter {
	
	public void write(String address, Vector<Vector<Object>> data){
		
		Vector arrayData[] = data.toArray(new Vector[0]);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(address));
			for(int i = 0; i < data.size(); i++){
				String s = "";
				for(int j = 0; j < data.get(i).size(); j++){
				
				if(data.get(i).get(j).equals(null)){
					s += "";
				}else{
					s += data.get(i).get(j);
				}
				
				if(!(j == data.get(i).size() - 1)){
					s+= ";";
				}
				
				}
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}	
}
