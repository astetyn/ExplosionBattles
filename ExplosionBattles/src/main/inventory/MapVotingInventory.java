package main.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Game;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MapVotingInventory {

	Inventory votingInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD+""+ChatColor.BOLD+"  -= Map chooser =-  ");
	PlayerEB playerEB;
	
	public MapVotingInventory(PlayerEB playerEB) {
		this.playerEB = playerEB;
		List<String> mapNames = Game.getInstance().getMapChooser().getMapNames();
		
		for(int i=0;i<mapNames.size();i++) {
			
			String mapName = mapNames.get(i);
			ItemStack it = new ItemStack(Material.GRASS,1);
			ItemMeta im = it.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + ""+i+" "+ChatColor.BOLD + mapName);
			it.setItemMeta(im);
			votingInventory.setItem(i, it);
			
		}
		showInventory();
	}

	private void showInventory() {
		playerEB.getPlayer().openInventory(votingInventory);
	}
	
}
