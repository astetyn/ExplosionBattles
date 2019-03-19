package main.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {

	private JavaPlugin plugin;
	private FileConfiguration config;
	
	public Configuration(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void loadConfiguration(){
		plugin.saveDefaultConfig();
		config = plugin.getConfig();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
}
