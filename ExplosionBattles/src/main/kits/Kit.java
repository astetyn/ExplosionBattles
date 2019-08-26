package main.kits;

import org.bukkit.inventory.ItemStack;

import main.gameobjects.Tickable;
import main.player.PlayerEB;
import main.player.shop.Buyable;
import main.weapons.Weapon;

public abstract class Kit implements Tickable, Buyable {

	private PlayerEB playerEB;
	private Weapon weapon;
	
	public Kit(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public Kit(PlayerEB playerEB, Weapon weapon) {
		this.playerEB = playerEB;
		this.weapon = weapon;
	}
	
	public void startInit() {}
	
	public void onInteract(ItemStack it) {}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
}
