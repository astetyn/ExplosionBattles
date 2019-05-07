package main.maps;

import org.bukkit.Location;

import main.configuration.MapConfiguration;
import main.player.PlayerEB;

public class LocationTeleport {

	private PlayerEB playerEB;
	private String map;
	private GameLocation gameLoc;
	
	public LocationTeleport(PlayerEB playerEB, String map, GameLocation gameLoc) {
		
		this.playerEB = playerEB;
		this.map = map;
		this.gameLoc = gameLoc;
		teleport();
		
	}
	
	private void teleport() {
		
		if(gameLoc == GameLocation.LOBBY) {
			LobbySystemChecker lsc = new LobbySystemChecker();
			if(!lsc.lobbyExists()) {
				playerEB.getPlayer().sendMessage("Lokacia lobby je poskodena alebo neexistuje. Kontaktuj AT.");
				return;
			}
			MapConfiguration wc = new MapConfiguration("lobby");
			Location loc = (Location) wc.getConfig().get("spawnlobby");
			playerEB.getPlayer().teleport(loc);
		}
		if(gameLoc == GameLocation.SPECTATOR) {
			MapConfiguration wc = new MapConfiguration(map);
			Location loc = (Location) wc.getConfig().get("spec");
			playerEB.getPlayer().teleport(loc);
		}
	}
	
}
