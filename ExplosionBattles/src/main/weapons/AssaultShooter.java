package main.weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import main.MsgCenter;
import main.player.PlayerEB;
import main.weapons.data.AssaultShooterData;
import main.weapons.data.WeaponData;
import net.md_5.bungee.api.ChatColor;

public class AssaultShooter extends Weapon {
	
	private AssaultShooterData assaultShooterData = new AssaultShooterData();
	
	public AssaultShooter(PlayerEB playerEB) {
		super.setPlayerEB(playerEB);
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.equals(assaultShooterData.getItem())) {
			wantsToFire(getPlayerEB());
		}
		return true;
	}
	
	public void wantsToFire(PlayerEB playerEB) {
		long actualTime = System.currentTimeMillis();
		
		double diff = actualTime - getLastUse();
		double seconds = diff / 1000;
		
		if(seconds<getCooldown()) {
		
		    double waitSeconds = getCooldown()-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Nabíjanie ešte "+ChatColor.YELLOW+ChatColor.BOLD+waitSeconds+ChatColor.GRAY+" sec...");
			return;
		}
		fireTNT(playerEB);
	}
	
	public void fireTNT(PlayerEB playerEB) {
		Player p = playerEB.getPlayer();
		Entity tnt = p.getWorld().spawn(p.getLocation(),TNTPrimed.class);
		tnt.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
		tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), p.getName()));
		((TNTPrimed)tnt).setFuseTicks(30);
		setLastUse(System.currentTimeMillis());
	}

	@Override
	public WeaponData getWeaponData() {
		return assaultShooterData;
	}
	
}
