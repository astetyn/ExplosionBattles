package main.stages;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import main.Game;
import main.MsgCenter;
import main.configuration.ConfigurationEB;
import main.player.PlayerEB;

public abstract class Stage {

	private int ticks;
	private int endTick;

	public Stage(int endTick) {
		this.ticks = 0;
		this.endTick = endTick;
	}
	
	public Stage(String configKey) {
		this.ticks = 0;
		ConfigurationEB c = Game.getInstance().getConfiguration();
		int seconds = c.getConfig().getInt(configKey);
		this.endTick = seconds*20;
	}
	
	/** This method is called when player is joining game. */
	public void onJoin(Player p) {
		
		if(Game.getInstance().getConfiguration().getConfig().getInt("game.max-players")==Game.getInstance().getPlayersInGame().size()) {
			p.sendMessage(MsgCenter.PREFIX+"Je tu plno. Počkaj kým sa niekto odpojí..");
			return;
		}
		
		PlayerEB playerEB = new PlayerEB(p);
	
		Game.getInstance().getPlayersInGame().add(playerEB);
		String warning = "";
		warning += ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.DARK_RED+ChatColor.BOLD+"WARNING!"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]\n";
		warning +=ChatColor.RED+"Táto minihra je vo vývoji. Ak natrafíš na bug alebo sa chceš podeliť o návrh na zlepšenie,"
				+ " napíš nám cez "+ChatColor.BOLD+"/eb report"+ChatColor.RED+" <sprava>\n";
		warning += ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.DARK_RED+ChatColor.BOLD+"WARNING!"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]";
		p.sendMessage(warning);
		
		for(PlayerEB pEB : Game.getInstance().getPlayersInGame()) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" sa pripojil/a do hry.");
		}
		
		p.setGameMode(GameMode.SURVIVAL);
		onPostJoin(playerEB);
	}
	
	/** This is called when player is leaving game.*/
	public void onLeave(PlayerEB playerEB) {
		Game.getInstance().getPlayersInGame().remove(playerEB);
		playerEB.getStatusBoard().clean();
		playerEB.getPlayerDataSaver().restoreAll();
		for(PlayerEB pEB : Game.getInstance().getPlayersInGame()) {
			pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" opustil/a hru.");
		}
		playerEB.getPlayer().setFoodLevel(20);
		playerEB.getPlayer().setHealth(20);
		onPostLeave(playerEB);
		if(Game.getInstance().getPlayersInGame().size()<=1) {
			this.end();
			Game.getInstance().setStage(new StageLobbyWaiting(Game.getInstance()));
		}
	}
	
	public abstract void onPostJoin(PlayerEB playerEB);
	
	public abstract void onPostLeave(PlayerEB playerEB);
	
	/** This method is called when Stage is ready to start.*/
	public abstract void start();
	
	/** This method is called from Clock every tick. 20x/sec.*/
	public abstract void onTick();
	
	/** This method should be called when new Stage is goind to be created.*/
	public abstract void end();

	public void increaseTicks() {
		this.ticks++;
	}

	/** This method will ready player for any activity, can be overriden but must be called from there.*/
	public void readyPlayer(PlayerEB playerEB) {
		playerEB.getPlayer().setFoodLevel(20);
		playerEB.getPlayer().setHealth(20);
		for(PotionEffect pe : playerEB.getPlayer().getActivePotionEffects()) {
			playerEB.getPlayer().removePotionEffect(pe.getType());
		}
	}
	
	public int getEndTick() {
		return endTick;
	}

	public void setEndTick(int finalTicks) {
		this.endTick = finalTicks;
	}

	public int getTicks() {
		return ticks;
	}
}
