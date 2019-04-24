package main.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import main.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class KitChooser {

	PlayerEB playerEB;
	
	private Inventory kitInventory;
	private static List<Kit> kits= new ArrayList<Kit>();
	
	static {
		
		kits.add(new BasicKit());
		kits.add(new Architect());
		
	}
	
	public KitChooser(PlayerEB playerEB) {
		this.playerEB = playerEB;
		this.kitInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD+""+ChatColor.BOLD+"  -= Kit chooser =-  ");
		addKits();
		showInventory();
	}
	
	private void addKits() {
		int i = 0;
		for(Kit kit : kits) {
			kitInventory.setItem(i,  kit.getChooseItem());
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
