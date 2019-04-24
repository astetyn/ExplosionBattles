package main;

import org.bukkit.Bukkit;

import main.maps.LocationTeleport;
import main.maps.MapChooser;
import main.maps.MapCreator;
import main.maps.MapSystemChecker;
import main.misc.StatusBoard;
import main.misc.airdrop.AirDrop;
import main.misc.inventory.InventoryManager;

public class StateManager {

	Clock clock;
	STATE state = STATE.LOBBY_WAITING;
	
	public void stateStart() {
		switch(state) {
		case LOBBY_WAITING:{
			Game.getInstance().setMapChooser(new MapChooser());
			InventoryManager.getInstance().giveItemsByStateAll();
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				playerEB.setSpectator(false);
				playerEB.setState(state);
				playerEB.getPlayer().setHealth(20);
				new LocationTeleport(playerEB,state,Game.getInstance().getMap());
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setWaiting();
			}
			break;
		}
		case LOBBY_LAUNCHING:{
			Game.getInstance().setMapChooser(new MapChooser());
			InventoryManager.getInstance().giveItemsByStateAll();
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				playerEB.setSpectator(false);
				playerEB.setState(state);
				playerEB.getPlayer().setHealth(20);
				new LocationTeleport(playerEB,state,Game.getInstance().getMap());
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setLaunching();
			}
			startClock(20);
			break;
		}
		case GAME_RUNNING:{
			
			String map = Game.getInstance().getMapChooser().getWinMap();
			Game.getInstance().setMap(map);
			
			InventoryManager.getInstance().clearInventoryAll();
			MapSystemChecker msc = new MapSystemChecker(Game.getInstance().getMap());
			if(!msc.isMapCompleted()) {
				return;
			}
			
			new MapCreator(Game.getInstance().getMap());
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				playerEB.getPlayer().sendMessage("Vyhrala mapa: "+map);
				playerEB.setSpectator(false);
				playerEB.setState(state);
				playerEB.getPlayer().setHealth(20);
				playerEB.getKit().startInit();
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setGameRunning();
			}
			Game.getInstance().setPlayersInGame(Game.getInstance().getPlayers().size());
			startClock(500);
			break;
		}
		case ENDING:{
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				playerEB.setState(state);
				playerEB.getPlayer().setHealth(20);
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setEnding();
			}
			startClock(5);
			break;
		}
		}
	}
	
	public void stateTick() {
		int remainingTime = clock.getMaxTicks()-clock.getTicks()-1;
		switch(state) {
		case LOBBY_WAITING:{
			
			break;
		}
		case LOBBY_LAUNCHING:{
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setData(remainingTime);
			}
			break;
		}
		case GAME_RUNNING:{
			
			if(Game.getInstance().getAirDrop()!=null) {
				Game.getInstance().getAirDrop().tick();
			}
			
			if(clock.getTicks()==10) {
				Game.getInstance().setAirDrop(new AirDrop());
			}
			
			if(Game.getInstance().getAirDrop()!=null) {
				if(!Game.getInstance().getAirDrop().isInProcess()) {
					if(clock.getTicks()%120==0&&clock.getTicks()!=0) {
						Game.getInstance().setAirDrop(new AirDrop());
					}
				}
			}
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setData(remainingTime);
			}
			
			if(Game.getInstance().getWorldsEB().isNight()) {
				Game.getInstance().getWorldsEB().getGameWorld().setTime(18000);
			}else {
				Game.getInstance().getWorldsEB().getGameWorld().setTime(6000);
			}
			
			break;
		}
		case ENDING:{
			for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
				StatusBoard statusBoard = new StatusBoard(playerEB);
				statusBoard.setData(remainingTime);
			}
			break;
		}
		}
	}
	
	public void stateEnd() {
		switch(state) {
		case LOBBY_WAITING:{
			changeState(STATE.LOBBY_LAUNCHING);
			break;
		}
		case LOBBY_LAUNCHING:{
			changeState(STATE.GAME_RUNNING);
			break;
		}
		case GAME_RUNNING:{
			
			changeState(STATE.ENDING);
			Game.getInstance().setWorldsEB(null);
			break;
		}
		case ENDING:{
			changeState(STATE.LOBBY_LAUNCHING);
			break;
		}
		}
	}
	
	public void changeState(STATE newState) {
		clockStop();
		if(newState==state) {
			return;
		}
		state = newState;
		stateStart();
	}
	
	public void clockStop() {
		if(clock==null) {
			return;
		}
		int index = clock.getIndex();
		if(Bukkit.getScheduler().isQueued(index)) {
			Bukkit.getScheduler().cancelTask(index);
		}
		clock = null;
	}
	
	public void startClock(int seconds) {
		clockStop();
		clock = new Clock(this,seconds);
		int index = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), clock, 20, 20);
		clock.setIndex(index);
	}
	
	public STATE getState() {
		return state;
	}
}
