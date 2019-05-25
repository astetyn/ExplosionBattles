package main.stages;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import main.Game;
import main.MsgCenter;
import main.player.PlayerEB;

public abstract class Stage {

	private int ticks = 0;
	private int finalTicks = 0;

	public void onJoin(Player p) {
		
		if(Game.getInstance().getConfiguration().getConfig().getInt("game.max-players")==Game.getInstance().getPlayers().size()) {
			p.sendMessage(MsgCenter.PREFIX+"Je tu plno. Počkaj kým sa niekto odpojí..");
			return;
		}
		
		PlayerEB playerEB = new PlayerEB(p);
	
		Game.getInstance().getPlayers().add(playerEB);
		String warning = "";
		warning += ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.DARK_RED+ChatColor.BOLD+"WARNING!"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]\n";
		warning +=ChatColor.RED+"Táto minihra je vo vývoji. Ak natrafíš na bug alebo sa chceš podeliť o návrh na zlepšenie,"
				+ " napíš nám cez "+ChatColor.BOLD+"/eb report"+ChatColor.RED+" <sprava>\n";
		warning += ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.DARK_RED+ChatColor.BOLD+"WARNING!"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]";
		p.sendMessage(warning);
		
		for(PlayerEB pEB : Game.getInstance().getPlayers()) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" sa pripojil/a do hry.");
		}
		
		p.setGameMode(GameMode.SURVIVAL);
		p.setFoodLevel(20);
		p.setHealth(20);
		onPostJoin(playerEB);
	}
	
	public void onLeave(PlayerEB playerEB) {
		Game.getInstance().getPlayers().remove(playerEB);
		playerEB.getStatusBoard().clean();
		playerEB.getPlayerDataSaver().restoreAll();
		for(PlayerEB pEB : Game.getInstance().getPlayers()) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" opustil/a hru.");
		}
		playerEB.getPlayer().setFoodLevel(20);
		playerEB.getPlayer().setHealth(20);
		onPostLeave(playerEB);
		if(Game.getInstance().getPlayers().size()<=1) {
			this.end();
			Game.getInstance().setStage(new StageLobbyWaiting(Game.getInstance()));
		}
	}
	
	public abstract void onPostJoin(PlayerEB playerEB);
	
	public abstract void onPostLeave(PlayerEB playerEB);
	
	public abstract void start();
	
	public abstract void tick();
	
	public abstract void end();

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public int getFinalTicks() {
		return finalTicks;
	}

	public void setFinalTicks(int finalTicks) {
		this.finalTicks = finalTicks;
	}
}
