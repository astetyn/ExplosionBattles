package main.weapons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import main.Game;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MiniGun extends Weapon {

	private double cooldown = 0.4;
	private int power = 12;
	private String accuracy = "Medium";
	int resetShield = 0;
	int ticks = 0;
	private final int price = 10000;
	private final boolean avaibleForVip = false;
	private final String index = "weapon_minigun";
	
	public MiniGun(PlayerEB playerEB) {
		super.setCooldown(cooldown);
		super.setPlayerEB(playerEB);
		super.setPower(power);
		super.setAccuracy(accuracy);
		super.setItem(getItem());
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.getType()==Material.WOOD_SWORD&&(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) {
			if(getPlayerEB().getPlayer().getInventory().getItemInOffHand().getType()!=Material.SHIELD) {
				getPlayerEB().getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.SHIELD,1));
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void tick() {
		if(ticks%(getCooldown()*20)==0&&ticks!=0) {
			if(getPlayerEB().getPlayer().isBlocking()) {
				Location loc1 = getPlayerEB().getPlayer().getTargetBlock(null, 100).getLocation().add(1,0,0);
				Location loc2 = getPlayerEB().getPlayer().getTargetBlock(null, 100).getLocation().add(0,1,0);
				Location loc3 = getPlayerEB().getPlayer().getTargetBlock(null, 100).getLocation().add(0,0,1);
				Location loc4 = getPlayerEB().getPlayer().getTargetBlock(null, 100).getLocation().add(-1,0,0);
				Location loc5 = getPlayerEB().getPlayer().getTargetBlock(null, 100).getLocation().add(0,0,-1);
				
				Game.getInstance().setLastShootPlayerEB(getPlayerEB());
				
				loc1.getWorld().createExplosion(loc1, 1.2F);
				loc1.getWorld().createExplosion(loc2, 1.2F);
				loc1.getWorld().createExplosion(loc3, 1.2F);
				loc1.getWorld().createExplosion(loc4, 1.2F);
				loc1.getWorld().createExplosion(loc5, 1.2F);
				
				shootParticle(getPlayerEB().getPlayer(),Particle.FLAME,2);
				
				resetShield = 0;
			}else {
				resetShield++;
			}
			if(resetShield>20) {
				getPlayerEB().getPlayer().getInventory().setItemInOffHand(null);
				resetShield = 0;
			}
			ticks = 0;
		}
		ticks++;
	}
	
	public void shootParticle(Player player, Particle particle, double velocity) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        player.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 0, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(),velocity , null);
    }

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.WOOD_SWORD,1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Nabijanie: "+ ChatColor.GOLD + getCooldown());
		l.add(ChatColor.WHITE+"Sila: "+ ChatColor.GOLD + getPower());
		l.add(ChatColor.WHITE+"Presnost: "+ ChatColor.GOLD + getAccuracy());
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Mini Gun");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public boolean isAvaibleForVip() {
		return avaibleForVip;
	}

	@Override
	public String getIndex() {
		return index;
	}
	
}
