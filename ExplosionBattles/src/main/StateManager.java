package main;

import org.bukkit.Bukkit;

import main.maps.LocationTeleport;
import main.maps.MapSystemChecker;
import main.maps.MapTeleport;
import main.misc.inventory.InventoryManager;

public class StateManager {

	Clock clock;
	STATE state = STATE.LOBBY_WAITING;
	
	public StateManager() {

	}
	
	public void stateStart() {
		Bukkit.broadcastMessage("start "+state);
		switch(state) {
		case LOBBY_WAITING:{
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				new LocationTeleport(playerEB,state,Game.getInstance().getMap());
			}
			break;
		}
		case LOBBY_LAUNCHING:{
			InventoryManager.getInstance().giveItemsByStateAll();
			startClock(30);
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				new LocationTeleport(playerEB,state,Game.getInstance().getMap());
			}
			break;
		}
		case GAME_RUNNING:{
			InventoryManager.getInstance().clearInventoryAll();
			MapSystemChecker msc = new MapSystemChecker(Game.getInstance().getMap());
			if(!msc.isMapCompleted()) {
				return;
			}
			
			new MapTeleport(Game.getInstance().getMap());
			startClock(60);
			break;
		}
		case ENDING:{
			startClock(5);
			break;
		}
		}
	}
	
	public void stateTick() {
		Bukkit.broadcastMessage(clock.getMaxTicks()-clock.getTicks()+"");
		switch(state) {
		case LOBBY_WAITING:{
			
			break;
		}
		case LOBBY_LAUNCHING:{

			break;
		}
		case GAME_RUNNING:{

			break;
		}
		case ENDING:{
			
			break;
		}
		}
	}
	
	public void stateEnd() {
		Bukkit.broadcastMessage("end "+state);
		switch(state) {
		case LOBBY_WAITING:{
			changeState(STATE.LOBBY_LAUNCHING);
			break;
		}
		case LOBBY_LAUNCHING:{
			changeState(STATE.GAME_RUNNING);
			break;
		}
		case GAME_RUNNING:{
			changeState(STATE.ENDING);
			break;
		}
		case ENDING:{
			changeState(STATE.LOBBY_LAUNCHING);
			break;
		}
		}
	}
	
	public void changeState(STATE newState) {
		clockStop();
		if(newState==state) {
			return;
		}
		state = newState;
		stateStart();
	}
	
	public void clockStop() {
		if(clock==null) {
			return;
		}
		int index = clock.getIndex();
		if(Bukkit.getScheduler().isQueued(index)) {
			Bukkit.getScheduler().cancelTask(index);
		}
		clock = null;
	}
	
	public void startClock(int seconds) {
		clockStop();
		clock = new Clock(this,seconds);
		int index = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), clock, 20, 20);
		clock.setIndex(index);
	}
	
	public STATE getState() {
		return state;
	}
}
