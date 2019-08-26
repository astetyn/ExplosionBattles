package main.weapons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import main.Game;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MiniGun extends Weapon {
	
	int resetShield = 0;
	int ticks = 0;
	
	public MiniGun() {
		super(0.4, 12, "Medium", "Drž pravé tlačítko myši a mier pod hráča.");
	}
	
	public MiniGun(PlayerEB playerEB) {
		super(playerEB, 0.4, 12, "Medium", "Drž pravé tlačítko myši a mier pod hráča.");
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.equals(getItem())&&(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) {
			if(getPlayerEB().getPlayer().getInventory().getItemInOffHand().getType()!=Material.SHIELD) {
				getPlayerEB().getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.SHIELD,1));
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void onTick() {
		if(ticks%(getCooldown()*20)==0&&ticks!=0) {
			if(getPlayerEB().getPlayer().isBlocking()&&(getPlayerEB().getPlayer().getInventory().getItemInMainHand().equals(getItem()))) {
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
			}
			ticks = 0;
		}
		resetShield++;
		if(resetShield==25) {
			getPlayerEB().getPlayer().getInventory().setItemInOffHand(null);
			resetShield = 0;
		}
		
		ticks++;
	}
	
	public void shootParticle(Player player, Particle particle, double velocity) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        player.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 0, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(),velocity , null);
	}

	@Override
	public String getIndex() {
		return "weapon_minigun";
	}

	@Override
	public int getPrice() {
		return 1200;
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.IRON_AXE,1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : getIndex().toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.WHITE+"Nabíjanie: "+ ChatColor.GOLD + getCooldown());
		l.add(ChatColor.WHITE+"Sila: "+ ChatColor.GOLD + getPower());
		l.add(ChatColor.WHITE+"Presnosť: "+ ChatColor.GOLD + getAccuracy());
		l.add(ChatColor.DARK_RED+""+ChatColor.ITALIC+"✪  Auto-Fire ✪");
		im.setLore(l);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		im.setUnbreakable(true);
		im.setDisplayName(ChatColor.YELLOW+"Mini Gun");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public boolean isAlive() {
		return true;
	}
}
