package main.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {

	private JavaPlugin plugin;
	private FileConfiguration config;
	
	public Configuration(JavaPlugin plugin) {
		this.plugin = plugin;
		loadFolders();
		loadConfiguration();
	}
	
	public void loadFolders() {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		File mapFolder = new File(plugin.getDataFolder()+"/maps");
		if(!mapFolder.exists()) {
			mapFolder.mkdirs();
		}
	}
	
	public void loadConfiguration(){
		plugin.reloadConfig();
		plugin.saveDefaultConfig();
		config = plugin.getConfig();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
	public FileConfiguration getConfig() {
		loadConfiguration();
		return config;
	}
}
