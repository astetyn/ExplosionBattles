package main.player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import main.Main;
import main.MsgCenter;
import net.md_5.bungee.api.ChatColor;

public class UserAccount {

	private FileConfiguration config;
	private File accountFile;
	private final String DATABASE_PATH = Main.getPlugin().getDataFolder().getPath()+"/accounts";
	private PlayerEB playerEB;
	
	public UserAccount(PlayerEB playerEB) {
		this.playerEB = playerEB;
		checkDatabase();
		accountFile = new File(DATABASE_PATH+"/"+playerEB.getPlayer().getName()+".yml");
		this.config = YamlConfiguration.loadConfiguration(accountFile);
		config.addDefault("coins", 0);
		config.addDefault("epoints", 0);
		config.addDefault("items", Arrays.asList("kit_basic","weapon_assaultshooter"));
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	private void saveConfig() {
		try {
			config.save(accountFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addCoinsWithNotification(int coins) {
		int actualCoins = getCoins();
		actualCoins+=coins;
		setCoins(actualCoins);
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+""+ChatColor.BOLD+"+"+coins+" coins");
	}
	
	public void addEPointsWithNotification(int points) {
		int actualEPoints = getEPoints();
		actualEPoints+=points;
		setEPoints(actualEPoints);
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.BLUE+""+ChatColor.BOLD+"+"+points+" EP");
	}
	
	private void setEPoints(int points) {
		config.set("epoints",points);
		saveConfig();
	}
	
	public int getEPoints() {
		int points = config.getInt("epoints");
		return points;
	}
	
	public void setCoins(int coins) {
		config.set("coins",coins);
		saveConfig();
	}
	
	public int getCoins() {
		int coins = config.getInt("coins");
		return coins;
	}
	
	@SuppressWarnings("unchecked")
	public void addItem(String index) {
		List<String> items = (List<String>) config.get("items");
		items.add(index);
		config.set("items", items);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getItems() {
		List<String> items = (List<String>) config.get("items");
		return items;
	}
	
	public boolean hasBoughtItem(String index) {
		for(String s : getItems()) {
			if(s.equals(index)) {
				return true;
			}
		}
		return false;
	}
	
	private void checkDatabase() {
		File databaseFolder = new File(DATABASE_PATH);
		if(!databaseFolder.exists()) {
			databaseFolder.mkdirs();
		}
	}
	
}
