package main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import main.Game;

public class EntityDamageListener implements Listener {

	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		Bukkit.broadcastMessage("damage");
		e.setCancelled(true);
	}
}
