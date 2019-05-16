package main.player;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerDataSaver {

	private Location loc;
	private ItemStack[] contents;
	private ItemStack[] armorContents;
	private ItemStack[] extraContents;
	private PlayerEB playerEB;
	private GameMode gameMode;
	
	public PlayerDataSaver(PlayerEB playerEB) {
		this.playerEB = playerEB;
		loc = playerEB.getPlayer().getLocation().clone();
		loc.setY(loc.getY()+1);
		PlayerInventory oldInventory = playerEB.getPlayer().getInventory();
		contents = oldInventory.getContents();
		armorContents = oldInventory.getArmorContents();
		extraContents = oldInventory.getExtraContents();
		oldInventory.clear();
		gameMode = playerEB.getPlayer().getGameMode();
	}
	
	public void restoreAll() {
		restoreGameMode();
		restoreLocation();
		restorePlayerInventory();
	}
	
	public void restoreLocation() {
		playerEB.getPlayer().teleport(loc);
	}
	
	public void restorePlayerInventory() {
		playerEB.getPlayer().getInventory().setContents(contents);
		playerEB.getPlayer().getInventory().setArmorContents(armorContents);
		playerEB.getPlayer().getInventory().setExtraContents(extraContents);
		playerEB.getPlayer().updateInventory();
	}
	
	public void restoreGameMode() {
		playerEB.getPlayer().setGameMode(gameMode);
	}

}
