package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import main.configuration.Configuration;
import main.gameobjects.airdrop.AirDrop;
import main.layouts.LobbyInventory;
import main.layouts.SpectatorInventory;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.maps.world.WorldsEB;
import main.player.GameStage;
import main.player.PlayerEB;
import main.stages.Stage;
import main.stages.StageEnding;
import main.stages.StageLobbyLaunching;
import main.stages.StageLobbyWaiting;

public class Game {

	private static Game game = new Game();
	private String map;
	private Stage stage;
	private Clock clock = new Clock(this);
	private MapChooser mapChooser;
	private AirDrop airDrop;
	private WorldsEB worldsEB;
	private Configuration configuration;
	private int playersInGame = 0;
	private boolean gameRunning = false;
	private List<PlayerEB> players = new ArrayList<PlayerEB>();
	private PlayerEB lastShootPlayerEB;
	private int playersOnStart = 0;
	
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
		if(configuration.getConfig().getInt("game.max-players")==players.size()) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+"Je tu plno. Počkaj kým sa niekto odpojí..");
			return;
		}
		String warning = "";
		warning += ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.DARK_RED+ChatColor.BOLD+"WARNING!"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]\n";
		warning +=ChatColor.RED+"Táto minihra je vo vývoji. Ak natrafíš na bug alebo sa chceš podeliť o návrh na zlepšenie,"
				+ " napíš nám cez "+ChatColor.BOLD+"/eb report"+ChatColor.RED+" <sprava>\n";
		warning += ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.DARK_RED+ChatColor.BOLD+"WARNING!"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]";
		playerEB.getPlayer().sendMessage(warning);
		
		if(isGameRunning()) {
			playerEB.getStatusBoard().setup("Wait for end game.");
			new LocationTeleport(playerEB,getMap(),GameLocation.SPECTATOR);
		}else if(stage instanceof StageLobbyLaunching) {
			playerEB.getStatusBoard().setup("Launching in:");
			new LocationTeleport(playerEB,getMap(),GameLocation.LOBBY);
		}else {
			playerEB.getStatusBoard().setup("Waiting for players...");
			playerEB.getStatusBoard().tick(-1);
			new LocationTeleport(playerEB,getMap(),GameLocation.LOBBY);
		}
		if(stage instanceof StageLobbyLaunching||stage instanceof StageLobbyWaiting) {
			new LobbyInventory(playerEB);
		}else {
			new SpectatorInventory(playerEB);
		}
	}
	
	private void playerPostJoin(PlayerEB playerEB) {
		if(players.size()==2) {
			stage.end();
			stage = new StageLobbyLaunching();
		}
		playerEB.getPlayer().setGameMode(GameMode.SURVIVAL);
		playerEB.getPlayer().setFoodLevel(20);
		playerEB.getPlayer().setHealth(20);
		for(PlayerEB p : players) {
			p.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" sa pripojil/a do hry.");
		}
	}
	
	public void playerForceLeave(PlayerEB playerEB) {
		if(playerEB.getGameStage()==GameStage.GAME_RUNNING) {
			playersInGame--;
		}
		
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
		playerEB.getStatusBoard().clean();
		playerEB.getPlayerDataSaver().restoreAll();
		for(PlayerEB p : players) {
			p.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" opustil/a hru.");
		}
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
		playerEB.setGameStage(GameStage.GAME_SPECTATOR);
		for(PlayerEB pEB : players) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.DARK_GRAY+playerEB.getPlayer().getName()+ChatColor.GRAY +" vybuchol.");
		}
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Vybuchol si.");
		new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.SPECTATOR);
		new SpectatorInventory(playerEB);
		playersInGame--;
		checkPlayersSituation();
	}
	
	public void playerDied(PlayerEB playerEB,PlayerEB playerEB2) {
		playerEB.setGameStage(GameStage.GAME_SPECTATOR);
		for(PlayerEB pEB : players) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.DARK_GRAY+playerEB2.getPlayer().getName()+ChatColor.GOLD+" ➼ "
			+ChatColor.DARK_GRAY+playerEB.getPlayer().getName()+ChatColor.DARK_RED+" ☢");
		}
		playerEB2.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Zabil si hráča "+ChatColor.DARK_GRAY+playerEB.getPlayer().getName());
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Bol si zabitý hráčom "+ChatColor.DARK_GRAY+playerEB2.getPlayer().getName());
		new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.SPECTATOR);
		new SpectatorInventory(playerEB);
		playerEB2.getUserAccount().addCoinsWithNotification(5);
		playersInGame--;
		checkPlayersSituation();
	}
	
	private void checkPlayersSituation() {
		if(playersInGame<=1) {
			String winnerName = null;
			for(PlayerEB pEB : players) {
				if(pEB.getGameStage()==GameStage.GAME_RUNNING) {
					winnerName = pEB.getPlayer().getName();
					pEB.getUserAccount().addCoinsWithNotification(10*playersOnStart);
					if(playersOnStart>=3) {
						pEB.getUserAccount().addEPointsWithNotification(10*playersOnStart);
					}
				}
			}
			for(PlayerEB pEB : players) {
				pEB.getPlayer().sendTitle(ChatColor.GOLD+winnerName,ChatColor.GRAY+"vyhral hru!", 20, 40, 20);
				pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GOLD+winnerName + ChatColor.GREEN+ " vyhral hru a nenechal sa odpáliť!");
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

	public int getPlayersOnStart() {
		return playersOnStart;
	}

	public void setPlayersOnStart(int playersOnStart) {
		this.playersOnStart = playersOnStart;
	}
}
