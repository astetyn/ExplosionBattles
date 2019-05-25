package main.player.shop;

import org.bukkit.inventory.ItemStack;

public interface ShopItem {

	public ItemStack getItem();
	
	public String getIndex();

	public int getPrice();

	public boolean isAvaibleForVip();
	
	public boolean isLimited();

}
