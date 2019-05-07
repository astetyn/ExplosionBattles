package main.weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class AssaultShooter extends Weapon {

	private double cooldown = 2;
	private int power = 40;
	private String accuracy = "Very Low";
	private final int price = 10000;
	private final boolean avaibleForVip = false;
	private final String index = "weapon_assaultshooter";
	
	public AssaultShooter(PlayerEB playerEB) {
		super.setCooldown(cooldown);
		super.setPlayerEB(playerEB);
		super.setPower(power);
		super.setAccuracy(accuracy);
		super.setItem(getItem());
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.getType()==Material.STICK) {
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
			playerEB.getPlayer().sendMessage("Na dalsiu strelu pockaj este "+waitSeconds+" sec.");
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
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.STICK,1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Nabijanie: "+ ChatColor.GOLD + getCooldown());
		l.add(ChatColor.WHITE+"Sila: "+ ChatColor.GOLD + getPower());
		l.add(ChatColor.WHITE+"Presnost: "+ ChatColor.GOLD + getAccuracy());
		im.setLore(l);
		im.setDisplayName(ChatColor.GRAY+"Assault Shooter");
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
