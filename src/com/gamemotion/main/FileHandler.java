package com.gamemotion.main;

import java.io.File;

public class FileHandler {

	String Path = "plugins/YouTube";
	
	public void Setup(){
		File MainDirectory = new File(Path);
		if(!MainDirectory.exists()){
			MainDirectory.mkdir();
		}
		File PlayerData = new File(Path + "/PlayerData");
		if(!PlayerData.exists()){
			PlayerData.mkdir();
		}
	}
	
}
