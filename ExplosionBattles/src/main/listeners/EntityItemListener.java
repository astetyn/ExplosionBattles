package main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

import main.Game;

public class EntityItemListener implements Listener {

	@EventHandler
	public void onPickItem(EntityPickupItemEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		Bukkit.broadcastMessage("pickItem");
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDropItem(EntityDropItemEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		Bukkit.broadcastMessage("dropItem");
		e.setCancelled(true);
	}
	
}
