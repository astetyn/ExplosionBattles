package main.weapons.data;

import main.player.shop.ShopItem;

public abstract class WeaponData implements ShopItem {
	
	public abstract int getPower();
	
	public abstract String getAccuracy();
	
	public abstract String getManualMsg();

	public abstract double getDefaultCooldown();
	
}
