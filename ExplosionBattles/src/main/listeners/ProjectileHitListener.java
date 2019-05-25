package main.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import main.Game;
import main.Main;

public class ProjectileHitListener implements Listener {

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {

		if(!(e.getHitEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getHitEntity();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		
		Entity victim = e.getHitEntity();
		Entity damager = e.getEntity();
		
		if(damager.getType()==EntityType.FIREBALL) {
			if(!damager.hasMetadata("fireball")) {
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
				return;
			}
			Location loc = victim.getLocation();
			Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
			MetadataValue mvv = damager.getMetadata("smallfireball").get(0);
			String playerName = mvv.asString();
			tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
			((TNTPrimed)tnt).setFuseTicks(0);
		}
		
	}

}
