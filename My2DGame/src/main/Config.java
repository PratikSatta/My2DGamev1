package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

	GamePanel gp;
	
	public Config(GamePanel gp) {
		this.gp = gp;
	}
	
	public void saveConfig() {
		
		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter("config.txt"));
			
			// Full Screen
			if (gp.fullScreenOn == true) {
				bWriter.write("On");
			}
			if (gp.fullScreenOn == false) {
				bWriter.write("Off");
			}
			bWriter.newLine(); 
			
			//Music Volume
			bWriter.write(String.valueOf(gp.music.volumeScale));
			bWriter.newLine();
			
			//SE Volume
			bWriter.write(String.valueOf(gp.soundEffect.volumeScale));
			bWriter.newLine();
			
			bWriter.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	public void loadConfig() {
		
		try {
			BufferedReader bReader = new BufferedReader(new FileReader("config.txt"));
			
			String string = bReader.readLine();
			
			//Full screen
			if (string.equals("On")) {
				gp.fullScreenOn = true;
			}
			if (string.equals("Off")) {
				gp.fullScreenOn = false;
			}
			
			//Music volume
			string = bReader.readLine();
			gp.music.volumeScale = Integer.parseInt(string);
			
			//SE Volume
			string = bReader.readLine();
			gp.music.volumeScale = Integer.parseInt(string);
			
			bReader.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
}
