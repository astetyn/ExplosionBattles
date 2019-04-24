package main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import main.Game;

public class PlayerCommandPreprocessListener implements Listener{

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		
		String[] allowedCmds = {"ban","banip","kick","ebd","stop","reload", "mute","msg","r","p","whois","list"};
		String cmd = e.getMessage().toLowerCase();
		String[] parts = cmd.split(" ");
		cmd = parts[0];
		cmd = cmd.replaceAll("/", "");
		
		boolean pass = false;
		
		for(String allowed : allowedCmds) {
			if(cmd.equals(allowed)) {
				pass = true;
			}
		}
		
		if(pass) {
			return;
		}else {
			p.sendMessage("Toto nemozes pouzit pocas hry!");
			e.setCancelled(true);
		}
	}

}
