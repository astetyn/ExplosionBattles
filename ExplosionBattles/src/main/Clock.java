package main;

public class Clock implements Runnable{

	private StateManager stateManager;
	private int ticks;
	private int maxTicks;
	private int index;

	public Clock(StateManager stateManager, int seconds) {
		this.stateManager = stateManager;
		this.maxTicks = seconds;
		this.ticks = 0;
	}
	
	@Override
	public void run() {
		stateManager.stateTick();
		ticks++;
		if(ticks == maxTicks) {
			stateManager.stateEnd();
		}
	}
	
	public int getTicks() {
		return ticks;
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
