package main.player.layouts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import main.Game;
import main.maps.voting.MapVoteInventory;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class LobbyInventoryLayout extends InventoryLayout {

	private PlayerEB playerEB;
	private List<InventoryLayoutItem> layoutItems;
	
	public LobbyInventoryLayout(PlayerEB playerEB) {
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
		al.add(ChatColor.GOLD + "Klikni pre hlasovanie mapy.");
		ItemStack is = new ItemStack(Material.PAPER,1);
		ItemMeta im3 = is.getItemMeta();
		im3.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+"Hlasovanie mapy");
		im3.setLore(al);
		is.setItemMeta(im3);
		layoutItems.add(new InventoryLayoutItem(is,0,ItemInteractAction.OPEN_MAP_CHOOSER));
		
		ArrayList <String> al2 = new ArrayList <String> ();
		al2.add(ChatColor.GOLD + "Sklad kitov a zbraní.");
		ItemStack is2 = new ItemStack(Material.CHEST,1);
		ItemMeta im5 = is2.getItemMeta();
		im5.setDisplayName(ChatColor.YELLOW+""+ChatColor.BOLD+"Armoury");
		im5.setLore(al2);
		is2.setItemMeta(im5);
		layoutItems.add(new InventoryLayoutItem(is2,1,ItemInteractAction.OPEN_SHOP));
		
		ArrayList <String> al3 = new ArrayList <String> ();
		al3.add(ChatColor.GOLD + "Klikni pre odpojenie z hry.");
		ItemStack is3 = new ItemStack(Material.CLOCK,1);
		ItemMeta im2 = is3.getItemMeta();
		im2.setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"Odpojenie z hry");
		im2.setLore(al3);
		is3.setItemMeta(im2);
		layoutItems.add(new InventoryLayoutItem(is3,8,ItemInteractAction.LEAVE));
		
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
				if(iia == ItemInteractAction.OPEN_MAP_CHOOSER) {
					new MapVoteInventory(playerEB);
					return true;
				}else if(iia == ItemInteractAction.OPEN_SHOP) {
					playerEB.getShop().openInventory();
					return true;
				}else if(iia == ItemInteractAction.LEAVE) {
					Game.getInstance().getStage().onLeave(playerEB);
					return true;
				}
			}
		}
		return false;
	}
	
}
