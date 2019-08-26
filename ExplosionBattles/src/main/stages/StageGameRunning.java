package main.stages;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.gameobjects.NukeAssault;
import main.gameobjects.SecondTickable;
import main.gameobjects.SupplyPackage;
import main.gameobjects.Tickable;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapCreator;
import main.maps.MapSystemChecker;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.SpectatorInventoryLayout;
import main.weapons.WeaponsManager;
import net.md_5.bungee.api.ChatColor;

public class StageGameRunning extends Stage {

	private List<Tickable> activeTickableObjects;
	private List<SecondTickable> activeSecondTickableObjects;
	private Game game;
	
	public StageGameRunning(Game game) {
		super("game.seconds-game");
		this.game = game;
		start();
	}
	
	@Override
	public void start() {
		activeTickableObjects = new ArrayList<Tickable>();
		activeSecondTickableObjects = new ArrayList<SecondTickable>();
		
		String map = game.getMapsManager().getWinMap();
		game.setMap(map);
		
		for(PlayerEB playerEB : game.getPlayersInGame()) {
			playerEB.getPlayer().getInventory().clear();
			playerEB.getPlayer().updateInventory();
		}
		
		MapSystemChecker msc = new MapSystemChecker(map);
		if(!msc.isMapCompleted()) {
			return;
		}
		new MapCreator(game.getMap());
		
		activeSecondTickableObjects.add(game.getWorldsEB());
		
		for(PlayerEB playerEB : game.getPlayersInGame()) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Vyhrala mapa: "+ChatColor.GREEN+ChatColor.BOLD+map);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Tvoja úloha: "+ChatColor.YELLOW+"ostaň posledný v hre!");
			playerEB.setGameStage(GameStage.GAME_RUNNING);
			readyPlayer(playerEB);
			if(!playerEB.isChosenWeapon()) {
				playerEB.setWeapon(WeaponsManager.createWeapon(playerEB.getKit().getWeapon(), playerEB));
			}
			playerEB.getKit().startInit();
			playerEB.getWeapon().equip();
			playerEB.getStatusBoard().setup("Game time:");
			playerEB.getConsumablesManager().addAllToInventory();
			activeTickableObjects.add(playerEB.getWeapon());
			activeTickableObjects.add(playerEB.getKit());
			activeSecondTickableObjects.add(playerEB.getStatusBoard());
		}
		int playersCount = game.getPlayersInGame().size();
		game.setPlayersInRunningGame(playersCount);
		game.setPlayersOnStart(playersCount);
	}
	
	@Override
	public void onTick() {
		
		List<Tickable> copyArray = new ArrayList<Tickable>(activeTickableObjects);
		for(Tickable tickable : copyArray) {
			if(!tickable.isAlive()) {
				activeTickableObjects.remove(tickable);
				continue;
			}
			tickable.onTick();
		}

		//From here it will count only seconds.
		if(getTicks()%20!=0||getTicks()==0) {
			return;
		}
		
		List<SecondTickable> copyArray2 = new ArrayList<SecondTickable>(activeSecondTickableObjects);
		for(SecondTickable secondTickable : copyArray2) {
			if(!secondTickable.isAlive()) {
				activeSecondTickableObjects.remove(secondTickable);
				continue;
			}
			secondTickable.onSecTick();
		}
		
		int passedSeconds = getTicks()/20;
		
		if(passedSeconds%(4*60)==0) {
			activeSecondTickableObjects.add(new TimeTeleport());
		}
		
		if(passedSeconds == 80) {
			activeTickableObjects.add(new NukeAssault());
		}
		
		if(passedSeconds == 10) {
			SupplyPackage sp = new SupplyPackage();
			Bukkit.getPluginManager().registerEvents(sp, Main.getPlugin());
			activeTickableObjects.add(sp);
		}
		
		if(passedSeconds%(200)==0) {
			activeTickableObjects.add(new NukeAssault());
		}
		
		if(passedSeconds%(120)==0) {
			SupplyPackage sp = new SupplyPackage();
			Bukkit.getPluginManager().registerEvents(sp, Main.getPlugin());
			activeTickableObjects.add(sp);
			for(PlayerEB playerEB : game.getPlayersInGame()) {
				if(playerEB.getGameStage()!=GameStage.GAME_RUNNING) {
					continue;
				}
				playerEB.getUserAccount().addCoinsWithNotification(2);
			}
		}
		
		if(getTicks()==getEndTick()) {
			end();
			for(PlayerEB pEB : game.getPlayersInGame()) {
				pEB.getPlayer().sendTitle(ChatColor.GRAY+"Remíza","", 20, 40, 20);
				pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+ChatColor.BOLD+"Remíza");
			}
			game.setStage(new StageEnding(game));
		}
	}
	
	public void end() {
		game.setWorldsEB(null);
	}

	public List<Tickable> getActiveGameObjects() {
		return activeTickableObjects;
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		playerEB.getStatusBoard().setup("Wait for end game.");
		playerEB.setInventoryLayout(new SpectatorInventoryLayout(playerEB));
		new LocationTeleport(playerEB,game.getMap(),GameLocation.SPECTATOR);
		readyPlayer(playerEB);
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {
		if(playerEB.getGameStage()==GameStage.GAME_RUNNING) {
			int pirg = game.getPlayersInRunningGame();
			game.setPlayersInRunningGame(pirg-1);
			game.checkPlayersSituation();
		}
	}
}
