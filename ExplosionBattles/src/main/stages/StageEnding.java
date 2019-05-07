package main.stages;

import main.Game;
import main.configuration.Configuration;
import main.player.PlayerEB;

public class StageEnding extends Stage {

	private int finalTicks;
	
	public StageEnding() {
		Configuration c = Game.getInstance().getConfiguration();
		int seconds = c.getConfig().getInt("game.seconds-ending");
		finalTicks = seconds*20;
		setFinalTicks(finalTicks);
		start();
	}
	
	public void start() {
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.setInRunningGame(false);
			playerEB.getWeapon().setSetThisRound(false);
			playerEB.getPlayer().setHealth(20);
			playerEB.getStatusBoard().setup("Ending phase...");
		}
	}
	
	public void tick() {
		if(getTicks()%20!=0) {
			return;
		}
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().setData(remainingTime);
		}
		if(getTicks()==getFinalTicks()) {
			Game.getInstance().setStage(null);
			Game.getInstance().setStage(new StageLobbyLaunching());
		}
	}

}
