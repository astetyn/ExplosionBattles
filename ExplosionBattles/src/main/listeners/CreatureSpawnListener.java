package main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import main.Game;
import main.maps.world.WorldsEB;

public class CreatureSpawnListener implements Listener{

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		
		WorldsEB wEB = Game.getInstance().getWorldsEB();
		
		if(wEB==null) {
			return;
		}
		
		if(e.getSpawnReason()==SpawnReason.CUSTOM) {
			return;
		}
		
		if(wEB.getGameWorld()!=null) {
			if(e.getLocation().getWorld()==wEB.getGameWorld()) {
				e.setCancelled(true);
			}
		}
		if(wEB.getSavedWorld()!=null) {
			if(e.getLocation().getWorld()==wEB.getSavedWorld()) {
				e.setCancelled(true);
			}
		}
		
		
	}
}
