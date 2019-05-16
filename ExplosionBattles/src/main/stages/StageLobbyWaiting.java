package main.stages;

import main.Game;
import main.layouts.LobbyInventory;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.player.GameStage;
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
			playerEB.setGameStage(GameStage.LOBBY_WAITING);
			playerEB.getPlayer().setHealth(20);
			new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.LOBBY);
			playerEB.getStatusBoard().setup("Waiting for players...");
			playerEB.getStatusBoard().tick(-1);
		}
	}
	
	@Override
	public void tick() {
	}
	
	@Override
	public void end() {
	}

	

}
