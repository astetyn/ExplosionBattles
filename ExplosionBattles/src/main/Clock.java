package main;

import org.bukkit.Bukkit;

import main.stages.StageLobbyWaiting;

public class Clock implements Runnable{

	private Game game;
	private int index;
	private boolean stopped = false;

	public Clock(Game game) {
		this.game = game;
		this.index = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this, 1, 1);
	}
	
	@Override
	public void run() {
		if(game.getStage()==null) {
			return;
		}
		if(game.getStage() instanceof StageLobbyWaiting) {
			return;
		}
		game.getStage().tick();
		int stageTicks = game.getStage().getTicks();
		game.getStage().setTicks(stageTicks+1);
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(index);
		stopped = true;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isStopped() {
		return stopped;
	}
	
}
