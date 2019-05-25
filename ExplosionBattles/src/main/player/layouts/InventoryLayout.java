package main.player.layouts;

import org.bukkit.inventory.ItemStack;

public abstract class InventoryLayout {

	public abstract void initItems();
	
	public abstract void putItems();
	
	public abstract boolean onInteract(ItemStack is);
	
}
