package main.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.player.GameStage;
import main.player.PlayerEB;
import main.stages.StageEnding;
import net.md_5.bungee.api.ChatColor;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		
		if(Game.getInstance().getPlayer(p).getGameStage()!=GameStage.GAME_RUNNING) {
			e.setCancelled(true);
			return;
		}
		
		if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
			return;
		}
		if(e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
			PlayerEB damager = null;
			if(Game.getInstance().getLastShootPlayerEB()!=null) {
				damager = Game.getInstance().getLastShootPlayerEB();
				Game.getInstance().setLastShootPlayerEB(null);
			}

			double damage = e.getDamage();
			
			damage/=4;
			
			e.setDamage(damage);
			
			boolean playerWillDie = playerCheckDamage(p,e.getDamage());
			if(playerWillDie) {
				PlayerEB playerEB = Game.getInstance().getPlayer(p);
				if(damager!=null) {
					Game.getInstance().playerDied(playerEB,damager);
				}else {
					Game.getInstance().playerDied(playerEB);
				}
				e.setCancelled(true);
			}
				
			if(damager!=null) {
				damager.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GOLD+ChatColor.BOLD+"Hit!");
			}
			
			return;
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityByEntityDamage(EntityDamageByEntityEvent e) {
		
		Entity entity = e.getEntity();
		Entity damager = e.getDamager();
		
		if(!(entity instanceof Player)) {
			return;
		}
		
		Player victim = (Player) entity;
		
		PlayerEB playerEB = Game.getInstance().getPlayer(victim);
		if(playerEB==null) {
			return;
		}
		
		double damage = e.getDamage();
		damage/=4;
		e.setDamage(damage);
		
		BigDecimal bd = new BigDecimal(Double.toString(damage));
	    bd = bd.setScale(1, RoundingMode.CEILING);
	    damage = bd.doubleValue();
	    
		MetadataValue mv = null;
		
		if(damager.getType()==EntityType.PRIMED_TNT) {
			if(!damager.hasMetadata("tnt")) {
				if(damage>=victim.getHealth()) {
					Game.getInstance().playerDied(playerEB);
					e.setCancelled(true);
				}
				return;
			}
			mv = damager.getMetadata("tnt").get(0);
		}
		
		if(mv==null) {
			
			if(damager.getType()==EntityType.FIREBALL) {
				if(!damager.hasMetadata("fireball")) {
					e.setCancelled(true);
					return;
				}
				Location loc = victim.getLocation();
				Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
				MetadataValue mvv = damager.getMetadata("fireball").get(0);
				String playerName = mvv.asString();
				tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
				((TNTPrimed)tnt).setYield(10);
				((TNTPrimed)tnt).setFuseTicks(0);
			}else if(damager.getType()==EntityType.SMALL_FIREBALL) {
				if(!damager.hasMetadata("smallfireball")) {
					e.setCancelled(true);
					return;
				}
				Location loc = victim.getLocation();
				Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
				MetadataValue mvv = damager.getMetadata("smallfireball").get(0);
				String playerName = mvv.asString();
				tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
				((TNTPrimed)tnt).setFuseTicks(0);
			}
			
			e.setCancelled(true);
			return;
		}
		
		String playerName = mv.asString();
		Player p = Bukkit.getPlayer(playerName);
		
		PlayerEB playerEB2 = Game.getInstance().getPlayer(p);
		
		if(Game.getInstance().getStage() instanceof StageEnding) {
			e.setCancelled(true);
			return;
		}
		
		p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Zásah spôsobil: "+ChatColor.RED+ChatColor.BOLD+damage+ChatColor.GRAY+" hp zranenie.");
		
		if(playerCheckDamage(victim, damage)) {
			Game.getInstance().playerDied(playerEB,playerEB2);
			e.setCancelled(true);
		}
		
	}
	
	private boolean playerCheckDamage(Player p, double damage) {
		
		double health = p.getHealth();
		
		if(health<=damage) {
			return true;
		}
		return false;
	}
}
