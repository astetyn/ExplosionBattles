package main.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.Main;

public class MapChecker {

	List<File> files = new ArrayList<File>();
	List<String> fileNames = new ArrayList<String>();
	
	public void loadFiles() {
		File folder = Main.getPlugin().getDataFolder();
		for(File f : folder.listFiles()) {
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
		for(String name : fileNames) {
			WorldConfiguration wc = new WorldConfiguration(Main.getPlugin(),name);
			int spawns = wc.getSpawns();
			p.sendMessage("Nacitana mapa: "+name+" spravena na "+ spawns+"/6");
		}
	}
	
	public List<String> getMaps() {
		return fileNames;
	}
	
}
