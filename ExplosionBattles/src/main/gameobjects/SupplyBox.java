package main.gameobjects;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;

public class SupplyBox {

	private boolean boxFalling = false;
	private Location loc;
	private Location oldLoc;
	private Material fallingBox = Material.REDSTONE_BLOCK;
	private Material box = Material.CHEST;
	private int tickCounter = 0;
	private String[] bonuses;
	
	public SupplyBox(Location loc, String[] bonuses) {
		this.boxFalling = true;
		this.loc = loc;
		this.bonuses = bonuses;
	}
	
	public void tick() {
		tickCounter++;
		loc.add(new Location(loc.getWorld(),0,-1,0));
		
		Block b = loc.getWorld().getBlockAt(loc);
		if(b.getType()!=Material.AIR) {
			if(oldLoc==null) {
				oldLoc = loc;
			}
			Block bb = oldLoc.getWorld().getBlockAt(oldLoc);
			bb.setType(box);
			Random rand = new Random();
			int index = rand.nextInt(bonuses.length);
			bb.setMetadata("supply", new FixedMetadataValue(Main.getPlugin(), bonuses[index]));
			loc = oldLoc;
			this.boxFalling = false;
			return;
		}
		b.setType(fallingBox);
		
		if(oldLoc!=null) {
			Block bb = oldLoc.getWorld().getBlockAt(oldLoc);
			bb.setType(Material.AIR);
			if(tickCounter%2==0) {
				Firework fw = oldLoc.getWorld().spawn(oldLoc, Firework.class);
				FireworkMeta fwm = fw.getFireworkMeta();
			    FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BURST).trail(true).build();
			    fwm.addEffect(effect);
			    fwm.setPower(5);
			    fw.setFireworkMeta(fwm);
			    fw.detonate();
			}
		}
		oldLoc = loc.clone();
	}

	public Location getBoxLocation() {
		return loc;
	}
	
	public boolean isBoxFalling() {
		return boxFalling;
	}

	public Material getBox() {
		return box;
	}

}
