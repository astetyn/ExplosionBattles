package main.inventory;

import org.bukkit.inventory.ItemStack;

public class ShopItem {
	
	private String index;
	private ItemStack is;
	private int price;
	private boolean avaibleForVip;
	
	public ShopItem(String index, ItemStack is, int price, boolean avaibleForVip) {
		this.index = index;
		this.is = is;
		this.price = price;
		this.avaibleForVip = avaibleForVip;
	}

	public String getIndex() {
		return index;
	}
	
	public ItemStack getIs() {
		return is;
	}

	public int getPrice() {
		return price;
	}

	public boolean isAvaibleForVip() {
		return avaibleForVip;
	}

}
