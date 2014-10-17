package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileReader {
	public static Charset basicCharset = StandardCharsets.UTF_8;
	public BufferedReader reader;
	public Path path;
	public Charset charset;
	public boolean initialized = false;
	public ArrayList<String> lines = new ArrayList<String>();
	
	public FileReader(String from){
		File file = new File(from);
		path = file.toPath();
		
		charset = StandardCharsets.UTF_8;
		
	}
	
	public FileReader (String fileName, String _charset){
		File file = new File(fileName);
		path = file.toPath();
		if(_charset == "UTF-8"){
			charset = StandardCharsets.UTF_8;
		}
	}
	
	public void readFile (){
		// NemÃ¡ smysl Ä�Ã­st, pokud nenÃ­ initnuto
		if(initialized){
			try (BufferedReader fReader = reader){
				String line = null;
				while((line = fReader.readLine()) != null){
					lines.add(line);
				}
			}
			catch (IOException ex){
				System.err.println();
			}
		}
		else{
			if(initReader())
				readFile();
		}
	}
	
	public String readLine(){
		// NemÃ¡ smysl Ä�Ã­st, pokud nenÃ­ initnuto
		if(initialized){
			String line = null;
			try {
				line = reader.readLine();
				return line;
			}
			catch (IOException ex){
				System.err.println("Ztracen reader v read line");
				System.err.println(ex);
				return null;
			}
		}
			if(initReader())
				return readLine();
			else 
				return null;
	}
	
	public boolean initReader(){
		// VytvoÅ™it fileReader
		try {
			reader = (BufferedReader) Files.newBufferedReader(path, charset);
			initialized = true;
			return true;
		}
		catch (IOException ex) {
			System.err.println(ex);
			initialized = false;
			return false;
		}
	}
	
	public String[] readNLines(int n){
		String[] lines = new String[n];
		String line = "1";
		for(int i = 0; i < n && (line = readLine()) != null; i++){
			lines[i] = line;
		};
		int celkem = 0;
		while(celkem < lines.length){
			if(lines[celkem] == null){
				break;
			}
			celkem++;
		};
		String[] finalArray;
		if(celkem == 0){
			finalArray = new String[1];
			finalArray[0] = null;
		}
		else{
			finalArray = new String[celkem];
		}
		System.arraycopy(lines, 0, finalArray, 0, celkem);
		
			
		return finalArray;
	}
	
	public String[] read(){
		try ( BufferedReader fileReader = Files.newBufferedReader(path, charset) ){
			reader = fileReader;
			String line = "";
			while((line = reader.readLine()) != null){
				lines.add(line);
			}
			
			return lines.toArray(new String[0]);
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
		return null;
	}
}
