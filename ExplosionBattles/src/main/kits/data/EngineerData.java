package main.kits.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.weapons.data.MiniGunData;
import main.weapons.data.WeaponData;
import net.md_5.bungee.api.ChatColor;

public class EngineerData extends KitData {

	private WeaponData weaponData = new MiniGunData();
	private final int price = 0;
	private final boolean avaibleForVip = false;
	private final String index = "kit_engineer";
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ANVIL,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Technika ti pomôže.");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeaponData().getItem().getItemMeta().getDisplayName());
		l.add(ChatColor.AQUA+"Možnosť stavať pevnosti a");
		l.add(ChatColor.AQUA+"zavolať bombardovanie.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Engineer");
		item.setItemMeta(im);
		return item;
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
	public WeaponData getWeaponData() {
		return weaponData;
	}

}
