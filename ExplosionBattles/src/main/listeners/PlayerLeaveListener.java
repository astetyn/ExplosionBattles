package main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import main.Game;

public class PlayerLeaveListener implements Listener {

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		Game.getInstance().getStage().onLeave(Game.getInstance().getPlayer(p));
	}
	
}
