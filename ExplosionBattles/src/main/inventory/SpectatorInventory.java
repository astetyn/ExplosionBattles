package main.inventory;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class SpectatorInventory {
	
private PlayerEB playerEB;
	
	public SpectatorInventory(PlayerEB playerEB) {
		this.playerEB = playerEB;
		giveItems();
	}
	
	private void giveItems() {
		PlayerInventory inv = playerEB.getPlayer().getInventory();
		inv.setHeldItemSlot(0);
		
		//LEAVE CLOCK
		ArrayList <String> al = new ArrayList <String> ();
		al.add(ChatColor.GOLD + "Kliknutim opustit hru.");
		ItemStack it = new ItemStack(Material.WATCH,1);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName(ChatColor.RED + ""+ChatColor.BOLD + "Leave");
		im.setLore(al);
		it.setItemMeta(im);
		//LEAVE CLOCK
		
		inv.setItem(8, it);
	}
	
}
