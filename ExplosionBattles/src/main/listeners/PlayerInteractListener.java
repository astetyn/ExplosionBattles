package main.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.gameobjects.airdrop.AirDrop;
import main.layouts.MapVotingInventory;
import main.player.PlayerEB;
import main.stages.StageLobbyLaunching;
import main.stages.StageLobbyWaiting;

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
		
		if(e.getHand()==EquipmentSlot.OFF_HAND) {
			return;
		}
		
		ItemStack is = p.getInventory().getItemInMainHand();
		Material material = is.getType();
		
		if(Game.getInstance().getStage() instanceof StageLobbyWaiting || Game.getInstance().getStage() instanceof StageLobbyLaunching) {
			if(material.equals(Material.WATCH)) {
				Game.getInstance().playerForceLeave(playerEB);
				e.setCancelled(true);
			}else if(material.equals(Material.WOOL)) {
				new MapVotingInventory(playerEB);
				e.setCancelled(true);
			}else if(material.equals(Material.CHEST)) {
				playerEB.getShop().openInventory();
				e.setCancelled(true);
			}
			return;
		}
		
		if(playerEB.getWeapon()!=null) {
			boolean canceled = playerEB.getWeapon().onInteract(e);
			if(!canceled) {
				return;
			}
		}
		
		if(playerEB.getKit()!=null) {
			playerEB.getKit().onInteract(is);
		}
		
		e.setCancelled(true);
	}
}
