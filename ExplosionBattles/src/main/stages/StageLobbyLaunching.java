package main.stages;

import main.Game;
import main.configuration.Configuration;
import main.layouts.LobbyInventory;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.player.GameStage;
import main.player.PlayerEB;

public class StageLobbyLaunching extends Stage{

	private int finalTicks;
	public StageLobbyLaunching() {
		Configuration c = Game.getInstance().getConfiguration();
		int seconds = c.getConfig().getInt("game.seconds-lobby");
		finalTicks = seconds*20;
		setFinalTicks(finalTicks);
		start();
	}
	
	@Override
	public void start() {
		Game.getInstance().setMapChooser(new MapChooser());
		Game.getInstance().getMapChooser().notifyAllPlayers();
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			new LobbyInventory(playerEB);
			playerEB.setGameStage(GameStage.LOBBY_LAUNCHING);
			playerEB.getPlayer().setHealth(20);
			new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.LOBBY);
			playerEB.getStatusBoard().setup("Launching in:");
		}
	}
	
	@Override
	public void tick() {
		if(getTicks()%20!=0) {
			return;
		}
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().tick(remainingTime);
		}
		if(getTicks()==getFinalTicks()) {
			Game.getInstance().setStage(new StageGameRunning());
		}
	}
	
	@Override
	public void end() {
		
	}

}
