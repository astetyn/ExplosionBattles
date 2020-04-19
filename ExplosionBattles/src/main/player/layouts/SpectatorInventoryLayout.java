package main.player.layouts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import main.Game;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class SpectatorInventoryLayout extends InventoryLayout {

	private PlayerEB playerEB;
	private List<InventoryLayoutItem> layoutItems;
	
	public SpectatorInventoryLayout(PlayerEB playerEB) {
		this.playerEB = playerEB;
		playerEB.getPlayer().getInventory().clear();
		playerEB.getPlayer().getInventory().setHeldItemSlot(0);
		initItems();
		putItems();
	}

	@Override
	public void initItems() {
		layoutItems = new ArrayList<InventoryLayoutItem>();
		
		ArrayList <String> al = new ArrayList <String> ();
		al.add(ChatColor.GOLD + "Klikni pre odpojenie z hry.");
		ItemStack is = new ItemStack(Material.CLOCK,1);
		ItemMeta im2 = is.getItemMeta();
		im2.setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"Odpojenie z hry");
		im2.setLore(al);
		is.setItemMeta(im2);
		layoutItems.add(new InventoryLayoutItem(is,8,ItemInteractAction.LEAVE));
		
	}

	@Override
	public void putItems() {
		PlayerInventory pi = playerEB.getPlayer().getInventory();
		for(InventoryLayoutItem ili : layoutItems) {
			pi.setItem(ili.getPosition(), ili.getIs());
		}
	}

	@Override
	public boolean onInteract(ItemStack itemStack) {
		for(InventoryLayoutItem ili : layoutItems) {
			if(itemStack.equals(ili.getIs())) {
				ItemInteractAction iia = ili.getAction();
				if(iia == ItemInteractAction.LEAVE) {
					Game.getInstance().getStage().onLeave(playerEB);
					return true;
				}
			}
		}
		return false;
	}
	
}
