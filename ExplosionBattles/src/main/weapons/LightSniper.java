package main.weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LightSniper extends Weapon {
	
	public LightSniper() {
		super(6, 60, "High", "Namier na hráča a klikni.");
	}
	
	public LightSniper(PlayerEB playerEB) {
		super(playerEB, 6, 60, "High", "Namier na hráča a klikni.");
	}
	
	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.equals(getItem())) {
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
		    String message = ChatColor.GRAY+"Nabíjanie ešte "+ChatColor.YELLOW+ChatColor.BOLD+waitSeconds+ChatColor.GRAY+" sec...";
			playerEB.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
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
	public String getIndex() {
		return "weapon_lightsniper";
	}

	@Override
	public int getPrice() {
		return 800;
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
		im.setLore(l);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		im.setUnbreakable(true);
		im.setDisplayName(ChatColor.YELLOW+"Light Sniper");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public void onTick() {}

	@Override
	public boolean isAlive() {
		return false;
	}
}
