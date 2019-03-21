package main.maps;

import org.bukkit.Location;

import main.Game;
import main.PlayerEB;
import main.configuration.WorldConfiguration;

public class MapTeleport {

	private String map;
	
	public MapTeleport(String map) {
		
		this.map = map;
		WorldEB worldEB = new WorldEB();
		worldEB.unloadWorld();
		worldEB.loadWorld();
		teleportMap();
		
	}
	
	private void teleportMap() {

		WorldConfiguration wc = new WorldConfiguration(map);

		int counter = 1;
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			Location loc = (Location) wc.getConfig().get("loc"+counter);
			playerEB.getPlayer().teleport(loc);
			counter++;
		}
	}
	
}
