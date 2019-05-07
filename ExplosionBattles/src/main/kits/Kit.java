package main.kits;

import org.bukkit.inventory.ItemStack;

import main.Buyable;
import main.player.PlayerEB;
import main.weapons.Weapon;

public abstract class Kit implements Cloneable, Buyable {

	private PlayerEB playerEB;
	private Weapon defaultWeapon;
	
	public void startInit() {
	}
	
	public void onInteract(ItemStack it) {
		
	}
	
	public void tick() {
	}
	
	public abstract ItemStack getItem();

	public void setPlayer(PlayerEB playerEB) {
		this.playerEB = playerEB;
	}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}
	
	public Object clone() { 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null; 
	}

	public Weapon getDefaultWeapon() {
		return defaultWeapon;
	}

	public void setDefaultWeapon(Weapon defaultWeapon) {
		this.defaultWeapon = defaultWeapon;
	}
}
