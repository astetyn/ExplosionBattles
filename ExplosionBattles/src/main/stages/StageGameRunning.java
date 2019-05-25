package main.stages;

import java.util.ArrayList;
import java.util.List;

import main.Game;
import main.MsgCenter;
import main.configuration.Configuration;
import main.gameobjects.GameObject;
import main.gameobjects.NukeAssault;
import main.gameobjects.SupplyPackage;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.MapCreator;
import main.maps.MapSystemChecker;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.SpectatorInventoryLayout;
import main.weapons.HeavyExplosiveSniper;
import main.weapons.WeaponsManager;
import net.md_5.bungee.api.ChatColor;

public class StageGameRunning extends Stage {

	private int finalTicks;
	private List<GameObject> activeGameObjects;
	private Game game;
	
	public StageGameRunning(Game game) {
		this.game = game;
		Configuration c = game.getConfiguration();
		int seconds = c.getConfig().getInt("game.seconds-game");
		finalTicks = seconds*20;
		setFinalTicks(finalTicks);
		start();
	}
	
	@Override
	public void start() {
		activeGameObjects = new ArrayList<GameObject>();
		String map = game.getMapsManager().getWinMap();
		game.setMap(map);
		
		for(PlayerEB playerEB : game.getPlayers()) {
			playerEB.getPlayer().getInventory().clear();
			playerEB.getPlayer().updateInventory();
		}
		
		MapSystemChecker msc = new MapSystemChecker(map);
		if(!msc.isMapCompleted()) {
			return;
		}
		
		new MapCreator(game.getMap());
		for(PlayerEB playerEB : game.getPlayers()) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Vyhrala mapa: "+ChatColor.GREEN+ChatColor.BOLD+map);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Tvoja úloha: "+ChatColor.YELLOW+"ostaň posledný v hre!");
			playerEB.setGameStage(GameStage.GAME_RUNNING);
			playerEB.getPlayer().setHealth(20);
			if(playerEB.getWeapon() instanceof HeavyExplosiveSniper) {
				playerEB.setWeapon(new WeaponsManager().createNewWeapon(playerEB.getKit().getKitData().getWeaponData(), playerEB));
			}
			playerEB.getKit().startInit();
			playerEB.getWeapon().equip();
			playerEB.getStatusBoard().setup("Game time:");
			playerEB.getConsumablesManager().addAllToInventory();
		}
		int playersCount = game.getPlayers().size();
		game.setPlayersInRunningGame(playersCount);
		game.setPlayersOnStart(playersCount);
	}
	
	@Override
	public void tick() {
		
		for(PlayerEB playerEB : game.getPlayers()) {
			if(playerEB.getGameStage()!=GameStage.GAME_RUNNING) {
				continue;
			}
			playerEB.getWeapon().tick();
			playerEB.getKit().tick();
		}
		
		List<GameObject> copyArray = new ArrayList<GameObject>(activeGameObjects);
		for(GameObject go : copyArray) {
			if(!go.isActive()) {
				activeGameObjects.remove(go);
				continue;
			}
			go.tick();
		}
		
		if(getTicks()%20!=0) {
			return;
		}
		
		if(getTicks()==80*20) {
			activeGameObjects.add(new NukeAssault());
		}
		
		if(getTicks()==10*20) {
			activeGameObjects.add(new SupplyPackage());
		}
		
		if(getTicks()%(200*20)==0&&getTicks()!=0) {
			activeGameObjects.add(new NukeAssault());
		}
		
		if(getTicks()%(120*20)==0&&getTicks()!=0) {
			activeGameObjects.add(new SupplyPackage());
			for(PlayerEB playerEB : game.getPlayers()) {
				if(playerEB.getGameStage()!=GameStage.GAME_RUNNING) {
					continue;
				}
				playerEB.getUserAccount().addCoinsWithNotification(2);
			}
		}
		
		for(PlayerEB playerEB : game.getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().tick(remainingTime);
		}
		
		if(game.getWorldsEB().isNight()) {
			game.getWorldsEB().getGameWorld().setTime(18000);
		}else {
			game.getWorldsEB().getGameWorld().setTime(6000);
		}
		if(getTicks()==getFinalTicks()) {
			end();
			for(PlayerEB pEB : game.getPlayers()) {
				pEB.getPlayer().sendTitle(ChatColor.GRAY+"Remíza","", 20, 40, 20);
				pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+ChatColor.BOLD+"Remíza");
			}
			game.setStage(new StageEnding(game));
		}
	}
	
	public void end() {
		game.setWorldsEB(null);
		
	}

	public List<GameObject> getActiveGameObjects() {
		return activeGameObjects;
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		playerEB.getStatusBoard().setup("Wait for end game.");
		new LocationTeleport(playerEB,game.getMap(),GameLocation.SPECTATOR);
		playerEB.setInventoryLayout(new SpectatorInventoryLayout(playerEB));
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
