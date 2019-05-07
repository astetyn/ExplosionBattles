package main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import main.Game;

public class PlayerSwapHandItemsListener implements Listener{

	
	@EventHandler
	public void onPlayerSwapHandItemsListener(PlayerSwapHandItemsEvent e) {
		
		Player p = (Player) e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		e.setCancelled(true);	
	}

}
