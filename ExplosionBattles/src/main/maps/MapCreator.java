package main.maps;

import org.bukkit.Location;

import main.Game;
import main.PlayerEB;
import main.configuration.WorldConfiguration;
import main.maps.world.WorldsEB;

public class MapCreator {

	private String map;
	private WorldsEB worldsEB;
	
	public MapCreator(String map) {
		
		this.map = map;
		WorldsEB worldEB = new WorldsEB();
		worldEB.restartGameWorld();
		this.worldsEB = worldEB;
		teleportMap();
		setConditions();
		
		Game.getInstance().setWorldsEB(worldEB);
	}
	
	private void setConditions() {
		
		WorldConfiguration wc = new WorldConfiguration(map);
		boolean rain = wc.getConfig().getBoolean("rain");
		boolean night = wc.getConfig().getBoolean("night");
		
		if(rain) {
			worldsEB.getGameWorld().setThundering(true);
			worldsEB.getGameWorld().setStorm(true);
		}else {
			worldsEB.getGameWorld().setThundering(false);
			worldsEB.getGameWorld().setStorm(false);
		}
		
		worldsEB.setRain(rain);
		worldsEB.setNight(night);
		
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