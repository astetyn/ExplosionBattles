package main.kits;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import main.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class BasicKit extends Kit{
	
	private long lastFire = 0;
	private final String name = "Basic Kit";
	private final double gunCooldown = 2;
	
	public BasicKit() {
		setGunCooldown(gunCooldown);
		setName(name);
	}
	
	public BasicKit(PlayerEB playerEB) {
		setPlayer(playerEB);
		setGunCooldown(gunCooldown);
		setName(name);
	}
	
	@Override
	public void startInit() {
		ItemStack is = new ItemStack(Material.STICK,1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW+"Flying Explosioo");
		is.setItemMeta(im);
		getPlayerEB().getPlayer().getInventory().setItem(0, is); 
	}
	
	@Override
	public void onInteract(ItemStack it) {
		if(it.getType()==Material.STICK) {
			wantsToFire();
		}
	}
	
	public void wantsToFire() {
		long actualTime = System.currentTimeMillis();
		
		double diff = actualTime - lastFire;
		double seconds = diff / 1000;
		
		if(seconds<getGunCooldown()) {
		
		    double waitSeconds = getGunCooldown()-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
			playerEB.getPlayer().sendMessage("Na dalsiu strelu pockaj este "+waitSeconds+" sec.");
			return;
		}
		fireTNT();
	}
	
	public void fireTNT() {
		Player p = getPlayerEB().getPlayer();
		Entity tnt = p.getWorld().spawn(p.getLocation(),TNTPrimed.class);
		tnt.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
		tnt.setMetadata("tnt", new FixedMetadataValue(Main.getPlugin(), p.getName()));
		((TNTPrimed)tnt).setFuseTicks(30);
		lastFire = System.currentTimeMillis();
	}

	@Override
	public ItemStack getChooseItem() {
		ItemStack item = new ItemStack(Material.STICK,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Default kit for TNTs.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Basic TNT Kit");
		item.setItemMeta(im);
		return item;
	}
	
}
