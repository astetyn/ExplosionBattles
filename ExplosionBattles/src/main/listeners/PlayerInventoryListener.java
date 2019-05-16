package main.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.layouts.MapVotingInventory;
import main.maps.MapVoteItem;
import main.player.PlayerEB;
import main.player.shop.Shop;
import main.player.shop.ShopItem;
import net.md_5.bungee.api.ChatColor;

public class PlayerInventoryListener implements Listener {

	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		
		PlayerEB playerEB = Game.getInstance().getPlayer(p);
		
		ItemStack is = e.getCurrentItem();
		
		if(is==null) {
			return;
		}else if(is.getType()==Material.AIR) {
			return;
		}

		Shop shop = playerEB.getShop();
		if(is.getItemMeta()!=null) {
			if(is.getItemMeta().getLore()!=null) {
				if(is.getItemMeta().getLore().size()>0) {
					String hidden = is.getItemMeta().getLore().get(0).replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "");
					for(ShopItem shopItem : shop.getShopItemsAll()) {
						if(hidden.equals(shopItem.getIndex())) {
							shop.onClick(shopItem);
						}
					}
				}
			}
		}

		if(shop.isWaitingForConfirmation()) {
			ItemStack[] chooseItems = shop.getBuyConfirmationInventory().getChooseItems();
			for(int i = 0; i < chooseItems.length;i++) {
				if(is.equals(chooseItems[i])) {
					shop.continueWithBuy(i);
				}
			}
		}
		
		for(MapVoteItem mvi : MapVotingInventory.getMapItems()) {
			if(is.equals(mvi.getIs())){
				Game.getInstance().getMapChooser().playerVoting(Game.getInstance().getPlayer(p),mvi.getIndex());
				break;
			}
		}
		e.setCancelled(true);
	}
}
