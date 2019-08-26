package main.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.kits.actions.JumpBoost;
import main.player.PlayerEB;
import main.weapons.AssaultShooter;
import net.md_5.bungee.api.ChatColor;

public class BasicKit extends Kit {
	
	private JumpBoost jumpBoost;
	
	public BasicKit() {
		super(new AssaultShooter());
	}
	
	public BasicKit(PlayerEB playerEB) {
		super(playerEB, new AssaultShooter());
	}
	
	@Override
	public void startInit() {
		jumpBoost = new JumpBoost(getPlayerEB(),8);
	}
	
	@Override
	public void onInteract(ItemStack is) {
		if(is.getType() == Material.GLASS_BOTTLE) {
			jumpBoost.wantsToUse();
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STICK,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : getIndex().toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Základný kit bez vylepšení.");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeapon().getItem().getItemMeta().getDisplayName());
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Basic TNT Kit");
		item.setItemMeta(im);
		return item;
	}

	@Override
	public String getIndex() {
		return "kit_basic";
	}

	@Override
	public int getPrice() {
		return 0;
	}

	@Override
	public void onTick() {}

	@Override
	public boolean isAlive() {
		return false;
	}
	
}
