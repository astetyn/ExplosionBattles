package main.player.shop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import main.weapons.data.WeaponData;

public class ShopInventory {

	private Inventory inventory;
	private PlayerEB playerEB;
	private List<ShopItem> shopItemsWeapons;
	private List<ShopItem> shopItemsKits;
	private List<ShopItem> shopItemsConsumables;
	private List<ShopItem> shopItemsLimited;
	
	public ShopInventory(PlayerEB playerEB, List<ShopItem> shopItemsKits, List<ShopItem> shopItemsWeapons, List<ShopItem> shopItemsConsumables, List<ShopItem> shopItemsLimited) {
		this.playerEB = playerEB;
		this.shopItemsKits = shopItemsKits;
		this.shopItemsWeapons = shopItemsWeapons;
		this.shopItemsLimited = shopItemsLimited;
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
	
	private void insertOneGlass(int slot, int color, String name) {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short)color);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		inventory.setItem(slot, is);
	}
	
	private void insertHalfGlass(int from, int color, String name) {
		for(int j=from;j<from+4;j++) {
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short)color);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(name);
			is.setItemMeta(im);
			inventory.setItem(j, is);
		}
	}
	
	private void insertItems(int from, List<ShopItem> items) {
		int i = from;
		for(ShopItem si : items) {

			ItemStack is = si.getItem();
			ItemMeta im = is.getItemMeta();
			
			String name = im.getDisplayName();
			String newName = "";
			if(si instanceof WeaponData) {
				newName = ChatColor.GRAY+""+ChatColor.BOLD+"Zbraň "+ChatColor.RESET+name;
			}else {
				newName = ChatColor.GRAY+""+ChatColor.BOLD+"Kit "+ChatColor.RESET+name;
			}
			im.setDisplayName(newName);
			
			List<String> lore = im.getLore();
			lore.add("");
			if(si.getPrice()!=-1) {
				lore.add(ChatColor.BLUE+"Cena: "+si.getPrice());
				if(si.isAvaibleForVip()) {
					lore.add(ChatColor.GRAY+"Zadarmo pre VIP: "+ChatColor.GREEN+"ÁNO");
				}else {
					lore.add(ChatColor.GRAY+"Zadarmo pre VIP: "+ChatColor.RED+"NIE");
				}
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
	
	private void insertConsumables(int from, List<ShopItem> items) {
		int i = from;
		for(ShopItem si : items) {

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
		insertHalfGlass(36,1,ChatColor.YELLOW+""+ChatColor.BOLD+"Limitovaná ponuka");
		insertOneGlass(40,4,ChatColor.YELLOW+""+ChatColor.BOLD+"_");
		insertHalfGlass(41,1,ChatColor.YELLOW+""+ChatColor.BOLD+"Jednorázové predmety");
		insertItems(45, shopItemsLimited);
		insertOneGlass(49,4,ChatColor.YELLOW+""+ChatColor.BOLD+"_");
		insertConsumables(50, shopItemsConsumables);	
	}

}
