package main.kits.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Leaves;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class Bush {

	long lastUse = 0;
	long cooldown = 30*1000;
	PlayerEB playerEB;
	
	public Bush(PlayerEB playerEB, int inventorySlot) {
		this.playerEB = playerEB;
		ItemStack item = new ItemStack(Material.LEAVES,1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW+"Hide behind bushes.");
		item.setItemMeta(im);
		playerEB.getPlayer().getInventory().setItem(inventorySlot, item);
	}
	
	public void wantsToUseBush() {
		long time = System.currentTimeMillis();
		if(time<lastUse+cooldown) {
			double diff = time - lastUse;
			double seconds = diff / 1000;
			
			double waitSeconds = cooldown/1000-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
			playerEB.getPlayer().sendMessage("Na dalsi krik pockaj este "+waitSeconds+" sec.");
			return;
		}
		buildBush(playerEB.getPlayer().getLocation());
		lastUse = time;
	}
	
	public void buildBush(Location loc) {
		Location customLoc = loc.clone();
		
		for(int o = 0; o<2;o++) {
			
			customLoc = loc.clone();
			customLoc.add(0+o,0,1);
			Block bb = customLoc.getBlock();
			if(bb.getType()!=Material.AIR) {
				continue;
			}
			bb.setType(Material.LEAVES);
			BlockState statee = bb.getState();
			Leaves ll = (Leaves) statee.getData();
			ll.setDecayable(false);
			statee.setData(ll);
			statee.update(false,false);
			

			customLoc = loc.clone();
			customLoc.add(-2,o,-2);
			for(int i = 0; i<5;i++) {
				
				customLoc.add(0,0,1);
				
				double d = Math.random();
				if(d>0.8&&o==1) {
					continue;
				}
			
				Block b = customLoc.getBlock();
				if(b.getType()!=Material.AIR) {
					continue;
				}
				b.setType(Material.LEAVES);
				BlockState state = b.getState();
				Leaves l = (Leaves) state.getData();
				l.setDecayable(false);
				state.setData(l);
				state.update(false,false);
				
			}
			
			customLoc = loc.clone();
			customLoc.add(-2,o,2);
			for(int i = 0; i<5;i++) {
				
				customLoc.add(1,0,0);
				
				double d = Math.random();
				if(d>0.8&&o==1) {
					continue;
				}
				
				Block b = customLoc.getBlock();
				if(b.getType()!=Material.AIR) {
					continue;
				}
				b.setType(Material.LEAVES);
				BlockState state = b.getState();
				Leaves l = (Leaves) state.getData();
				l.setDecayable(false);
				state.setData(l);
				state.update(false,false);
			}
			customLoc = loc.clone();
			customLoc.add(2,o,-2);
			for(int i = 0; i<5;i++) {
				
				customLoc.add(0,0,1);
				
				double d = Math.random();
				if(d>0.8&&o==1) {
					continue;
				}

				Block b = customLoc.getBlock();
				if(b.getType()!=Material.AIR) {
					continue;
				}
				b.setType(Material.LEAVES);
				BlockState state = b.getState();
				Leaves l = (Leaves) state.getData();
				l.setDecayable(false);
				state.setData(l);
				state.update(false,false);
			}
			customLoc = loc.clone();
			customLoc.add(-2,o,-2);
			for(int i = 0; i<5;i++) {
				
				customLoc.add(1,0,0);
				
				double d = Math.random();
				if(d>0.6&&o==1) {
					continue;
				}
				
				Block b = customLoc.getBlock();
				if(b.getType()!=Material.AIR) {
					continue;
				}
				b.setType(Material.LEAVES);
				BlockState state = b.getState();
				Leaves l = (Leaves) state.getData();
				l.setDecayable(false);
				state.setData(l);
				state.update(false,false);
			}
		}
	}

}
