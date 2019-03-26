package main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import main.Game;

public class EntityItemListener implements Listener {

	
	@EventHandler
	public void onPickItem(PlayerPickupItemEvent e) {
		Player p = (Player) e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		Bukkit.broadcastMessage("pickItem");
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		Bukkit.broadcastMessage("dropItem");
		e.setCancelled(true);
	}
	
}
