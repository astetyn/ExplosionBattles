package main.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import main.Game;
import main.player.PlayerEB;
import main.weapons.data.MiniGunData;
import main.weapons.data.WeaponData;

public class MiniGun extends Weapon {

	private MiniGunData miniGunData = new MiniGunData();
	
	int resetShield = 0;
	int ticks = 0;
	
	public MiniGun(PlayerEB playerEB) {
		super.setPlayerEB(playerEB);
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.equals(getWeaponData().getItem())&&(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) {
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
			if(getPlayerEB().getPlayer().isBlocking()&&(getPlayerEB().getPlayer().getInventory().getItemInMainHand().equals(getWeaponData().getItem()))) {
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
	public WeaponData getWeaponData() {
		return miniGunData;
	}
}
