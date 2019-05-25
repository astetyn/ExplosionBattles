package main.player.consumables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Game;
import main.MsgCenter;
import main.gameobjects.GameObject;
import main.player.PlayerEB;
import main.stages.Stage;
import main.stages.StageGameRunning;

public class Smoke extends GameObject implements Consumable {

	private final String index = "consumable_smoke";
	private final int price = 50;
	private final boolean limited = false;
	private PlayerEB playerEB;
	private boolean active = false;
	private int ticks = 0;
	private final int maxTicks = 8;
	private List<SmokeData> activeSmokes = new ArrayList<SmokeData>();
	
	public Smoke(PlayerEB playerEB) {
		this.playerEB = playerEB;
		active = true;
	}

	@Override
	public void onInteract() {
		Location loc = playerEB.getPlayer().getLocation();
		playerEB.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100,1));
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Aktivovala sa ti "+ChatColor.WHITE+"neviditeľnosť"+ChatColor.GRAY+" na 5 sekúnd.");
		Entity smokeEntity = loc.getWorld().dropItem(loc, new ItemStack(getItem()));
		smokeEntity.setVelocity(loc.getDirection().normalize().multiply(2));
		playerEB.getPlayer().getInventory().removeItem(getItem());
		activeSmokes.add(new SmokeData(0,smokeEntity));
		Stage stage = Game.getInstance().getStage();
		if(stage instanceof StageGameRunning) {
			if(!((StageGameRunning) stage).getActiveGameObjects().contains(this)) {
				((StageGameRunning) stage).getActiveGameObjects().add(this);
			}
		}
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item;
		Dye dye = new Dye();
		dye.setColor(DyeColor.GRAY);
		item = dye.toItemStack(1);
		ItemMeta im = item.getItemMeta();
		
		ArrayList<String> lore = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		lore.add(hidden);
		
		im.setLore(lore);
		
		im.setDisplayName("Dymovnica");
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
	public boolean isAvaibleForVip() {
		return false;
	}

	@Override
	public boolean isLimited() {
		return limited;
	}

	@Override
	public void tick() {
		ticks++;
		if(ticks%10==0) {
			List<SmokeData> copy = new ArrayList<SmokeData>(activeSmokes);
			for(SmokeData sd : copy) {
				Entity smokeEntity = sd.getEntity();
				Location loc = smokeEntity.getLocation();
				loc.add(0,1,0);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE,loc,2);
				sd.addTick();
				if(sd.getTicks()>=maxTicks) {
					activeSmokes.remove(sd);
				}
			}
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

}

class SmokeData {
	
	private int ticks;
	private Entity entity;
	
	public SmokeData(int ticks, Entity entity) {
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
