package main;

public class Clock implements Runnable{

	private Game game;
	private int ticks;
	private int maxTicks;
	private int index;
	private STATE nextState;

	public Clock(Game game, int seconds, STATE nextState) {
		this.game = game;
		this.maxTicks = seconds;
		this.ticks = 0;
		this.setNextState(nextState);
	}
	
	@Override
	public void run() {
		game.clockTick();
		ticks++;
		if(ticks == maxTicks) {
			game.clockEnd();
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

	public STATE getNextState() {
		return nextState;
	}

	public void setNextState(STATE nextState) {
		this.nextState = nextState;
	}
	
}
