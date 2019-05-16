package main.stages;

import main.Game;
import main.MsgCenter;
import main.configuration.Configuration;
import main.gameobjects.airdrop.AirDrop;
import main.maps.MapCreator;
import main.maps.MapSystemChecker;
import main.player.GameStage;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class StageGameRunning extends Stage {

	private int finalTicks;
	
	public StageGameRunning() {
		Configuration c = Game.getInstance().getConfiguration();
		int seconds = c.getConfig().getInt("game.seconds-game");
		finalTicks = seconds*20;
		setFinalTicks(finalTicks);
		start();
	}
	
	@Override
	public void start() {
		Game.getInstance().setGameRunning(true);
		String map = Game.getInstance().getMapChooser().getWinMap();
		Game.getInstance().setMap(map);
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.getPlayer().getInventory().clear();
			playerEB.getPlayer().updateInventory();
		}
		
		MapSystemChecker msc = new MapSystemChecker(map);
		if(!msc.isMapCompleted()) {
			return;
		}
		
		new MapCreator(Game.getInstance().getMap());
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Vyhrala mapa: "+ChatColor.DARK_GREEN+ChatColor.BOLD+map);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Tvoja úloha: "+ChatColor.YELLOW+"ostaň posledný v hre!");
			playerEB.setGameStage(GameStage.GAME_RUNNING);
			playerEB.getPlayer().setHealth(20);
			playerEB.getKit().startInit();
			playerEB.getWeapon().equip();
			playerEB.getStatusBoard().setup("Game time:");
		}
		int playersCount = Game.getInstance().getPlayers().size();
		Game.getInstance().setPlayersInGame(playersCount);
		Game.getInstance().setPlayersOnStart(playersCount);
	}
	
	@Override
	public void tick() {
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			if(playerEB.getGameStage()!=GameStage.GAME_RUNNING) {
				continue;
			}
			playerEB.getWeapon().tick();
			playerEB.getKit().tick();
		}
		
		if(getTicks()%20!=0) {
			return;
		}
		if(Game.getInstance().getAirDrop()!=null) {
			Game.getInstance().getAirDrop().tick();
		}
		
		if(getTicks()==10*20) {
			Game.getInstance().setAirDrop(new AirDrop());
		}
		
		if(Game.getInstance().getAirDrop()!=null) {
			if(!Game.getInstance().getAirDrop().isInProcess()) {
				if(getTicks()%(120*20)==0&&getTicks()!=0) {
					Game.getInstance().setAirDrop(new AirDrop());
					for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
						if(playerEB.getGameStage()!=GameStage.GAME_RUNNING) {
							continue;
						}
						playerEB.getUserAccount().addCoinsWithNotification(2);
					}
				}
			}
		}
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().tick(remainingTime);
		}
		
		if(Game.getInstance().getWorldsEB().isNight()) {
			Game.getInstance().getWorldsEB().getGameWorld().setTime(18000);
		}else {
			Game.getInstance().getWorldsEB().getGameWorld().setTime(6000);
		}
		if(getTicks()==getFinalTicks()) {
			end();
			Game.getInstance().setStage(new StageEnding());
		}
	}
	
	public void end() {
		Game.getInstance().setGameRunning(false);
		Game.getInstance().setWorldsEB(null);
	}

}
