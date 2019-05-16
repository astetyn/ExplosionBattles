package main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import main.Game;

public class EntityRegainHealthListener implements Listener {

	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent e) {
		
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		
		Player p = (Player) e.getEntity();
		if(Game.getInstance().isPlayerInGame(p)) {
			if(e.getRegainReason()==RegainReason.SATIATED) {
				e.setCancelled(true);
			}
		}
		
	}

}
