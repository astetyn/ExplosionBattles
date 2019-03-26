package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.maps.LocationTeleport;
import main.misc.inventory.InventoryManager;
import main.misc.locations.LocationSaver;

public class Game {

	static Game game = new Game();
	private String map = "mapka";
	private StateManager stateManager = new StateManager();

	private List<PlayerEB> players = new ArrayList<PlayerEB>();
	
	public void playerJoin(Player p) {
		PlayerEB playerEB = new PlayerEB(p);
		players.add(playerEB);
		playerPreJoin(playerEB);
		playerPostJoin(playerEB);
	}
	
	private void playerPreJoin(PlayerEB playerEB) {
		InventoryManager.getInstance().saveAndClearInventory(playerEB);
		InventoryManager.getInstance().giveItemsByState(playerEB);
		LocationSaver.getInstance().saveLocation(playerEB);
		new LocationTeleport(playerEB,stateManager.getState(),map);
	}
	
	private void playerPostJoin(PlayerEB playerEB) {
		if(players.size()==1) {
			stateManager.changeState(STATE.LOBBY_LAUNCHING);
		}
	}
	
	public void playerForceLeave(PlayerEB playerEB) {
		players.remove(playerEB);
		InventoryManager.getInstance().loadInventory(playerEB);
		playerPostLeave(playerEB);
	}
	
	public void playerPostLeave(PlayerEB playerEB) {
		if(players.size()<=1) {
			stateManager.changeState(STATE.LOBBY_WAITING);
		}
		LocationSaver.getInstance().loadAndTeleport(playerEB);
	}
	
	public boolean isPlayerInGame(Player p) {
		for(PlayerEB playerEB : players) {
			if(playerEB.getPlayer().equals(p)) {
				return true;
			}
		}
		return false;
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
	
	public String getMap() {
		return this.map;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}
}
