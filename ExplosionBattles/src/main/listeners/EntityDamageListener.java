package main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import main.Game;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
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
	
	@EventHandler
	public void onEntityByEntityDamage(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)) {
			return;
		}
		
		Player damager = (Player) e.getDamager();
		if(!Game.getInstance().isPlayerInGame(damager)) {
			return;
		}
		
		Bukkit.broadcastMessage("damageByEntity");
		e.setCancelled(true);
		
	}
}
