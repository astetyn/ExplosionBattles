package main.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.player.PlayerEB;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		PlayerEB playerEB = Game.getInstance().getPlayer(p);
		
		if(e.getHand()==null) {
			return;
		}
		
		if(e.getHand()==EquipmentSlot.OFF_HAND) {
			return;
		}
		if(e.getClickedBlock()!=null) {
			Material m = e.getClickedBlock().getType();
			if(m==Material.WOOD_BUTTON||m==Material.WOODEN_DOOR||m==Material.ACACIA_DOOR||m==Material.BIRCH_DOOR||
				m==Material.DARK_OAK_DOOR||m==Material.JUNGLE_DOOR||m==Material.SPRUCE_DOOR||m==Material.TRAP_DOOR||m==Material.STONE_BUTTON) {
				return;
			}
		}
		
		ItemStack is = p.getInventory().getItemInMainHand();
		
		boolean c = playerEB.getInventoryLayout().onInteract(is);
		if(c) {
			e.setCancelled(true);
			return;
		}

		boolean canceled = playerEB.getWeapon().onInteract(e);
		if(!canceled) {
			return;
		}

		playerEB.getKit().onInteract(is);
		playerEB.getConsumablesManager().onInteract(is);
		
		e.setCancelled(true);
	}
}
