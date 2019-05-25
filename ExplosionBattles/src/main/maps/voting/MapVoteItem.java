package main.maps.voting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class MapVoteItem {

	private ItemStack is;
	private String mapName;
	private String builder;
	private int votes = 0;
	
	private final Material mapMaterial = Material.PAPER;
	
	public MapVoteItem(String mapName, String builder) {
		ItemStack is = new ItemStack(mapMaterial,1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+mapName);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY+"Postavené od: "+ChatColor.DARK_GRAY+builder);
		lore.add(ChatColor.GRAY+"Hlasy: "+ChatColor.GOLD+0);
		im.setLore(lore);
		is.setItemMeta(im);
		this.is = is;
		this.mapName = mapName;
		this.builder = builder;
	}

	public ItemStack getIs() {
		return is;
	}

	public String getMapName() {
		return mapName;
	}
	
	public String getBuilder() {
		return builder;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void setVotesAndReload(int votes) {
		this.votes = votes;
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY+"Postavené od: "+ChatColor.DARK_GRAY+builder);
		lore.add(ChatColor.GRAY+"Hlasy: "+ChatColor.GOLD+votes);
		im.setLore(lore);
		is.setItemMeta(im);
	}

}
