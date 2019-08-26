package main.gameobjects;

/** This interface should be used when some object need ticks in seconds.*/
public interface SecondTickable {
	
	/** This method is called every second when object is alive.*/
	public abstract void onSecTick();

	/** This method returns if object still require second-rate, if false, object will be erased
	 * from list of second-rate objects. */
	public abstract boolean isAlive();
	
}
