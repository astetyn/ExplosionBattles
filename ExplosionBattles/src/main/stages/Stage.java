package main.stages;

public class Stage {

	private int ticks = 0;
	private int finalTicks = 0;
	
	public Stage() {
	}

	public void start() {
		
	}
	
	public void tick() {
		
	}
	
	public void end() {
		
	}

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
