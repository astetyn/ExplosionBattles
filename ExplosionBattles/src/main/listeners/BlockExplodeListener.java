package main.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockExplosionListener implements Listener {

	@EventHandler
	public void onBlockExplosion(EntityExplodeEvent e) {
		for(Block b : e.blockList()){
			if(b.hasMetadata("airdrop")) {
				e.blockList().remove(b);
				continue;
			}
			b.setType(Material.AIR);
		}
	}

}
