package main.kits.actions;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class BombAssault {

	private Entity decoy;
	private ItemStack item;
	private int itemTicks = 0;
	private int bombTicks = 0;
	private final int activationTicks = 100;  
	private final int bombardmentTicks = 150;
	private Location locForBombs;
	private int radius = 10;
	private boolean inProcess = false;
	private PlayerEB playerEB;
	private final int itemCooldownTicks = 1200;
	
	public BombAssault(PlayerEB playerEB, int inventorySlot) {
		this.playerEB = playerEB;
		item = new ItemStack(Material.NETHER_STAR,1);
		ItemMeta im2 = item.getItemMeta();
		im2.setDisplayName("Call help from sky.");
		item.setItemMeta(im2);
		playerEB.getPlayer().getInventory().setItem(inventorySlot, item);
	}

	public boolean isInProcess() {
		return inProcess;
	}

	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}
	
	public void wantsToCallAssault() {
		if(inProcess) {
			return;
		}
		Location loc = playerEB.getPlayer().getLocation();
		inProcess = true;
		decoy = loc.getWorld().dropItem(loc, new ItemStack(Material.NETHER_STAR,1));
		decoy.setVelocity(loc.getDirection().normalize().multiply(2));
		playerEB.getPlayer().getInventory().removeItem(item);
	}
	
	public void tick() {
		itemTicks++;
		
		if(itemTicks == itemCooldownTicks) {
			playerEB.getPlayer().getInventory().addItem(item);
			itemTicks = 0;
		}
		
		if(!inProcess) {
			return;
		}
		
		if(bombTicks%10==0&&bombTicks!=0) {
			if(bombTicks<activationTicks) {
				Location locFirework = decoy.getLocation().clone();
				locFirework.add(0,1,0);
				Firework fw = locFirework.getWorld().spawn(locFirework, Firework.class);
				FireworkEffect effect = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.WHITE).with(FireworkEffect.Type.BALL).build();
			    FireworkMeta fwm = fw.getFireworkMeta();
			    fwm.clearEffects();
			    fwm.addEffect(effect);
			    fwm.setPower(1);
			    fw.setFireworkMeta(fwm);
			    fw.detonate();
			}else if(bombTicks==activationTicks) {
				Location checkArea = decoy.getLocation().clone();
				for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
					playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+MsgCenter.ALLERT+ChatColor.RED+"Upozornenie!"+ChatColor.GRAY+" Bombardovanie v oblasti: "
							+ "x"+ChatColor.YELLOW+(int)checkArea.getX()+ChatColor.GRAY+" z"+ChatColor.YELLOW+(int)checkArea.getZ()+MsgCenter.ALLERT);
				}
				Block b = checkArea.getBlock();
				int cycle = 0;
				while(b.getType()!=Material.BARRIER&&b.getType()!=Material.BEDROCK) {
					checkArea.add(0,1,0);
					b = checkArea.getBlock();
					cycle++;
					if(cycle==200) {
						break;
					}
				}
				locForBombs = checkArea.clone();
			}else if(bombTicks<activationTicks+bombardmentTicks){
				int startX = locForBombs.getBlockX()-radius;
				int startZ = locForBombs.getBlockZ()-radius;
				int bombX = (int) (startX+(Math.random()*radius*2));
				int bombZ = (int) (startZ+(Math.random()*radius*2));
				Location loc = new Location(locForBombs.getWorld(),bombX,locForBombs.getY()-2,bombZ);
				Entity tnt = loc.getWorld().spawn(loc,TNTPrimed.class);
				((TNTPrimed)tnt).setFuseTicks(200);
				tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), playerEB.getPlayer().getName()));
			}else {
				inProcess = false;
				bombTicks = 0;
				return;
			}
		}	
		bombTicks++;
	}
}
