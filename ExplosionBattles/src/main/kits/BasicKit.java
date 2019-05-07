package main.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import main.weapons.AssaultShooter;
import main.weapons.Weapon;
import net.md_5.bungee.api.ChatColor;

public class BasicKit extends Kit{

	private Weapon weapon = new AssaultShooter(null);
	private final int price = 10000;
	private final boolean avaibleForVip = false;
	private final String index = "kit_basic";
	
	public BasicKit(PlayerEB playerEB) {
		setPlayer(playerEB);
		setDefaultWeapon(weapon);
	}
	
	@Override
	public void startInit() {
		if(getPlayerEB().getWeapon()==null) {
			weapon.setPlayerEB(getPlayerEB());
			getPlayerEB().setWeapon(weapon);
		}else if(!getPlayerEB().getWeapon().isSetThisRound()){
			weapon.setPlayerEB(getPlayerEB());
			getPlayerEB().setWeapon(weapon);
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STICK,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Default kit for TNTs.");
		l.add(ChatColor.WHITE+"Obsahuje zbran "+weapon.getItem().getItemMeta().getDisplayName());
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Basic TNT Kit");
		item.setItemMeta(im);
		return item;
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
