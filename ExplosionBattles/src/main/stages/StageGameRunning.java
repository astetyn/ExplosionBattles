package main.stages;

import main.Game;
import main.configuration.Configuration;
import main.gameobjects.airdrop.AirDrop;
import main.inventory.InventoryManager;
import main.maps.MapCreator;
import main.maps.MapSystemChecker;
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
		
		InventoryManager.getInstance().clearInventoryAll();
		MapSystemChecker msc = new MapSystemChecker(map);
		if(!msc.isMapCompleted()) {
			return;
		}
		
		new MapCreator(Game.getInstance().getMap());
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.getPlayer().sendMessage("Vyhrala mapa: "+ChatColor.GREEN+map);
			playerEB.setInRunningGame(true);
			playerEB.getPlayer().setHealth(20);
			playerEB.getKit().startInit();
			playerEB.getWeapon().equip();
			playerEB.getStatusBoard().setup("Game will end in:");
		}
		Game.getInstance().setPlayersInGame(Game.getInstance().getPlayers().size());
	}
	
	@Override
	public void tick() {
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
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
				}
			}
		}
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			int remainingTime = (getFinalTicks()-getTicks())/20;
			playerEB.getStatusBoard().setData(remainingTime);
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
