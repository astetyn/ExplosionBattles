package main.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {

	@EventHandler
	public void onBlockExplosion(BlockExplodeEvent e) {
		
		List<Block> copyList = new ArrayList<Block>(e.blockList());
		for(Block b : copyList){
			
			if(b.getType()==Material.TNT) {
				Location loc = b.getLocation();
				Entity tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
				((TNTPrimed)tnt).setFuseTicks(0);
			}
			
			if(b.hasMetadata("airdrop")) {
				e.blockList().remove(b);
				continue;
			}
			b.setType(Material.AIR);
		}
	}

}
