package main.weapons;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import main.Buyable;
import main.player.PlayerEB;

public abstract class Weapon implements Cloneable, Buyable {

	private long lastUse;
	private double originalCooldown = -1;
	private double cooldown;
	private int power;
	private String accuracy;
	private ItemStack item;
	private PlayerEB playerEB;
	private boolean setThisRound = false;
	
	public Weapon() {
	}
	
	public boolean onInteract(PlayerInteractEvent event) {
		return true;
	}

	public double getCooldown() {
		return cooldown;
	}

	public void setCooldown(double cooldown) {
		this.cooldown = cooldown;
		if(originalCooldown == -1) {
			this.originalCooldown = cooldown;
		}
	}

	public long getLastUse() {
		return lastUse;
	}

	public void setLastUse(long lastUse) {
		this.lastUse = lastUse;
	}

	public abstract ItemStack getItem();
	
	public void setItem(ItemStack is) {
		this.item = is;
	}
	
	public void equip() {
		if(item==null) {
			return;
		}
		playerEB.getPlayer().getInventory().setItem(0, item); 
		this.cooldown = this.originalCooldown;
	}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}

	public void setPlayerEB(PlayerEB playerEB) {
		this.playerEB = playerEB;
	}
	
	public void tick() {
		
	}
	
	public Object clone() { 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null; 
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isSetThisRound() {
		return setThisRound;
	}

	public void setSetThisRound(boolean setThisRound) {
		this.setThisRound = setThisRound;
	}
}
