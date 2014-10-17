package gui;

import input.FileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import log.Logger;

public class ConfigSaver {
	String[] config;
	public ConfigSaver(){
		config = read();
	}
	
	public void write(String[]settings){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("appSettings.txt"));
				//1. radek: input, 2. radek: output, 3. radek rules
				for(int i = 0; i<3; i++){
				bw.write(settings[i]);
				bw.newLine();
				//Logger.debug(settings[i]);
				}
			
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public String[] read(){
		File f = new File("appSettings.txt");
		if(f.exists()){
		FileReader fr = new FileReader("appSettings.txt","UTF-8");
		return(fr.read());
		}
		String[] out = {"","",""};
		return out;
	}
	
}
