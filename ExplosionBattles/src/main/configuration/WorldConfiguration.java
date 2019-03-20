package main.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import main.Main;

public class WorldConfiguration {

	private FileConfiguration config;
	private String fileName;
	private File file;
	private final int MAX_PLAYERS = 6;
	private File mapFolder = new File(Main.getPlugin().getDataFolder()+"/maps");
	
	public WorldConfiguration(String mapName) {
		this.fileName = mapName+".yml";
		this.file = new File(mapFolder, fileName);
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void saveConfig() {
	    try {
	        config.save(file);
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	}
	
	public void createConfig() {
		config.addDefault("spawn-locs", 0);
		config.addDefault("night", false);
		config.addDefault("rain", false);
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public void deleteConfig() {
		file.delete();
	}
	
	public boolean addspawn(Location loc) {
		int spawnPoints = getSpawns();
		if(spawnPoints==MAX_PLAYERS) {
			return false;
		}
		String key = "loc"+(spawnPoints+1);
		config.set(key,loc);
		config.set("spawn-locs",spawnPoints+1);
		saveConfig();
		return true;
	}
	
	public boolean removespawn() {
		int spawnPoints = getSpawns();
		if(spawnPoints==0) {
			return false;
		}
		String key = "loc"+(spawnPoints);
		config.set(key,null);
		config.set("spawn-locs",spawnPoints-1);
		saveConfig();
		return true;
	}
	
	public void setSpawnSpecator(Location loc) {
		String key = "spec";
		config.set(key,loc);
		saveConfig();
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public int getSpawns() {
		return config.getInt("spawn-locs");
	}
	
	public boolean spectatorExists() {
		return config.contains("spec");
	}
	
	public boolean configExists() {
		File f = new File(mapFolder, fileName);
		if(!f.exists()) {
			return false;
		}else {
			return true;
		}
	}
	
}
