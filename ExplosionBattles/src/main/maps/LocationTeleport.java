package main.maps;

import org.bukkit.Location;

import main.PlayerEB;
import main.STATE;
import main.configuration.WorldConfiguration;

public class LocationTeleport {

	private PlayerEB playerEB;
	private STATE state;
	private String map;
	
	public LocationTeleport(PlayerEB playerEB,STATE state, String map) {
		
		this.playerEB = playerEB;
		this.state = state;
		this.map = map;
		teleport();
		
	}
	
	private void teleport() {
		
		switch(state) {
		case LOBBY_WAITING: case LOBBY_LAUNCHING:{
			WorldConfiguration wc = new WorldConfiguration("lobby");
			Location loc = (Location) wc.getConfig().get("spawnlobby");
			playerEB.getPlayer().teleport(loc);
			break;
		}
		case GAME_RUNNING: case ENDING:{
			WorldConfiguration wc = new WorldConfiguration(map);
			Location loc = (Location) wc.getConfig().get("spec");
			playerEB.getPlayer().teleport(loc);
			break;
		}
		}
		
}
	
}
