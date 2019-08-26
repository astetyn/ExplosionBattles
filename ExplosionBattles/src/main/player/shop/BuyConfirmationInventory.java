package main.player.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class BuyConfirmationInventory {

	private ItemStack[] chooseItems = new ItemStack[2];
	private Inventory inventory;
	private Buyable shopItem;
	
	public BuyConfirmationInventory(PlayerEB playerEB, Buyable shopItem) {
		this.shopItem = shopItem;
		ItemStack air = new ItemStack(Material.AIR,1);
		ItemStack agree = new ItemStack(Material.EMERALD_BLOCK,1);
		ItemStack deny = new ItemStack(Material.REDSTONE_BLOCK,1);
		ItemMeta im = agree.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Kúpiť "+shopItem.getItem().getItemMeta().getDisplayName()
		+ChatColor.GREEN+" za "+ChatColor.GOLD+ChatColor.BOLD+shopItem.getPrice()+ChatColor.GREEN+ChatColor.BOLD+" coins");
		agree.setItemMeta(im);
		ItemMeta im2 = deny.getItemMeta();
		im2.setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"ZRUŠIŤ");
		deny.setItemMeta(im2);
		chooseItems[0] = agree;
		chooseItems[1] = deny;
		inventory = Bukkit.createInventory(null, 9,ChatColor.BLUE+""+ChatColor.BOLD+"-= Potvrdenie nákupu =-");
		ItemStack[] inventoryLayout = {air,agree,agree,agree,air,deny,deny,deny,air};
		inventory.setContents(inventoryLayout);
		playerEB.getPlayer().openInventory(inventory);
	}
	
	public ItemStack[] getChooseItems() {
		return chooseItems;
	}
	
	public Buyable getShopItem() {
		return shopItem;
	}

}
