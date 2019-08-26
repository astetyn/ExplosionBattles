package main.weapons;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import main.gameobjects.Tickable;
import main.player.PlayerEB;
import main.player.shop.Buyable;

public abstract class Weapon implements Tickable, Buyable {

	private double cooldown;
	private long lastUse = 0;
	private PlayerEB playerEB;
	private double power;
	private String accuracy;
	private String manual;
	
	private final int DEFAULT_INV_SLOT = 0;
	
	/** Create basic object for use without player. */
	public Weapon(double cooldown, double power, String accuracy, String manual) {
		this.cooldown = cooldown;
		this.power = power;
		this.accuracy = accuracy;
		this.manual = manual;
	}
	
	/** Create basic object for player use. */
	public Weapon(PlayerEB playerEB, double cooldown, double power, String accuracy, String manual) {
		this.playerEB = playerEB;
		this.cooldown = cooldown;
		this.power = power;
		this.accuracy = accuracy;
		this.manual = manual;
	}
	
	public Weapon(Weapon weapon, PlayerEB playerEB) {
		this.playerEB = playerEB;
		this.cooldown = weapon.cooldown;
		this.power = weapon.power;
		this.accuracy = weapon.accuracy;
		this.manual = weapon.manual;
	}
	
	/** This method is called when player interacts in game with gun.
	 *  
	 * @return true, if event should be canceled
	 */
	public abstract boolean onInteract(PlayerInteractEvent event);
	
	/** This method give the player weapon item and show manual to the player.*/
	public void equip() {
		this.playerEB.getPlayer().getInventory().setItem(this.DEFAULT_INV_SLOT, getItem()); 
		this.playerEB.getPlayer().sendTitle(ChatColor.BLUE+""+ChatColor.BOLD+"Ovládanie zbrane", ChatColor.AQUA+""+ChatColor.ITALIC+this.manual,20,40,40);
	}
	
	public abstract ItemStack getItem();
	
	public double getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(double cooldown) {
		this.cooldown = cooldown;
	}
	
	public long getLastUse() {
		return lastUse;
	}

	public void setLastUse(long lastUse) {
		this.lastUse = lastUse;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}
}
