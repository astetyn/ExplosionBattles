package main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.configuration.Configuration;
import main.listeners.CommandExecutor;
import main.listeners.EntityDamageListener;
import main.listeners.EntityExplodeListener;
import main.listeners.EntityItemListener;
import main.listeners.FoodLevelChangeListener;
import main.listeners.PlayerBlockListener;
import main.listeners.PlayerInteractListener;
import main.listeners.PlayerInventoryListener;
import main.listeners.PlayerLeaveListener;

public class Main extends JavaPlugin {
	
	private static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerInteractListener(),this);
		pluginManager.registerEvents(new PlayerBlockListener(),this);
		pluginManager.registerEvents(new PlayerInventoryListener(),this);
		pluginManager.registerEvents(new PlayerLeaveListener(),this);
		pluginManager.registerEvents(new EntityExplodeListener(),this);
		pluginManager.registerEvents(new EntityItemListener(),this);
		pluginManager.registerEvents(new EntityDamageListener(),this);
		pluginManager.registerEvents(new FoodLevelChangeListener(),this);
		
		Configuration c = new Configuration(plugin);
		c.loadFolders();
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
