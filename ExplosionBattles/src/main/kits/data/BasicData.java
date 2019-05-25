package main.kits.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.weapons.data.AssaultShooterData;
import main.weapons.data.WeaponData;
import net.md_5.bungee.api.ChatColor;

public class BasicData extends KitData {

	private WeaponData weaponData = new AssaultShooterData();
	private final int price = 0;
	private final boolean avaibleForVip = true;
	private final String index = "kit_basic";
	private final boolean limited = false;
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STICK,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Základný kit bez vylepšení.");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeaponData().getItem().getItemMeta().getDisplayName());
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Basic TNT Kit");
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
