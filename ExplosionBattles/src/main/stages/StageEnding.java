package main.stages;

import main.Game;
import main.configuration.Configuration;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.SpectatorInventoryLayout;

public class StageEnding extends Stage {

	private int finalTicks;
	private Game game;
	
	public StageEnding(Game game) {
		this.game = game;
		Configuration c = game.getConfiguration();
		int seconds = c.getConfig().getInt("game.seconds-ending");
		finalTicks = seconds*20;
		setFinalTicks(finalTicks);
		start();
	}
	
	public void start() {
		for(PlayerEB playerEB : game.getPlayers()) {
			playerEB.setGameStage(GameStage.GAME_ENDING);
			playerEB.getPlayer().setHealth(20);
			playerEB.getStatusBoard().setup("Ending phase...");
		}
	}
	
	public void tick() {
		if(getTicks()%20!=0) {
			return;
		}
		for(PlayerEB playerEB : game.getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().tick(remainingTime);
		}
		if(getTicks()==getFinalTicks()) {
			game.setStage(new StageLobbyLaunching(game));
		}
	}

	@Override
	public void end() {
		
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		playerEB.getStatusBoard().setup("Wait for end game.");
		new LocationTeleport(playerEB,game.getMap(),GameLocation.SPECTATOR);
		playerEB.setInventoryLayout(new SpectatorInventoryLayout(playerEB));
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {
		
	}

}
