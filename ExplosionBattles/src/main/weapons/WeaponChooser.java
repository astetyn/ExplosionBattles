package main.weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class WeaponChooser {

PlayerEB playerEB;
	
	private Inventory weaponInventory;
	private static List<Weapon> weapons = new ArrayList<Weapon>();
	private static HashMap<ItemStack,Weapon> itemsWeapons = new HashMap<ItemStack,Weapon>();
	
	static {
		
		weapons.add(new AssaultShooter(null));
		weapons.add(new LightSniper(null));
		weapons.add(new MiniGun(null));
		weapons.add(new HeavyExplosiveSniper(null));
		
	}
	
	public WeaponChooser(PlayerEB playerEB) {
		this.playerEB = playerEB;
		this.weaponInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD+""+ChatColor.BOLD+centerTitle("-= Weapon chooser =-"));
		addWeapons();
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
	
	private void addWeapons() {
		int i = 0;
		for(Weapon weapon : weapons) {
			ItemStack is = weapon.getItem().clone();
			int weaponPrice = weapon.getPrice();
			if(weaponPrice==-1) {
				ItemMeta im = is.getItemMeta();
				List<String> l = im.getLore();
				l.add(ChatColor.RED+"NEKUPITELNE. Da sa");
				l.add(ChatColor.RED+"ziskat iba z air dropov.");
				im.setLore(l);
				is.setItemMeta(im);
			}else {
				ItemMeta im = is.getItemMeta();
				List<String> l = im.getLore();
				l.add(ChatColor.BLUE+"Cena: "+weaponPrice);
				im.setLore(l);
				is.setItemMeta(im);
			}
			itemsWeapons.put(is, weapon);
			weaponInventory.setItem(i, is);
			i++;
		}
	}
	
	private void showInventory() {
		playerEB.getPlayer().openInventory(weaponInventory);
	}

	public static List<Weapon> getWeapons() {
		return weapons;
	}
	
	public static HashMap<ItemStack,Weapon> getItemsWeapons() {
		return itemsWeapons;
	}

}
