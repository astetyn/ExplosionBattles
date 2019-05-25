package main.player.layouts;

import org.bukkit.inventory.ItemStack;

public class InventoryLayoutItem {

	private ItemStack is;
	private int position;
	private ItemInteractAction action;
	
	public InventoryLayoutItem(ItemStack is, int position, ItemInteractAction action) {
		this.is = is;
		this.position = position;
		this.action = action;
	}

	public ItemStack getIs() {
		return is;
	}

	public int getPosition() {
		return position;
	}

	public ItemInteractAction getAction() {
		return action;
	}

}
