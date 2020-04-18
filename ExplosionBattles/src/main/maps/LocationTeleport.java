package main.maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import main.MsgCenter;
import main.configuration.MapConfiguration;
import main.player.PlayerEB;
import main.utils.LocationS;
import net.md_5.bungee.api.ChatColor;

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
				playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Lokácia lobby je poškodená alebo neexistuje. Kontaktuj vedenie.");
				return;
			}
			MapConfiguration wc = new MapConfiguration("lobby");
			LocationS loc = (LocationS) wc.getConfig().get("spawnlobby");
			playerEB.getPlayer().teleport(new Location(Bukkit.getWorld(loc.getWorld()),loc.getX(),loc.getY(),loc.getZ()));
		}
		if(gameLoc == GameLocation.SPECTATOR) {
			MapConfiguration wc = new MapConfiguration(map);
			LocationS loc = (LocationS) wc.getConfig().get("spec");
			playerEB.getPlayer().teleport(new Location(Bukkit.getWorld(loc.getWorld()),loc.getX(),loc.getY(),loc.getZ()));
		}
	}
	
}
