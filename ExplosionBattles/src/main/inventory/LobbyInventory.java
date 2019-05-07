package main.inventory;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class LobbyInventory {

	private PlayerEB playerEB;
	
	public LobbyInventory(PlayerEB playerEB) {
		this.playerEB = playerEB;
		playerEB.getPlayer().getInventory().clear();
		giveItems();
	}
	
	private void giveItems() {
		PlayerInventory inv = playerEB.getPlayer().getInventory();
		inv.setHeldItemSlot(0);
		
		//KIT PAPER
		ArrayList <String> al = new ArrayList <String> ();
		al.add(ChatColor.GOLD + "Open kit chooser.");
		ItemStack it = new ItemStack(Material.PAPER,1);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + ""+ChatColor.BOLD + "Kits");
		im.setLore(al);
		it.setItemMeta(im);
		//KIT PAPER
		
		//MAP VOTING
		ArrayList <String> al3 = new ArrayList <String> ();
		al3.add(ChatColor.GOLD + "Click for voting map.");
		ItemStack it3 = new ItemStack(Material.WOOL,1);
		ItemMeta im3 = it3.getItemMeta();
		im3.setDisplayName(ChatColor.YELLOW + ""+ChatColor.BOLD + "Vote map");
		im3.setLore(al3);
		it3.setItemMeta(im3);
		//MAP VOTING
		
		//WEAPON CHOOSE
		ArrayList <String> al4 = new ArrayList <String> ();
		al4.add(ChatColor.GOLD + "Open weapon chooser.");
		ItemStack it4 = new ItemStack(Material.STICK,1);
		ItemMeta im4 = it4.getItemMeta();
		im4.setDisplayName(ChatColor.YELLOW + ""+ChatColor.BOLD + "Weapons");
		im4.setLore(al4);
		it4.setItemMeta(im4);
		//WEAPON CHOOSE
		
		//SHOP
		ArrayList <String> al5 = new ArrayList <String> ();
		al5.add(ChatColor.GOLD + "Buy new kits and weapons.");
		ItemStack it5 = new ItemStack(Material.CHEST,1);
		ItemMeta im5 = it5.getItemMeta();
		im5.setDisplayName(ChatColor.RED + ""+ChatColor.BOLD + "Shop");
		im5.setLore(al5);
		it5.setItemMeta(im5);
		//SHOP
		
		//LEAVE CLOCK
		ArrayList <String> al2 = new ArrayList <String> ();
		al2.add(ChatColor.GOLD + "Leave on click.");
		ItemStack it2 = new ItemStack(Material.WATCH,1);
		ItemMeta im2 = it2.getItemMeta();
		im2.setDisplayName(ChatColor.RED + ""+ChatColor.BOLD + "Leave");
		im2.setLore(al2);
		it2.setItemMeta(im2);
		//LEAVE CLOCK
		
		inv.setItem(0, it);
		inv.setItem(1, it3);
		inv.setItem(2, it4);
		inv.setItem(3, it5);
		inv.setItem(8, it2);
	}
}
