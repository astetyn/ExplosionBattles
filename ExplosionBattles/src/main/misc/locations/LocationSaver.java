package main.misc.locations;

import java.util.HashMap;

import org.bukkit.Location;

import main.PlayerEB;

public class LocationSaver {

	private static LocationSaver locationSaver = new LocationSaver();
	private HashMap<PlayerEB,Location> savedLocations = new HashMap<PlayerEB,Location>();

	public void saveLocation(PlayerEB playerEB) {
		Location loc = playerEB.getPlayer().getLocation();
		savedLocations.put(playerEB, loc);
	}
	
	public void loadAndTeleport(PlayerEB playerEB) {
		Location loc = savedLocations.get(playerEB);
		playerEB.getPlayer().teleport(loc);
		savedLocations.remove(playerEB);
	}
	
	public static LocationSaver getInstance() {
		return locationSaver;
	}

}
