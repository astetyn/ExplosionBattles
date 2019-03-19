package main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.configuration.Configuration;
import main.listeners.CommandExecutor;

public class Main extends JavaPlugin {
	
	private static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		Configuration c = new Configuration(plugin);
		c.loadConfiguration();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandExecutor ce = new CommandExecutor(sender, cmd, label, args);
		return ce.onCommand();
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}
	
}
