package main.kits.actions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import main.player.PlayerEB;

public class Smoke {

	private PlayerEB playerEB;
	private ItemStack item;
	private int ticks = 0;
	private final int activationTicks = 40;
	private final int maxTicks = 400;
	private final int itemCooldownTicks = 1000;
	private List<SmokeData> smokes = new ArrayList<SmokeData>();
	
	public Smoke(PlayerEB playerEB, int inventorySlot) {
		this.playerEB = playerEB;
		Dye dye = new Dye();
		dye.setColor(DyeColor.GRAY);
		item = dye.toItemStack(1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("Smoke");
		item.setItemMeta(im);
		playerEB.getPlayer().getInventory().setItem(inventorySlot, item);
		playerEB.getPlayer().updateInventory();
	}
	
	public void wantsToUseSmoke(Location loc) {
		Entity smoke = loc.getWorld().dropItem(loc, new ItemStack(item));
		smoke.setVelocity(loc.getDirection().normalize().multiply(2));
		SmokeData sd = new SmokeData(0,smoke);
		smokes.add(sd);
		playerEB.getPlayer().getInventory().removeItem(item);
	}
	
	public void tick() {
		
		if(ticks%10==0&&ticks!=0) {
			
			List<SmokeData> list = new ArrayList<SmokeData>(smokes);
			
			for(SmokeData sd : list) {
				int smokeTicks = sd.ticks;
				if(smokeTicks<activationTicks) {
					sd.ticks+=10;
					continue;
				}
				if(smokeTicks>=maxTicks) {
					smokes.remove(sd);
					continue;
				}
				Location loc = sd.ent.getLocation();
				loc.add(0,1,0);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE,loc,2);
				sd.ticks+=10;
			}
			
			if(ticks>=itemCooldownTicks) {
				playerEB.getPlayer().getInventory().addItem(item);
				ticks = 0;
			}
		}
		ticks++;
	}
}


class SmokeData {
	
	public int ticks = 0;
	public Entity ent = null;
	
	public SmokeData(int ticks, Entity ent) {
		this.ticks = ticks;
		this.ent = ent;
	}
	
}
