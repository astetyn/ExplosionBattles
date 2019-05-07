package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import main.configuration.Configuration;
import main.gameobjects.airdrop.AirDrop;
import main.inventory.InventoryManager;
import main.inventory.LobbyInventory;
import main.inventory.SpectatorInventory;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.maps.world.WorldsEB;
import main.misc.locations.LocationSaver;
import main.player.PlayerEB;
import main.stages.Stage;
import main.stages.StageEnding;
import main.stages.StageLobbyLaunching;
import main.stages.StageLobbyWaiting;
import net.md_5.bungee.api.ChatColor;

public class Game {

	private static Game game = new Game();
	private String map;
	private Stage stage;
	private Clock clock = new Clock(this);
	private MapChooser mapChooser = new MapChooser();
	private AirDrop airDrop;
	private WorldsEB worldsEB;
	private Configuration configuration = new Configuration(Main.getPlugin());
	private int playersInGame = 0;
	private boolean gameRunning = false;
	private List<PlayerEB> players = new ArrayList<PlayerEB>();
	private PlayerEB lastShootPlayerEB;
	
	public void playerJoin(Player p) {
		if(stage==null) {
			stage = new StageLobbyWaiting();
		}
		PlayerEB playerEB = new PlayerEB(p);
		players.add(playerEB);
		playerPreJoin(playerEB);
		playerPostJoin(playerEB);
	}
	
	private void playerPreJoin(PlayerEB playerEB) {
		if(clock.isStopped()) {
			clock = new Clock(this);
		}
		if(configuration.getConfig().getInt("game.max-players")==playersInGame) {
			playerEB.getPlayer().sendMessage("Je tu plno. Pockaj kym sa niekto odpoji.");
			return;
		}
		LocationSaver.getInstance().saveLocation(playerEB);
		if(isGameRunning()) {
			playerEB.getStatusBoard().setup("Wait for end game.");
			new LocationTeleport(playerEB,getMap(),GameLocation.SPECTATOR);
		}else {
			playerEB.getStatusBoard().setup("Waiting for players...");
			new LocationTeleport(playerEB,getMap(),GameLocation.LOBBY);
		}
		InventoryManager.getInstance().saveAndClearInventory(playerEB);
		if(stage instanceof StageLobbyLaunching||stage instanceof StageLobbyWaiting) {
			new LobbyInventory(playerEB);
		}else {
			new SpectatorInventory(playerEB);
		}
	}
	
	private void playerPostJoin(PlayerEB playerEB) {
		if(players.size()==1) {
			stage.end();
			stage = new StageLobbyLaunching();
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
			stage.end();
			stage = null;
			stage = new StageLobbyWaiting();
			clock.stop();
		}
		
		playerPostLeave(playerEB);
	}
	
	private void playerPostLeave(PlayerEB playerEB) {
		playerEB.getStatusBoard().setClean();
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
	
	public void playerDied(PlayerEB playerEB) {
		playerEB.setInRunningGame(false);
		for(PlayerEB pEB : players) {
			pEB.getPlayer().sendMessage(playerEB.getPlayer().getName() + " vybuchol.");
		}
		playerEB.getPlayer().sendMessage("Vybuchol si.");
		new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.SPECTATOR);
		playersInGame--;
		if(playersInGame<=1) {
			String winnerName = null;
			for(PlayerEB pEB : players) {
				if(pEB.isInRunningGame()) {
					winnerName = pEB.getPlayer().getName();
				}
			}
			for(PlayerEB pEB : players) {
				pEB.getPlayer().sendTitle(winnerName,"vyhral hru!", 20, 40, 20);
				pEB.getPlayer().sendMessage(winnerName + " vyhral hru a nenechal sa odpalit!");
			}
			stage.end();
			stage = new StageEnding();
		}
	}
	
	public void playerDied(PlayerEB playerEB,PlayerEB playerEB2) {
		playerEB.setInRunningGame(false);
		for(PlayerEB pEB : players) {
			pEB.getPlayer().sendMessage(playerEB.getPlayer().getName() + " vybuchol.");
		}
		playerEB2.getPlayer().sendMessage("Zabil si hraca "+playerEB.getPlayer().getName());
		playerEB.getPlayer().sendMessage("Bol si zabity hracom "+playerEB2.getPlayer().getName());
		new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.SPECTATOR);
		playersInGame--;
		if(playersInGame<=1) {
			String winnerName = null;
			for(PlayerEB pEB : players) {
				if(pEB.isInRunningGame()) {
					winnerName = pEB.getPlayer().getName();
				}
			}
			for(PlayerEB pEB : players) {
				pEB.getPlayer().sendTitle(winnerName,"vyhral hru!", 20, 40, 20);
				pEB.getPlayer().sendMessage(winnerName + " vyhral hru a nenechal sa odpalit!");
			}
			stage.end();
			stage = new StageEnding();
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

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public PlayerEB getLastShootPlayerEB() {
		return lastShootPlayerEB;
	}

	public void setLastShootPlayerEB(PlayerEB lastShootPlayerEB) {
		this.lastShootPlayerEB = lastShootPlayerEB;
	}
}
