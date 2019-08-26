package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import main.configuration.ConfigurationEB;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.voting.MapsManager;
import main.maps.world.WorldsEB;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.SpectatorInventoryLayout;
import main.stages.Stage;
import main.stages.StageEnding;
import main.stages.StageLobbyWaiting;

public class Game {

	private static Game game = new Game();
	private String map;
	private Stage stage;
	private Clock clock;
	private MapsManager mapsManager;
	private WorldsEB worldsEB;
	private ConfigurationEB configuration;
	private int playersInRunningGame;
	private List<PlayerEB> playersInGame;
	private PlayerEB lastShootPlayerEB;
	private int playersOnStart;
	
	public Game() {
		this.configuration = new ConfigurationEB(Main.getPlugin());
		this.clock = new Clock(this);
		this.playersInGame = new ArrayList<PlayerEB>();
		this.playersInRunningGame = 0;
		this.playersOnStart = 0;
	}
	
	public void postInit() {
		stage = new StageLobbyWaiting(this);
	}
	
	public boolean isPlayerInGame(Player p) {
		for(PlayerEB playerEB : playersInGame) {
			if(playerEB.getPlayer().equals(p)) {
				return true;
			}
		}
		return false;
	}
	
	public PlayerEB getPlayer(Player p) {
		for(PlayerEB playerEB : playersInGame) {
			if(playerEB.getPlayer().equals(p)) {
				return playerEB;
			}
		}
		return null;
	}
	
	public void playerDied(PlayerEB playerEB) {
		playerEB.setGameStage(GameStage.GAME_SPECTATOR);
		for(PlayerEB pEB : playersInGame) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.DARK_GRAY+playerEB.getPlayer().getName()+ChatColor.GRAY +" vybuchol.");
		}
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Vybuchol si.");
		new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.SPECTATOR);
		playerEB.setInventoryLayout(new SpectatorInventoryLayout(playerEB));
		for(PotionEffect pe : playerEB.getPlayer().getActivePotionEffects()) {
			playerEB.getPlayer().removePotionEffect(pe.getType());
		}
		playerEB.getPlayer().setHealth(20);
		playersInRunningGame--;
		checkPlayersSituation();
	}
	
	public void playerDied(PlayerEB playerEB,PlayerEB playerEB2) {
		playerEB.setGameStage(GameStage.GAME_SPECTATOR);
		for(PlayerEB pEB : playersInGame) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.DARK_GRAY+playerEB2.getPlayer().getName()+ChatColor.GOLD+" ➼ "
			+ChatColor.DARK_GRAY+playerEB.getPlayer().getName()+ChatColor.DARK_RED+" ☢");
		}
		playerEB2.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Zabil si hráča "+ChatColor.DARK_GRAY+playerEB.getPlayer().getName());
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Bol si zabitý hráčom "+ChatColor.DARK_GRAY+playerEB2.getPlayer().getName());
		new LocationTeleport(playerEB,Game.getInstance().getMap(),GameLocation.SPECTATOR);
		playerEB.setInventoryLayout(new SpectatorInventoryLayout(playerEB));
		for(PotionEffect pe : playerEB.getPlayer().getActivePotionEffects()) {
			playerEB.getPlayer().removePotionEffect(pe.getType());
		}
		playerEB.getPlayer().setHealth(20);
		playerEB2.getUserAccount().addCoinsWithNotification(5);
		playersInRunningGame--;
		checkPlayersSituation();
	}
	
	public void checkPlayersSituation() {
		
		if(playersInRunningGame<=1) {
			String winnerName = null;
			for(PlayerEB pEB : playersInGame) {
				if(pEB.getGameStage()==GameStage.GAME_RUNNING) {
					winnerName = pEB.getPlayer().getName();
					pEB.getUserAccount().addCoinsWithNotification(10*playersOnStart);
					if(playersOnStart>=3) {
						pEB.getUserAccount().addEPointsWithNotification(10*playersOnStart);
					}
				}
			}
			for(PlayerEB pEB : playersInGame) {
				pEB.getPlayer().sendTitle(ChatColor.GOLD+winnerName,ChatColor.GRAY+"vyhral hru!", 20, 40, 20);
				pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GOLD+winnerName + ChatColor.GREEN+ " vyhral hru a nenechal sa odpáliť!");
			}
			stage.end();
			stage = new StageEnding(game);
		}
	}
	
	public static Game getInstance() {
		return game;
	}

	public List<PlayerEB> getPlayersInGame() {
		return playersInGame;
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

	public int getPlayersInRunningGame() {
		return playersInRunningGame;
	}

	public void setPlayersInRunningGame(int playersInGame) {
		this.playersInRunningGame = playersInGame;
	}

	public MapsManager getMapsManager() {
		return mapsManager;
	}
	
	public void setMapsManager(MapsManager mapsManager) {
		this.mapsManager = mapsManager;
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

	public ConfigurationEB getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ConfigurationEB configuration) {
		this.configuration = configuration;
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
