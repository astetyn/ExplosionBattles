package main.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.consumables.MedKit;
import main.consumables.Smoke;
import main.kits.actions.Bush;
import main.kits.actions.JumpBoost;
import main.player.PlayerEB;
import main.weapons.LightSniper;
import net.md_5.bungee.api.ChatColor;

public class CamperKit extends Kit {

	private Bush bush;
	private JumpBoost jumpBoost;
	
	public CamperKit() {
		super(new LightSniper());
	}
	
	public CamperKit(PlayerEB playerEB) {
		super(playerEB, new LightSniper());
	}

	@Override
	public void startInit() {
		jumpBoost = new JumpBoost(getPlayerEB(),8);
		bush = new Bush(getPlayerEB(),1);
		getPlayerEB().getConsumablesManager().addBoughtItem(new Smoke(getPlayerEB()),5);
		getPlayerEB().getConsumablesManager().addBoughtItem(new MedKit(getPlayerEB()),1);
	}

	@Override
	public void onInteract(ItemStack is) {
		if(is.getType() == Material.LEAVES) {
			bush.wantsToUseBush();
		}
		if(is.getType() == Material.GLASS_BOTTLE) {
			jumpBoost.wantsToUse();
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.SAPLING,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : getIndex().toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Schovať sa a už len vyčkať..");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeapon().getItem().getItemMeta().getDisplayName());
		l.add(ChatColor.AQUA+"Obsahuje kríky, lekárničku a dymovnicu.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Camper");
		item.setItemMeta(im);
		return item;
	}

	@Override
	public String getIndex() {
		return "kit_camper";
	}

	@Override
	public int getPrice() {
		return 800;
	}

	@Override
	public void onTick() {}

	@Override
	public boolean isAlive() {
		return false;
	}

}
