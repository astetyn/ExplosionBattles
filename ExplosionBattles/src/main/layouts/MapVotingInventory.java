package main.layouts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Game;
import main.maps.MapVoteItem;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MapVotingInventory {

	Inventory votingInventory = Bukkit.createInventory(null, 9, ChatColor.BLUE+""+ChatColor.BOLD+"-= Map chooser =-");
	PlayerEB playerEB;
	
	public static List<MapVoteItem> getMapItems() {
		List<MapVoteItem> items = new ArrayList<MapVoteItem>();
		List<String> mapNames = Game.getInstance().getMapChooser().getMapNames();
		
		for(int i=0;i<mapNames.size();i++) {
			String mapName = mapNames.get(i);
			ItemStack it = new ItemStack(Material.WOOL,1);
			ItemMeta im = it.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN+""+ChatColor.BOLD + mapName);
			it.setItemMeta(im);
			items.add(new MapVoteItem(it,i));
		}
		return items;
	}
	
	public MapVotingInventory(PlayerEB playerEB) {
		this.playerEB = playerEB;
		
		int i = 0;
		for(MapVoteItem mvi : getMapItems()) {
			votingInventory.setItem(i, mvi.getIs());
			i++;
		}
		showInventory();
	}

	private void showInventory() {
		playerEB.getPlayer().openInventory(votingInventory);
	}
	
}
