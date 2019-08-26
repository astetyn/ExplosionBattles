package main.stages;

import org.bukkit.Location;

import main.Game;
import main.MsgCenter;
import main.configuration.MapConfiguration;
import main.gameobjects.SecondTickable;
import main.player.GameStage;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class TimeTeleport implements SecondTickable {

	private int sec;
	private int finalSec;
	private boolean alive;
	
	public TimeTeleport() {
		sec = 0;
		finalSec = 4;
		alive = true;
	}
	
	@Override
	public void onSecTick() {
		
		notifyBeforeTeleport(sec, finalSec);
		if(sec==finalSec) {
			teleport();
			alive = false;
			return;
		}
		sec++;
	}
	
	private void notifyBeforeTeleport(int second, int finalSec) {
		for(PlayerEB pEB : Game.getInstance().getPlayersInGame()) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX + ChatColor.GOLD+"Teleport všetkých hráčov za: "+ChatColor.GREEN+ChatColor.BOLD+(finalSec-second));
		}
	}
	
	private void teleport() {
		
		MapConfiguration wc = new MapConfiguration(Game.getInstance().getMap());
		Location loc = (Location) wc.getConfig().get("loc"+3);
		
		for(PlayerEB pEB : Game.getInstance().getPlayersInGame()) {
			if(pEB.getGameStage()==GameStage.GAME_RUNNING) {
				pEB.getPlayer().teleport(loc);
				pEB.getPlayer().sendMessage(MsgCenter.PREFIX + ChatColor.GOLD+"Bol si teleportnutý!");
			}
		}
	}

	@Override
	public boolean isAlive() {
		return alive;
	}
	
}
