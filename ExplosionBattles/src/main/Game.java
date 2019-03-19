package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import main.configuration.LocTeleport;
import main.maps.MapManager;

public class Game {

	//static Game game = new Game();
	private String map;
	
	private MapManager mapManager = new MapManager();
	private Clock clock;
	
	private STATE state = STATE.LOBBY_WAITING;

	private List<PlayerEB> players = new ArrayList<PlayerEB>();
	
	private HashMap<PlayerEB,PlayerInventory> inventories = new HashMap<PlayerEB,PlayerInventory>();
	
	public void playerJoin(Player p) {
		PlayerEB playerEB = new PlayerEB(p);
		players.add(playerEB);
		playerPreJoin(playerEB);
		playerPostJoin(playerEB);
	}
	
	private void playerPreJoin(PlayerEB playerEB) {
		inventories.put(playerEB,playerEB.getPlayer().getInventory());
		playerEB.getPlayer().getInventory().clear();
		playerEB.getPlayer().updateInventory();
		new LocTeleport(playerEB,state);
	}
	
	private void playerPostJoin(PlayerEB playerEB) {
		if(players.size()==2) {
			changeState(STATE.LOBBY_LAUNCHING);
		}
	}
	
	public void playerForceLeave(PlayerEB playerEB) {
		players.remove(playerEB);
		PlayerInventory inventory = inventories.get(playerEB);
		playerEB.getPlayer().getInventory().setContents(inventory.getContents());
		playerEB.getPlayer().updateInventory();
		playerPostLeave(playerEB);
	}
	
	public void playerPostLeave(PlayerEB playerEB) {
		if(players.size()==1) {
			changeState(STATE.LOBBY_WAITING);
		}
	}
	
	public void startClock(int seconds) {
		clock = new Clock(this,seconds);
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
			break;
		}
		case GAME_RUNNING:{
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
