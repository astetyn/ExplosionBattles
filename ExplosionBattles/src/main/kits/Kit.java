package main.kits;

import org.bukkit.inventory.ItemStack;

import main.kits.data.KitData;
import main.player.PlayerEB;

public abstract class Kit {

	private PlayerEB playerEB;
	
	public void startInit() {}
	
	public void onInteract(ItemStack it) {}
	
	public void tick() {}
	
	public abstract KitData getKitData();

	public void setPlayer(PlayerEB playerEB) {
		this.playerEB = playerEB;
	}

	public PlayerEB getPlayerEB() {
		return playerEB;
	}
	
}
