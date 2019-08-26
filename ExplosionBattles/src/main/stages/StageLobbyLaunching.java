package main.stages;

import main.Clock;
import main.Game;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.LobbyInventoryLayout;

public class StageLobbyLaunching extends Stage{

	private Game game;
	
	public StageLobbyLaunching(Game game) {
		super("game.seconds-lobby");
		this.game = game;
		
		if(game.getClock().isStopped()) {
			game.setClock(new Clock(game));
		}
		
		start();
	}
	
	@Override
	public void start() {
		game.getMapsManager().reloadMaps();
		game.getMapsManager().notifyAllPlayers();
		
		for(PlayerEB playerEB : game.getPlayersInGame()) {
			readyPlayer(playerEB);
		}
	}
	
	@Override
	public void onTick() {
		if(getTicks()%20!=0) {
			return;
		}
		for(PlayerEB playerEB : game.getPlayersInGame()) {
			int remainingTime = (getEndTick()-getTicks())/20;
			playerEB.getStatusBoard().setTime(remainingTime);
		}
		if(getTicks()==getEndTick()) {
			end();
			game.setStage(new StageGameRunning(game));
		}
	}
	
	@Override
	public void end() {
		
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		readyPlayer(playerEB);
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {

	}
	
	@Override
	public void readyPlayer(PlayerEB playerEB) {
		playerEB.setGameStage(GameStage.LOBBY_LAUNCHING);
		playerEB.setInventoryLayout(new LobbyInventoryLayout(playerEB));
		new LocationTeleport(playerEB,game.getMap(),GameLocation.LOBBY);
		playerEB.getStatusBoard().setup("Launching in:");
		super.readyPlayer(playerEB);
	}
}
