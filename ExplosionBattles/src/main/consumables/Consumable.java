package main.consumables;

import main.player.shop.Buyable;

public interface Consumable extends Buyable {

	public void onInteract();
	
}
