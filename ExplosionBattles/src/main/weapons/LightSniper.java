package main.weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class LightSniper extends Weapon {

	private double cooldown = 6;
	private int power = 60;
	private String accuracy = "High";
	private final int price = 10000;
	private final boolean avaibleForVip = false;
	private final String index = "weapon_lightsniper";
	
	public LightSniper(PlayerEB playerEB) {
		
		super.setCooldown(cooldown);
		super.setPlayerEB(playerEB);
		super.setPower(power);
		super.setAccuracy(accuracy);
		super.setItem(getItem());
		
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.getType()==Material.BLAZE_ROD) {
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
			playerEB.getPlayer().sendMessage("Na dalsiu strelu pockaj este "+waitSeconds+" sec.");
			return;
		}
		setLastUse(System.currentTimeMillis());
		fireFireball(playerEB);
	}
	
	public void fireFireball(PlayerEB playerEB) {
		Player p = playerEB.getPlayer();
		SmallFireball fb = p.launchProjectile(SmallFireball.class);
		fb.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
		fb.setMetadata("smallfireball", new FixedMetadataValue(Main.getPlugin(), p.getName()));
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.BLAZE_ROD,1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Nabijanie: "+ ChatColor.GOLD + getCooldown());
		l.add(ChatColor.WHITE+"Sila: "+ ChatColor.GOLD + getPower());
		l.add(ChatColor.WHITE+"Presnost: "+ ChatColor.GOLD + getAccuracy());
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Light Sniper");
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
