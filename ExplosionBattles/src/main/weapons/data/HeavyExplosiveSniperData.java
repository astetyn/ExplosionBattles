package main.weapons.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class HeavyExplosiveSniperData extends WeaponData {

	private final double defaultCooldown = 10;
	private final int power = 200;
	private final String accuracy = "High";
	private final int price = -1;
	private final boolean avaibleForVip = false;
	private final String index = "weapnon_heavyexplosivesniper";
	private final String manualMsg = "Namier na hráča a klikni.";

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.BLAZE_ROD,1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.WHITE+"Nabíjanie: "+ ChatColor.GOLD + defaultCooldown);
		l.add(ChatColor.WHITE+"Sila: "+ ChatColor.GOLD + power);
		l.add(ChatColor.WHITE+"Presnosť: "+ ChatColor.GOLD + accuracy);
		im.setLore(l);
		im.setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"Heavy Explosive Sniper");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public String getIndex() {
		return index;
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
	public int getPower() {
		return power;
	}

	@Override
	public String getAccuracy() {
		return accuracy;
	}

	@Override
	public String getManualMsg() {
		return manualMsg;
	}

	@Override
	public double getDefaultCooldown() {
		return defaultCooldown;
	}

}
