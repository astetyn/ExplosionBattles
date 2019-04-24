package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import main.configuration.WorldConfiguration;
import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.maps.world.WorldsEB;
import main.misc.StatusBoard;
import main.misc.airdrop.AirDrop;
import main.misc.inventory.InventoryManager;
import main.misc.locations.LocationSaver;
import net.md_5.bungee.api.ChatColor;

public class Game {

	static Game game = new Game();
	private String map;
	private StateManager stateManager = new StateManager();
	private MapChooser mapChooser = new MapChooser();
	private AirDrop airDrop;
	private WorldsEB worldsEB;
	private int playersInGame = 0;
	
	private List<PlayerEB> players = new ArrayList<PlayerEB>();
	
	public void playerJoin(Player p) {
		PlayerEB playerEB = new PlayerEB(p);
		players.add(playerEB);
		playerPreJoin(playerEB);
		playerPostJoin(playerEB);
	}
	
	private void playerPreJoin(PlayerEB playerEB) {
		LocationSaver.getInstance().saveLocation(playerEB);
		new LocationTeleport(playerEB,stateManager.getState(),map);
		InventoryManager.getInstance().saveAndClearInventory(playerEB);
		InventoryManager.getInstance().giveItemsByState(playerEB);
	}
	
	private void playerPostJoin(PlayerEB playerEB) {
		StatusBoard statusBoard = new StatusBoard(playerEB);
		statusBoard.setWaiting();
		if(players.size()==2) {
			stateManager.changeState(STATE.LOBBY_LAUNCHING);
		}
		playerEB.getPlayer().setGameMode(GameMode.SURVIVAL);
		playerEB.getPlayer().setFoodLevel(20);
		playerEB.getPlayer().setHealth(20);
		playerEB.getPlayer().sendMessage(ChatColor.RED+"Tato minihra je vo vyvoji. Ak natrafite na bug alebo sa chcete podelit o navrh na zlepsenie, napiste nam cez /eb report <sprava>");
	}
	
	public void playerForceLeave(PlayerEB playerEB) {
		playersInGame--;
		players.remove(playerEB);
		
		if(players.size()<=1) {
			stateManager.changeState(STATE.LOBBY_WAITING);
		}
		
		playerPostLeave(playerEB);
	}
	
	private void playerPostLeave(PlayerEB playerEB) {
		StatusBoard statusBoard = new StatusBoard(playerEB);
		statusBoard.setClean();
		LocationSaver.getInstance().loadAndTeleport(playerEB);
		InventoryManager.getInstance().loadInventory(playerEB);
	}
	
	public boolean isPlayerInGame(Player p) {
		for(PlayerEB playerEB : players) {
			if(playerEB.getPlayer().equals(p)) {
				return true;
			}
		}
		return false;
	}
	
	public PlayerEB getPlayer(Player p) {
		for(PlayerEB playerEB : players) {
			if(playerEB.getPlayer().equals(p)) {
				return playerEB;
			}
		}
		return null;
	}
	
	public void playerDied(PlayerEB playerEB,PlayerEB playerEB2) {
		for(PlayerEB pEB : players) {
			pEB.getPlayer().sendMessage(playerEB.getPlayer().getName() + " vybuchol.");
		}
		playerEB2.getPlayer().sendMessage("Zabil si hraca "+playerEB.getPlayer().getName());
		playerEB.getPlayer().sendMessage("Bol si zabity hracom "+playerEB2.getPlayer().getName());
		playerEB.setSpectator(true);
		WorldConfiguration wc = new WorldConfiguration(map);
		Location loc = (Location) wc.getConfig().get("spec");
		playerEB.getPlayer().teleport(loc);
		playersInGame--;
		if(playersInGame<=1) {
			stateManager.changeState(STATE.ENDING);
			String winnerName = null;
			for(PlayerEB pEB : players) {
				if(!pEB.isSpectator()) {
					winnerName = pEB.getPlayer().getName();
				}
			}
			for(PlayerEB pEB : players) {
				pEB.getPlayer().sendTitle(winnerName,"vyhral hru!", 20, 40, 20);
				pEB.getPlayer().sendMessage(winnerName + " vyhral hru a nenechal sa odpalit!");
			}
		}
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
	
	public void setMap(String map) {
		this.map = map;
	}
	
	public String getMap() {
		return this.map;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}

	public WorldsEB getWorldsEB() {
		return worldsEB;
	}

	public void setWorldsEB(WorldsEB worldEB) {
		this.worldsEB = worldEB;
	}

	public int getPlayersInGame() {
		return playersInGame;
	}

	public void setPlayersInGame(int playersInGame) {
		this.playersInGame = playersInGame;
	}

	public MapChooser getMapChooser() {
		return mapChooser;
	}
	
	public void setMapChooser(MapChooser mapChooser) {
		this.mapChooser = mapChooser;
	}

	public AirDrop getAirDrop() {
		return airDrop;
	}

	public void setAirDrop(AirDrop airDrop) {
		this.airDrop = airDrop;
	}
}
