package main.stages;

import main.Game;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.SpectatorInventoryLayout;

public class StageEnding extends Stage {

	private Game game;
	
	public StageEnding(Game game) {
		super("game.seconds-ending");
		this.game = game;
		start();
	}
	
	public void start() {
		for(PlayerEB playerEB : game.getPlayersInGame()) {
			readyPlayer(playerEB);
		}
	}
	
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
			game.setStage(new StageLobbyLaunching(game));
		}
	}

	@Override
	public void end() {
		
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		new LocationTeleport(playerEB,game.getMap(),GameLocation.SPECTATOR);
		readyPlayer(playerEB);
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {
		
	}
	
	@Override
	public void readyPlayer(PlayerEB playerEB) {
		playerEB.setGameStage(GameStage.GAME_ENDING);
		playerEB.setInventoryLayout(new SpectatorInventoryLayout(playerEB));
		playerEB.getStatusBoard().setup("Ending phase...");
		super.readyPlayer(playerEB);
	}
}
