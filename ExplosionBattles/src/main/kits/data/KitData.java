package main.kits.data;

import main.player.shop.ShopItem;
import main.weapons.data.WeaponData;

public abstract class KitData implements ShopItem {
	
	public abstract WeaponData getWeaponData();
	
}
