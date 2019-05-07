package main.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import main.Main;

public class BlockIgniteListener implements Listener {

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent e) {
		if(e.getIgnitingEntity()==null) {
			return;
		}
		
		if(e.getIgnitingEntity().getType()==EntityType.SMALL_FIREBALL) {
			Entity entity= e.getIgnitingEntity();
			if(!entity.hasMetadata("smallfireball")) {
				return;
			}
			Location loc = e.getBlock().getLocation();
			
			if(entity.hasMetadata("smallfireball")) {
				
				Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
				MetadataValue mv = entity.getMetadata("smallfireball").get(0);
				String playerName = mv.asString();
				tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
				((TNTPrimed)tnt).setFuseTicks(0);
			}
			e.setCancelled(true);
		}
	}
}
