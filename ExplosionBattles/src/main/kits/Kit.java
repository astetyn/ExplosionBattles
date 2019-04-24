package main.kits;

import org.bukkit.inventory.ItemStack;

import main.PlayerEB;

public class Kit implements Cloneable {

	private String name;
	protected PlayerEB playerEB;
	protected double gunCooldown;
	
	public Kit() {
	}
	
	public void startInit() {
	}
	
	public void onInteract(ItemStack it) {
		
	}
	
	public void tick() {
		
	}
	
	public ItemStack getChooseItem() {
		return null;
	}

	public void setPlayer(PlayerEB playerEB) {
		this.playerEB = playerEB;
	}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Object clone() { 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null; 
	} 
	
	public double getGunCooldown() {
		return this.gunCooldown;
	}
	
	public void setGunCooldown(double newCooldown) {
		this.gunCooldown = newCooldown;
	}
}
