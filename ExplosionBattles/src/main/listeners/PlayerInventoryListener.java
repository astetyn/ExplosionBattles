package main.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.PlayerEB;
import main.kits.Kit;
import main.kits.KitChooser;

public class PlayerInventoryListener implements Listener {

	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		
		ItemStack is = e.getCurrentItem();
		
		if(is==null) {
			return;
		}
		
		for(Kit kit : KitChooser.getKits()) {

			if(is.equals(kit.getChooseItem())){
				PlayerEB playerEB = Game.getInstance().getPlayer(p);
				Kit copyKit = (Kit) kit.clone();
				playerEB.setKit(copyKit);
				playerEB.getKit().setPlayer(playerEB);
				playerEB.getPlayer().sendMessage("Kit "+kit.getName()+" uspesne nastaveny.");
				break;
			}
		}
		
		if(is.getItemMeta()==null) {
			return;
		}
		
		for(int i=0;i < Game.getInstance().getMapChooser().getMapNames().size();i++) {
			
			String clickedName = is.getItemMeta().getDisplayName();
			clickedName = ChatColor.stripColor(clickedName);
			if(clickedName.startsWith(i+"")) {
				Game.getInstance().getMapChooser().playerVoting(Game.getInstance().getPlayer(p),i);
				break;
			}
		}
		e.setCancelled(true);
	}
	
}
