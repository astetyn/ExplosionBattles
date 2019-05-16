package main.kits.actions;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.MsgCenter;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MedKit {

	private int ticks = 0;
	private final int itemCooldownTicks = 1200;
	private PlayerEB playerEB;
	private ItemStack item;
	
	public MedKit(PlayerEB playerEB, int inventorySlot) {
		this.playerEB = playerEB;
		item = new ItemStack(Material.ENDER_CHEST,1);
		ItemMeta im2 = item.getItemMeta();
		im2.setDisplayName("Med Kit");
		item.setItemMeta(im2);
		playerEB.getPlayer().getInventory().setItem(inventorySlot, item);
		playerEB.getPlayer().updateInventory();
	}
	
	public void wantsToUseMedKit() {
		playerEB.getPlayer().setHealth(20);
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Život doplnený.");
		playerEB.getPlayer().getInventory().removeItem(item);
	}
	
	public void tick() {
		if(ticks>=itemCooldownTicks) {
			playerEB.getPlayer().getInventory().addItem(item);
			ticks = 0;
		}
		ticks++;
	}

}
