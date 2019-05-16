package main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import main.Game;
import main.player.shop.Shop;

public class PlayerCloseInventoryListener implements Listener {

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		
		Player p = (Player) event.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		
		Shop shop = Game.getInstance().getPlayer(p).getShop();
		if(shop.isWaitingForConfirmation()) {
			shop.setWaitingForConfirmation(false);
		}
		
	}

}
