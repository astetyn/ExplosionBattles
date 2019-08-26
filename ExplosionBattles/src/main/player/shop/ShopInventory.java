package main.player.shop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import main.weapons.Weapon;

public class ShopInventory {

	private Inventory inventory;
	private PlayerEB playerEB;
	private List<Buyable> shopItemsWeapons;
	private List<Buyable> shopItemsKits;
	private List<Buyable> shopItemsConsumables;
	
	public ShopInventory(PlayerEB playerEB, List<Buyable> shopItemsKits, List<Buyable> shopItemsWeapons, List<Buyable> shopItemsConsumables) {
		this.playerEB = playerEB;
		this.shopItemsKits = shopItemsKits;
		this.shopItemsWeapons = shopItemsWeapons;
		this.shopItemsConsumables = shopItemsConsumables;
		inventory = Bukkit.createInventory(null, 54,ChatColor.BLUE+""+ChatColor.BOLD+"-= Armoury =-");
		reloadItems();
	}
	
	public void openInventory() {
		reloadItems();
		playerEB.getPlayer().openInventory(inventory);
	}
	
	private void insertGlass(int from, int color, String name) {
		for(int j=from;j<from+9;j++) {
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short)color);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(name);
			is.setItemMeta(im);
			inventory.setItem(j, is);
		}
	}
	
	private void insertItems(int from, List<Buyable> items) {
		int i = from;
		for(Buyable si : items) {

			ItemStack is = si.getItem();
			ItemMeta im = is.getItemMeta();
			
			String name = im.getDisplayName();
			String newName = "";
			if(si instanceof Weapon) {
				newName = ChatColor.GRAY+""+ChatColor.BOLD+"Zbraň "+ChatColor.RESET+name;
			}else {
				newName = ChatColor.GRAY+""+ChatColor.BOLD+"Kit "+ChatColor.RESET+name;
			}
			im.setDisplayName(newName);
			
			List<String> lore = im.getLore();
			lore.add("");
			if(si.getPrice()!=-1) {
				lore.add(ChatColor.BLUE+"Cena: "+si.getPrice());
			}else {
				lore.add(ChatColor.RED+"Nedostupné.");
			}
			
			if(playerEB.getUserAccount().hasBoughtItem(si.getIndex())) {
				lore.add(ChatColor.GREEN+"ZAKÚPENÉ");
			}
			im.setLore(lore);
			is.setItemMeta(im);
			inventory.setItem(i, is);
			i++;
		}
	}
	
	private void insertConsumables(int from, List<Buyable> items) {
		int i = from;
		for(Buyable si : items) {

			ItemStack is = si.getItem();
			ItemMeta im = is.getItemMeta();
			
			List<String> lore = im.getLore();
			if(si.getPrice()!=-1) {
				lore.add(ChatColor.BLUE+"Cena: "+si.getPrice());
			}else {
				lore.add(ChatColor.RED+"Nedostupné.");
			}
			
			int bought = playerEB.getConsumablesManager().getBought(si);
			ChatColor gray = ChatColor.GRAY;
			ChatColor gold = ChatColor.GOLD;
			lore.add(gray+"Zakúpené "+gold+bought+gray+"/"+gold+playerEB.getConsumablesManager().getMaxBought());
			
			im.setLore(lore);
			is.setItemMeta(im);
			inventory.setItem(i, is);
			i++;
		}
	}
	
	public void reloadItems() {
		insertGlass(0,5,ChatColor.YELLOW+""+ChatColor.BOLD+"Kity");
		insertItems(9, shopItemsKits);
		insertGlass(18,5,ChatColor.YELLOW+""+ChatColor.BOLD+"Zbrane");
		insertItems(27, shopItemsWeapons);
		insertGlass(36,1,ChatColor.YELLOW+""+ChatColor.BOLD+"Jednorázové predmety");
		insertConsumables(45, shopItemsConsumables);	
	}

}
