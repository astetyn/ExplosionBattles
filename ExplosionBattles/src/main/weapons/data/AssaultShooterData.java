package main.weapons.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class AssaultShooterData extends WeaponData {

	private final double defaultCooldown = 2;
	private final int power = 40;
	private final String accuracy = "Very Low";
	private final int price = 0;
	private final boolean avaibleForVip = true;
	private final String index= "weapon_assaultshooter";
	private final String manualMsg = "Namier do vzduchu a klikni myšou.";
	private final boolean limited = false;

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.STONE_AXE,1);
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
		im.setDisplayName(ChatColor.GRAY+"Assault Shooter");
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
