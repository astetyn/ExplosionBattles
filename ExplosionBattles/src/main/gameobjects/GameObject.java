package main.gameobjects;

import org.bukkit.block.Block;

import main.player.PlayerEB;

public abstract class GameObject {
	
	public abstract void tick();

	public abstract boolean isActive();
	
	public void onInteractBlock(PlayerEB playerEB, Block b) {};
}
