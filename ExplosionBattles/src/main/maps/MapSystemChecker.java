package main.maps;

import main.Game;
import main.Main;
import main.PlayerEB;
import main.configuration.Configuration;
import main.configuration.WorldConfiguration;

public class MapSystemChecker {

	private String map;
	
	public MapSystemChecker(String map) {
		this.map = map;
	}
	
	public boolean isMapCompleted() {
		boolean b = check();
		if(b) {
			return true;
		}else {
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				playerEB.getPlayer().sendMessage("Mapa je poskodena alebo neexistuje, nacitavanie zlyhalo, kontaktuj vedenie.");
			}
			return false;
		}
	}
	
	public boolean check() {
		
		WorldConfiguration wc = new WorldConfiguration(map);
		
		if(!wc.configExists()) {
			return false;
		}
		Configuration c = new Configuration(Main.getPlugin());
		int maxSpawns = c.getConfig().getInt("game.max-players");

		if(wc.getSpawns()!=maxSpawns) {
			return false;
		}
		if(!wc.spectatorExists()) {
			return false;
		}
		return true;
		
	}
	
}
