package main.misc.airdrop;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;

public class DropBox {

	private boolean boxExists = false;
	
	private Location loc;
	private Location oldLoc;
	private Material fallingBox = Material.REDSTONE_BLOCK;
	private Material box = Material.CHEST;
	
	public DropBox(Location loc) {
		this.boxExists = true;
		this.loc = loc;
	}
	
	public void tick() {
		
		loc.add(new Location(loc.getWorld(),0,-1,0));
		
		Block b = loc.getWorld().getBlockAt(loc);
		if(b.getType()!=Material.AIR) {
			Block bb = oldLoc.getWorld().getBlockAt(oldLoc);
			bb.setType(box);
			bb.setMetadata("airdrop", new FixedMetadataValue(Main.getPlugin(), "weapon"));
			this.boxExists = false;
			return;
		}
		b.setType(fallingBox);
		
		if(oldLoc!=null) {
			Block bb = oldLoc.getWorld().getBlockAt(oldLoc);
			bb.setType(Material.AIR);
			Firework fw = oldLoc.getWorld().spawn(oldLoc, Firework.class);
			FireworkMeta fwm = fw.getFireworkMeta();
		    FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BURST).trail(true).build();
		    fwm.addEffect(effect);
		    fwm.setPower(0);
		    fw.setFireworkMeta(fwm);
		    fw.detonate();
		}
		oldLoc = loc.clone();
	}

	public boolean isBoxExists() {
		return boxExists;
	}

	public void setBoxExists(boolean boxExists) {
		this.boxExists = boxExists;
	}

	public Material getBox() {
		return box;
	}

}
