package main;

import org.bukkit.entity.Player;

public class PlayerEB {

	private Player player;
	private STATE state;
	
	public PlayerEB(Player player) {
		
		this.setPlayer(player);
		
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
	
}
