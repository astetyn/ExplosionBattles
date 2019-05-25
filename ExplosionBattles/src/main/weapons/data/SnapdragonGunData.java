package main.weapons.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SnapdragonGunData extends WeaponData {

	private final double defaultCooldown = 20;
	private final int power = 40;
	private final String accuracy = "Ultra";
	private final int price = 7000;
	private final boolean avaibleForVip = false;
	private final String index= "weapon_snapdragongun";
	private final String manualMsg = "Vystreľ raketu pri hráčovi.";
	private final boolean limited = true;

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.DIAMOND_AXE,1);
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
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		im.setUnbreakable(true);
		im.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Snapdragon Gun");
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

	@Override
	public boolean isLimited() {
		return limited;
	}

}
