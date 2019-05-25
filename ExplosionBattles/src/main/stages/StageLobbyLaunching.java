package main.stages;

import main.Game;
import main.configuration.Configuration;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.LobbyInventoryLayout;

public class StageLobbyLaunching extends Stage{

	private int finalTicks;
	private Game game;
	
	public StageLobbyLaunching(Game game) {
		this.game = game;
		Configuration c = game.getConfiguration();
		int seconds = c.getConfig().getInt("game.seconds-lobby");
		finalTicks = seconds*20;
		setFinalTicks(finalTicks);
		start();
	}
	
	@Override
	public void start() {
		game.getMapsManager().reloadMaps();
		game.getMapsManager().notifyAllPlayers();
		
		for(PlayerEB playerEB : game.getPlayers()) {
			playerEB.setInventoryLayout(new LobbyInventoryLayout(playerEB));
			playerEB.setGameStage(GameStage.LOBBY_LAUNCHING);
			playerEB.getPlayer().setHealth(20);
			new LocationTeleport(playerEB,game.getMap(),GameLocation.LOBBY);
			playerEB.getStatusBoard().setup("Launching in:");
		}
	}
	
	@Override
	public void tick() {
		if(getTicks()%20!=0) {
			return;
		}
		for(PlayerEB playerEB : game.getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().tick(remainingTime);
		}
		if(getTicks()==getFinalTicks()) {
			game.setStage(new StageGameRunning(game));
		}
	}
	
	@Override
	public void end() {
		
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		playerEB.getStatusBoard().setup("Launching in:");
		new LocationTeleport(playerEB,game.getMap(),GameLocation.LOBBY);
		playerEB.setInventoryLayout(new LobbyInventoryLayout(playerEB));
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {

	}
}
