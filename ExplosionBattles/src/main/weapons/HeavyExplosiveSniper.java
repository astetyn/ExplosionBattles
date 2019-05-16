package main.weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import main.MsgCenter;
import main.player.PlayerEB;
import main.weapons.data.HeavyExplosiveSniperData;
import main.weapons.data.WeaponData;
import net.md_5.bungee.api.ChatColor;

public class HeavyExplosiveSniper extends Weapon{

	private HeavyExplosiveSniperData heavyExplosiveSniperData = new HeavyExplosiveSniperData();
	
	public HeavyExplosiveSniper(PlayerEB playerEB) {
		super.setPlayerEB(playerEB);	
	}

	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.equals(getWeaponData().getItem())) {
			wantsToFire(getPlayerEB());
		}
		return true;
	}
	
	private void wantsToFire(PlayerEB playerEB) {
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
		setLastUse(System.currentTimeMillis());
		fireFireball(playerEB);
	}
	
	public void fireFireball(PlayerEB playerEB) {
		Player p = playerEB.getPlayer();
		Fireball fb = p.launchProjectile(Fireball.class);
		fb.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
		fb.setMetadata("fireball", new FixedMetadataValue(Main.getPlugin(), p.getName()));
	}

	@Override
	public WeaponData getWeaponData() {
		return heavyExplosiveSniperData;
	}

}
