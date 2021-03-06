package main.kits.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.weapons.data.LightSniperData;
import main.weapons.data.WeaponData;
import net.md_5.bungee.api.ChatColor;

public class CamperData extends KitData {

	private WeaponData weaponData = new LightSniperData();
	private final int price = 5000;
	private final boolean avaibleForVip = true;
	private final String index = "kit_camper";
	private final boolean limited = false;

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.OAK_SAPLING,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Schovať sa a už len vyčkať..");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeaponData().getItem().getItemMeta().getDisplayName());
		l.add(ChatColor.AQUA+"Obsahuje kríky, lekárničku a dymovnicu.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Camper");
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
	
	@Override
	public boolean isLimited() {
		return limited;
	}
	
}
