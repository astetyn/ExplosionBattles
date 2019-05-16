package main.maps;

import org.bukkit.inventory.ItemStack;

public class MapVoteItem {

	private ItemStack is;
	private int index;
	
	public MapVoteItem(ItemStack is, int index) {
		this.is = is;
		this.index = index;
	}

	public ItemStack getIs() {
		return is;
	}

	public int getIndex() {
		return index;
	}

}
