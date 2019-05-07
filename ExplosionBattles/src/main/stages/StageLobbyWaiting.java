package main.stages;

import main.Game;
import main.inventory.LobbyInventory;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.player.PlayerEB;

public class StageLobbyWaiting extends Stage {

	private int finalTicks = -1;
	
	public StageLobbyWaiting() {
		setFinalTicks(finalTicks);
		start();
	}
	
	@Override
	public void start() {
		Game.getInstance().setMapChooser(new MapChooser());
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			new LobbyInventory(playerEB);
			playerEB.setInRunningGame(false);
			playerEB.getPlayer().setHealth(20);
			new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.LOBBY);
			playerEB.getStatusBoard().setup("Waiting for players...");
		}
	}
	
	@Override
	public void end() {

	}

}
