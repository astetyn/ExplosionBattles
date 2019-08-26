package main.consumables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Game;
import main.MsgCenter;
import main.gameobjects.Tickable;
import main.player.PlayerEB;
import main.stages.Stage;
import main.stages.StageGameRunning;
import net.md_5.bungee.api.ChatColor;

public class Grenade implements Tickable, Consumable {

	private final String index = "consumable_grenade";
	private final int price = 100;
	private PlayerEB playerEB;
	private boolean active;
	private int ticks = 0;
	private final int maxTicks = 8;
	private List<GrenadeData> activeGrenades = new ArrayList<GrenadeData>();
	
	public Grenade(PlayerEB playerEB) {
		this.playerEB = playerEB;
		this.active = true;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.SLIME_BALL,1);
		ItemMeta im = item.getItemMeta();

		ArrayList<String> lore = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		lore.add(hidden);
		
		im.setLore(lore);
		im.setDisplayName("Granát");
		item.setItemMeta(im);
		return item;
	}

	@Override
	public String getIndex() {
		return index;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public void onInteract() {
		Location loc = playerEB.getPlayer().getLocation();
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Fire in the hole!");
		Entity grenadeEntity = loc.getWorld().dropItem(loc, new ItemStack(getItem()));
		grenadeEntity.setVelocity(loc.getDirection().normalize().multiply(2));
		playerEB.getPlayer().getInventory().removeItem(getItem());
		activeGrenades.add(new GrenadeData(0,grenadeEntity));
		Stage stage = Game.getInstance().getStage();
		if(stage instanceof StageGameRunning) {
			if(!((StageGameRunning) stage).getActiveGameObjects().contains(this)) {
				((StageGameRunning) stage).getActiveGameObjects().add(this);
			}
		}
		
	}

	@Override
	public void onTick() {
		ticks++;
		if(ticks%10==0) {
			List<GrenadeData> copy = new ArrayList<GrenadeData>(activeGrenades);
			for(GrenadeData gd : copy) {
				Entity grenadeEntity = gd.getEntity();
				Location loc = grenadeEntity.getLocation();
				loc.add(0,1,0);
				loc.getWorld().spawnParticle(Particle.DRAGON_BREATH,loc,2);
				gd.addTick();
				if(gd.getTicks()>=maxTicks) {
					Entity grenadeTnt = loc.getWorld().spawn(loc,TNTPrimed.class);
					((TNTPrimed)grenadeTnt).setYield(15);
					((TNTPrimed)grenadeTnt).setFuseTicks(0);
					activeGrenades.remove(gd);
				}
			}
		}
		
	}

	@Override
	public boolean isAlive() {
		return active;
	}

}
class GrenadeData {
	
	private int ticks;
	private Entity entity;
	
	public GrenadeData(int ticks, Entity entity) {
		this.ticks = ticks;
		this.entity = entity;
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void addTick() {
		ticks++;
	}
}
