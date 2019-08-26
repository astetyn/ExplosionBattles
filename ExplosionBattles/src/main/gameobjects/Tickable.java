package main.gameobjects;

/** This interface should be used when some object in game require tick-rate for some time.*/
public interface Tickable {
	
	/** This method is called every tick when object is alive.*/
	public abstract void onTick();

	/** This method returns if object still require tick-rate, if false, object will be erased
	 * from list of tick-rate objects. */
	public abstract boolean isAlive();
	
}
