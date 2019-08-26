package main;

import org.bukkit.Bukkit;

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
		game.getStage().onTick();
		game.getStage().increaseTicks();
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
