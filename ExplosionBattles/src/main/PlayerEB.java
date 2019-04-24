package main;

import org.bukkit.entity.Player;

import main.kits.BasicKit;
import main.kits.Kit;

public class PlayerEB {

	private Player player;
	private STATE state;
	private boolean spectator;
	private Kit kit = new BasicKit(this);
	
	public PlayerEB(Player player) {
		this.setPlayer(player);
		this.setSpectator(false);
	}

	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isSpectator() {
		return spectator;
	}

	public void setSpectator(boolean spectator) {
		this.spectator = spectator;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}
	
	public Kit getKit() {
		return kit;
	}
	
}
