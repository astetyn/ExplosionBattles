package main.player.shop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class ShopInventory {

	private Inventory inventory;
	private PlayerEB playerEB;
	private List<ShopItem> shopItemsWeapons;
	private List<ShopItem> shopItemsKits;
	
	public ShopInventory(PlayerEB playerEB, List<ShopItem> shopItemsKits, List<ShopItem> shopItemsWeapons) {
		this.playerEB = playerEB;
		this.shopItemsKits = shopItemsKits;
		this.shopItemsWeapons = shopItemsWeapons;
		inventory = Bukkit.createInventory(null, 36,ChatColor.BLUE+""+ChatColor.BOLD+"-= SHOP =-");
		reloadItems();
	}
	
	public void openInventory() {
		reloadItems();
		playerEB.getPlayer().openInventory(inventory);
	}
	
	public void reloadItems() {
		for(int j=0;j<9;j++) {
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 5);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW+""+ChatColor.BOLD+"Kity");
			is.setItemMeta(im);
			inventory.setItem(j, is);
		}
		int i = 9;
		for(ShopItem si : shopItemsKits) {

			ItemStack is = si.getItem();
			ItemMeta im = is.getItemMeta();
			
			String name = im.getDisplayName();
			String newName = ChatColor.GRAY+""+ChatColor.BOLD+"Kit "+ChatColor.RESET+name;
			im.setDisplayName(newName);
			
			List<String> lore = im.getLore();
			lore.add("");
			lore.add(ChatColor.BLUE+"Cena: "+si.getPrice());
			if(si.isAvaibleForVip()) {
				lore.add(ChatColor.GRAY+"Zadarmo pre VIP: "+ChatColor.GREEN+"ÁNO");
			}else {
				lore.add(ChatColor.GRAY+"Zadarmo pre VIP: "+ChatColor.RED+"NIE");
			}
			
			if(playerEB.getUserAccount().hasBoughtItem(si.getIndex())) {
				lore.add(ChatColor.GREEN+"ZAKÚPENÉ");
			}

			im.setLore(lore);
			is.setItemMeta(im);
			inventory.setItem(i, is);
			i++;
		}
		for(int j=18;j<27;j++) {
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 5);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW+""+ChatColor.BOLD+"Weapons");
			is.setItemMeta(im);
			inventory.setItem(j, is);
		}
		i = 27;
		for(ShopItem si : shopItemsWeapons) {
			
			ItemStack is = si.getItem();
			ItemMeta im = is.getItemMeta();
			
			String name = im.getDisplayName();
			String newName = ChatColor.GRAY+""+ChatColor.BOLD+"Zbraň "+ChatColor.RESET+name;
			im.setDisplayName(newName);
			
			List<String> lore = im.getLore();
			lore.add("");
			lore.add(ChatColor.BLUE+"Cena: "+si.getPrice());
			if(si.isAvaibleForVip()) {
				lore.add(ChatColor.GRAY+"Zadarmo pre VIP: "+ChatColor.GREEN+"ÁNO");
			}else {
				lore.add(ChatColor.GRAY+"Zadarmo pre VIP: "+ChatColor.RED+"NIE");
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

}
