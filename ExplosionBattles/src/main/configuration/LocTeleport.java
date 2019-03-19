package main.configuration;

import org.bukkit.Location;

import main.Game;
import main.Main;
import main.PlayerEB;
import main.STATE;

public class LocTeleport {

	private PlayerEB playerEB;
	private STATE state;
	private String map;
	
	public LocTeleport(PlayerEB playerEB,STATE state) {
		
		this.playerEB = playerEB;
		this.state = state;
		teleport();
		
	}
	
	public LocTeleport(String map) {
		
		this.map = map;
		teleportMap();
		
	}
	
	private void teleport() {
		
		switch(state) {
		case LOBBY_WAITING: case LOBBY_LAUNCHING:{
			WorldConfiguration wc = new WorldConfiguration(Main.getPlugin(),"lobby");
			Location loc = (Location) wc.getConfig().get("spawnlobby");
			playerEB.getPlayer().teleport(loc);
			break;
		}
		case GAME_RUNNING: case ENDING:{
			WorldConfiguration wc = new WorldConfiguration(Main.getPlugin(),map);
			Location loc = (Location) wc.getConfig().get("spec");
			playerEB.getPlayer().teleport(loc);
			break;
		}
		}
		
	}
	
	private void teleportMap() {

		WorldConfiguration wc = new WorldConfiguration(Main.getPlugin(),map);

		int counter = 1;
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			Location loc = (Location) wc.getConfig().get("loc"+counter);
			playerEB.getPlayer().teleport(loc);
			counter++;
		}
	}
	
}
