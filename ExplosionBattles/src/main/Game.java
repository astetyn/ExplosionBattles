package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.configuration.LocTeleport;
import main.maps.MapManager;
import main.misc.InventorySaver;

public class Game {

	static Game game = new Game();
	private String map;
	
	private MapManager mapManager = new MapManager();
	private Clock clock;
	
	private STATE state = STATE.LOBBY_WAITING;

	private List<PlayerEB> players = new ArrayList<PlayerEB>();
	
	public void playerJoin(Player p) {
		PlayerEB playerEB = new PlayerEB(p);
		players.add(playerEB);
		playerPreJoin(playerEB);
		playerPostJoin(playerEB);
	}
	
	private void playerPreJoin(PlayerEB playerEB) {
		InventorySaver.getInstance().saveAndClearInventory(playerEB);
		new LocTeleport(playerEB,state);
	}
	
	private void playerPostJoin(PlayerEB playerEB) {
		if(players.size()==1) {
			changeState(STATE.LOBBY_LAUNCHING);
		}
	}
	
	public void playerForceLeave(PlayerEB playerEB) {
		players.remove(playerEB);
		InventorySaver.getInstance().loadInventory(playerEB);
		playerPostLeave(playerEB);
	}
	
	public void playerPostLeave(PlayerEB playerEB) {
		if(players.size()==1) {
			changeState(STATE.LOBBY_WAITING);
		}
	}
	
	public void startClock(int seconds,STATE nextState) {
		clock = new Clock(this,seconds, nextState);
		int index = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), clock, 20, 20);
		clock.setIndex(index);
		Bukkit.broadcastMessage("start");
	}
	
	public void clockTick() {
		Bukkit.broadcastMessage("tick"+state);
	}
	
	public void clockEnd() {
		Bukkit.broadcastMessage("end");
		clockStop();
		changeState(clock.getNextState());
	}
	
	public void clockStop() {
		int index = clock.getIndex();
		Bukkit.getScheduler().cancelTask(index);
	}
	
	public void changeState(STATE newState) {
		
		if(newState==state) {
			return;
		}
		
		switch(newState) {
		case LOBBY_WAITING:{
			break;
		}
		case LOBBY_LAUNCHING:{
			startClock(30,STATE.GAME_RUNNING);
			break;
		}
		case GAME_RUNNING:{
			startClock(60,STATE.ENDING);
			new LocTeleport(map);
			break;
		}
		case ENDING:{
			break;
		}
		}
		
		state = newState;
		
	}
	
	public static Game getInstance() {
		return game;
	}

	public List<PlayerEB> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerEB> players) {
		this.players = players;
	}

	public STATE getState() {
		return state;
	}
	
	public void setState(STATE state) {
		this.state = state;
	}
}
