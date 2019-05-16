package main.weapons;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;

import main.player.PlayerEB;
import main.weapons.data.WeaponData;

public abstract class Weapon {

	private double cooldown = -1;
	private long lastUse = 0;
	private PlayerEB playerEB;
	
	public abstract WeaponData getWeaponData();
	
	public boolean onInteract(PlayerInteractEvent event) {
		return true;
	}

	public double getCooldown() {
		if(cooldown == -1) {
			cooldown = getWeaponData().getDefaultCooldown();
		}
		return cooldown;
	}
	
	public void setCooldown(double cooldown) {
		this.cooldown = cooldown;
	}
	
	public void equip() {
		if(getWeaponData().getItem()==null) {
			return;
		}
		playerEB.getPlayer().getInventory().setItem(0, getWeaponData().getItem()); 
		cooldown = getWeaponData().getDefaultCooldown();
		getPlayerEB().getPlayer().sendTitle(ChatColor.BLUE+""+ChatColor.BOLD+"Ovládanie zbrane", ChatColor.AQUA+""+ChatColor.ITALIC+getWeaponData().getManualMsg(),20,40,40);
	}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}

	public void setPlayerEB(PlayerEB playerEB) {
		this.playerEB = playerEB;
	}
	
	public void tick() {};	
	
	public long getLastUse() {
		return lastUse;
	}

	public void setLastUse(long lastUse) {
		this.lastUse = lastUse;
	}
}
