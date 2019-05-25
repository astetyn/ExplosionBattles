package main.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import main.Main;

public class EntityExplodeListener implements Listener {

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		
		List<Block> copyList = new ArrayList<Block>(e.blockList());
		for(Block b : copyList){
			
			if(b.getType()==Material.TNT) {
				Location loc = b.getLocation();
				Entity tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
				((TNTPrimed)tnt).setFuseTicks(0);
			}
			
			if(b.hasMetadata("supply")) {
				e.blockList().remove(b);
				continue;
			}
			b.setType(Material.AIR);
		}
		
		if(e.getEntityType()==EntityType.FIREBALL) {
			if(!e.getEntity().hasMetadata("fireball")) {
				return;
			}
			
			Location loc = e.getLocation();
			Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
			MetadataValue mv = e.getEntity().getMetadata("fireball").get(0);
			String playerName = mv.asString();
			tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
			((TNTPrimed)tnt).setYield(10);
			((TNTPrimed)tnt).setFuseTicks(0);
			e.setCancelled(true);
		}else if(e.getEntityType()==EntityType.WITHER_SKULL) {
			if(!e.getEntity().hasMetadata("snapdragon")) {
				return;
			}
			
			Location loc = e.getLocation();
			Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
			MetadataValue mv = e.getEntity().getMetadata("snapdragon").get(0);
			String playerName = mv.asString();
			tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
			((TNTPrimed)tnt).setFuseTicks(0);
			e.setCancelled(true);
		}
	}
}
