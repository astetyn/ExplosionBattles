package main.player.consumables;

import main.player.shop.ShopItem;

public interface Consumable extends ShopItem {

	public void onInteract();
	
}
