package main.maps.voting;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import main.Game;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MapVoteInventory {

	Inventory inventory;
	
	public MapVoteInventory(PlayerEB playerEB) {
		List<MapVoteItem> mapItems = Game.getInstance().getMapsManager().getMapItems();
		inventory = Bukkit.createInventory(null, 9, ChatColor.BLUE+""+ChatColor.BOLD+"-= Hlasovanie mapy =-");
		
		for(int i = 0; i<mapItems.size();i++) {
			MapVoteItem mvi = mapItems.get(i);
			inventory.setItem(i, mvi.getIs());
		}
		
		playerEB.getPlayer().openInventory(inventory);
	}

}
