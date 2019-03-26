package main.maps;

import java.io.File;

import main.Main;

public class LobbySystemChecker {

	File mapFolder = new File(Main.getPlugin().getDataFolder()+"/maps");
	
	public boolean lobbyExists() {
		
		for(File f : mapFolder.listFiles()) {
			String name = f.getName();
			name = name.replaceAll(".yml", "");
			if(name.equals("lobby")) {
				return true;
			}
		}
		return false;
	}
}
