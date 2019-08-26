package main.kits.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.MsgCenter;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class JumpBoost {

	long lastUse = 0;
	long cooldown = 40*1000;
	PlayerEB playerEB;
	
	public JumpBoost(PlayerEB playerEB, int inventorySlot) {
		this.playerEB = playerEB;
		ItemStack item = new ItemStack(Material.GLASS_BOTTLE,1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Jump Boost");
		item.setItemMeta(im);
		playerEB.getPlayer().getInventory().setItem(inventorySlot, item);
	}
	
	public void wantsToUse() {
		long time = System.currentTimeMillis();
		if(time<lastUse+cooldown) {
			double diff = time - lastUse;
			double seconds = diff / 1000;
			
			double waitSeconds = cooldown/1000-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Na ďalší Jump Boost počkaj ešte "+ChatColor.YELLOW+ChatColor.BOLD+waitSeconds+ChatColor.GRAY+" sec...");
			return;
		}
		giveEffect();
		lastUse = time;
	}
	
	private void giveEffect() {
		playerEB.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 10));
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Jump Boost aktivovaný na 10 sekúnd.");
	}
	
}
