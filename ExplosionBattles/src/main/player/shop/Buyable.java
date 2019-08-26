package main.player.shop;

import org.bukkit.inventory.ItemStack;

public interface Buyable {

	public ItemStack getItem();
	
	public String getIndex();

	public int getPrice();

}
