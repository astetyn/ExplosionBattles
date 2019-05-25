package main.player.consumables;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.MsgCenter;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MedKit implements Consumable {

	private final String index = "consumable_medkit";
	private final int price = 50;
	private final boolean limited = false;
	private PlayerEB playerEB;
	
	public MedKit(PlayerEB playerEB) {
		this.playerEB = playerEB;
	}
	
	@Override
	public void onInteract() {
		if(playerEB.getPlayer().getHealth()==20) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Máš plný život.");
			return;
		}
		playerEB.getPlayer().setHealth(20);
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Život doplnený.");
		playerEB.getPlayer().getInventory().removeItem(getItem());
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ENDER_CHEST,1);
		ItemMeta im = item.getItemMeta();

		ArrayList<String> lore = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : index.toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		lore.add(hidden);
		
		im.setLore(lore);
		im.setDisplayName("Lekárnička");
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
		return false;
	}

	@Override
	public boolean isLimited() {
		return limited;
	}

}
