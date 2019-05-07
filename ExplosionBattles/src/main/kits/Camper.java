package main.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.kits.actions.Bush;
import main.kits.actions.MedKit;
import main.kits.actions.Smoke;
import main.player.PlayerEB;
import main.weapons.LightSniper;
import main.weapons.Weapon;
import net.md_5.bungee.api.ChatColor;

public class Camper extends Kit {

	private Weapon weapon = new LightSniper(null);
	Smoke smoke;
	MedKit medkit;
	Bush bush;
	private final int price = 10000;
	private final boolean avaibleForVip = false;
	private final String index = "kit_camper";
	
	public Camper(PlayerEB playerEB) {
		setPlayer(playerEB);
		setDefaultWeapon(weapon);
	}

	@Override
	public void startInit() {
		bush = new Bush(getPlayerEB(),1);
		smoke = new Smoke(getPlayerEB(),2);
		medkit = new MedKit(getPlayerEB(),3);
		if(getPlayerEB().getWeapon()==null) {
			weapon.setPlayerEB(getPlayerEB());
			getPlayerEB().setWeapon(weapon);
		}else if(!getPlayerEB().getWeapon().isSetThisRound()){
			weapon.setPlayerEB(getPlayerEB());
			getPlayerEB().setWeapon(weapon);
		}
	}

	@Override
	public void onInteract(ItemStack is) {
		if(is.getType() == Material.INK_SACK) {
			smoke.wantsToUseSmoke(getPlayerEB().getPlayer().getLocation());
		}
		if(is.getType() == Material.ENDER_CHEST) {
			medkit.wantsToUseMedKit();
		}
		if(is.getType() == Material.LEAVES) {
			bush.wantsToUseBush();
		}
	}
	
	@Override
	public void tick() {
		smoke.tick();
		medkit.tick();
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.SAPLING,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Hide and wait. Everyday life.");
		l.add(ChatColor.WHITE+"Obsahuje zbran "+weapon.getItem().getItemMeta().getDisplayName());
		l.add("K dispozicii mas Smoke a MedKit.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Camper");
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
