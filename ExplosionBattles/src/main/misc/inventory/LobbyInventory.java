package main.misc.inventory;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import main.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class LobbyInventory {

	private PlayerEB playerEB;
	
	public LobbyInventory(PlayerEB playerEB) {
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
		
		//KIT PAPER
		ArrayList <String> al2 = new ArrayList <String> ();
		al2.add(ChatColor.GOLD + "Kliknutim otvoris ponuku kitov.");
		ItemStack it2 = new ItemStack(Material.PAPER,1);
		ItemMeta im2 = it2.getItemMeta();
		im2.setDisplayName(ChatColor.YELLOW + ""+ChatColor.BOLD + "Kity");
		im2.setLore(al2);
		it2.setItemMeta(im2);
		//KIT PAPER
		
		inv.setItem(8, it);
		inv.setItem(0, it2);
		/*playerEB.getPlayer().getInventory().setContents(inv.getContents());
		playerEB.getPlayer().updateInventory();*/
	}
}
