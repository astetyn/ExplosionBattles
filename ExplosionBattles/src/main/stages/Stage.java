package main.stages;

public abstract class Stage {

	private int ticks = 0;
	private int finalTicks = 0;

	public abstract void start();
	
	public abstract void tick();
	
	public abstract void end();

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public int getFinalTicks() {
		return finalTicks;
	}

	public void setFinalTicks(int finalTicks) {
		this.finalTicks = finalTicks;
	}
}
