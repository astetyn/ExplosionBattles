package main.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.PlayerEB;
import main.STATE;
import main.kits.Kit;
import main.kits.KitChooser;
import main.misc.airdrop.AirDrop;
import main.misc.inventory.MapVotingInventory;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		PlayerEB playerEB = Game.getInstance().getPlayer(p);
		
		
		if(e.getAction()!=null) {
			if(e.getAction()==Action.LEFT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_BLOCK) {
				AirDrop ad = Game.getInstance().getAirDrop();
				if(ad!=null) {
					Game.getInstance().getAirDrop().onInteractBlock(playerEB, e.getClickedBlock());
				}
			}
		}
		
		if(e.getHand()==null) {
			return;
		}
		
		ItemStack it = p.getInventory().getItemInMainHand();
		Material material = it.getType();
		
		if(material.equals(Material.PAPER)) {
			new KitChooser(playerEB);
			e.setCancelled(true);
		}else if(material.equals(Material.WATCH)) {
			Game.getInstance().playerForceLeave(playerEB);
			e.setCancelled(true);
		}else if(material.equals(Material.WOOL)) {
			new MapVotingInventory(playerEB);
			e.setCancelled(true);
		}
		
		if(playerEB.getState()!=STATE.GAME_RUNNING) {
			return;
		}
		
		Kit kit = playerEB.getKit();
		kit.onInteract(it);
			
		e.setCancelled(true);
	}
}
