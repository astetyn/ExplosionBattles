package main.listeners;

import org.bukkit.Location;
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
		if(e.getEntityType()==EntityType.FIREBALL) {
			if(!e.getEntity().hasMetadata("fireball")) {
				return;
			}
			Location loc = e.getLocation();
			Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
			MetadataValue mv = e.getEntity().getMetadata("fireball").get(0);
			String playerName = mv.asString();
			tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerName));
			((TNTPrimed)tnt).setFuseTicks(0);
		}
	}
}
