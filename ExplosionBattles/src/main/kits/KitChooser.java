package main.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class KitChooser {

	PlayerEB playerEB;
	
	private Inventory kitInventory;
	private static List<Kit> kits= new ArrayList<Kit>();
	
	static {
		
		kits.add(new BasicKit(null));
		kits.add(new Architect(null));
		kits.add(new Engineer(null));
		kits.add(new Camper(null));
		
	}
	
	public KitChooser(PlayerEB playerEB) {
		this.playerEB = playerEB;
		this.kitInventory = Bukkit.createInventory(null, 9,centerTitle(ChatColor.GOLD+""+ChatColor.BOLD+"-= Kit chooser =-"));
		addKits();
		showInventory();
	}
	
	private String centerTitle(String title) {
	    StringBuilder result = new StringBuilder();
	    int spaces = (27 - ChatColor.stripColor(title).length());
	 
	    for (int i = 0; i < spaces; i++) {
	        result.append(" ");
	    }
	    return result.append(title).toString();
	}
	
	private void addKits() {
		int i = 0;
		for(Kit kit : kits) {
			kitInventory.setItem(i,  kit.getItem());
			i++;
		}
	}
	
	private void showInventory() {
		playerEB.getPlayer().openInventory(kitInventory);
	}
	
	public static List<Kit> getKits(){
		return kits;
	}

}
