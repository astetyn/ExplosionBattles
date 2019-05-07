package main.player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import main.Main;
import main.inventory.ShopItem;
import net.md_5.bungee.api.ChatColor;

public class BankAccount {

	private FileConfiguration config;
	private File accountFile;
	private final String DATABASE_PATH = Main.getPlugin().getDataFolder().getPath()+"/accounts";
	private PlayerEB playerEB;
	
	public BankAccount(PlayerEB playerEB) {
		this.playerEB = playerEB;
		checkDatabase();
		accountFile = new File(DATABASE_PATH+"/"+playerEB.getPlayer().getName()+".yml");
		this.config = YamlConfiguration.loadConfiguration(accountFile);
		config.addDefault("coins", 0);
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
	
	public void wantsToBuy(ShopItem shopItem) {
		String index1 = shopItem.getIndex();
		if(getItems().contains(index1)) {
			playerEB.getPlayer().sendMessage("Toto uz mas zakupene.");
			return;
		}
		int price = shopItem.getPrice();
		int playersCoins = getCoins();
		if(playersCoins<price) {
			playerEB.getPlayer().sendMessage("Nemas dost penazi.");
			return;
		}
		playerEB.getPlayer().sendMessage("Prebieha transakcia...");
		int difference = playersCoins - price;
		setCoins(difference);
		addItem(shopItem.getIndex());
		playerEB.getPlayer().sendMessage(ChatColor.GREEN+"Transakcia prebehla uspesne, kupil si si novu vec!");
	}
	
	private void setCoins(int coins) {
		config.set("coins",coins);
		saveConfig();
	}
	
	private int getCoins() {
		int coins = config.getInt("coins");
		return coins;
	}
	
	@SuppressWarnings("unchecked")
	private void addItem(String index) {
		List<String> items = (List<String>) config.get("items");
		items.add(index);
		config.set("items", items);
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getItems() {
		List<String> items = (List<String>) config.get("items");
		return items;
	}
	
	public boolean hadBoughtItem(String index) {
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
