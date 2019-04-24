package main.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.Main;
import main.configuration.WorldConfiguration;

public class MapPlayerChecker {

	List<File> files = new ArrayList<File>();
	List<String> fileNames = new ArrayList<String>();
	File mapFolder = new File(Main.getPlugin().getDataFolder()+"/maps");
	
	public void loadFiles() {

		for(File f : mapFolder.listFiles()) {
			String name = f.getName();
			name = name.replaceAll(".yml", "");
			if(name.equals("config")||name.equals("lobby")) {
				continue;
			}
			fileNames.add(name);
			files.add(f);
		}
	}
	
	public void showList(Player p) {
		
		if(fileNames.isEmpty()) {
			p.sendMessage("Ziadne mapy este neboli vytvorene. Vytvoris ich cez /ebsetup create <nazov>");
		}
		
		for(String name : fileNames) {
			WorldConfiguration wc = new WorldConfiguration(name);
			int spawns = wc.getSpawns();
			p.sendMessage("Nacitana mapa: "+name+" hotova na "+ spawns+"/6" + " a spectator: "+wc.spectatorExists());
		}
	}
	
	public List<String> getMaps() {
		return fileNames;
	}
	
}
