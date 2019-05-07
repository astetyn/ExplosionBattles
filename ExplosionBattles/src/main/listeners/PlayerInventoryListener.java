package main.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.inventory.Shop;
import main.inventory.ShopItem;
import main.kits.Kit;
import main.kits.KitChooser;
import main.player.PlayerEB;
import main.weapons.Weapon;
import main.weapons.WeaponChooser;

public class PlayerInventoryListener implements Listener {

	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(!Game.getInstance().isPlayerInGame(p)) {
			return;
		}
		PlayerEB playerEB = Game.getInstance().getPlayer(p);
		if(playerEB.isInRunningGame()) {
			e.setCancelled(true);
			return;
		}
		
		ItemStack is = e.getCurrentItem();
		
		if(is==null) {
			return;
		}else if(is.getType()==Material.AIR) {
			return;
		}
		
		for(Kit kit : KitChooser.getKits()) {

			if(is.equals(kit.getItem())){
				if(!playerEB.getBankAccount().hadBoughtItem(kit.getIndex())) {
					p.sendMessage("Tento kit nemas zakupeny.");
					break;
				}
				Kit copyKit = (Kit) kit.clone();
				playerEB.setKit(copyKit);
				playerEB.getKit().setPlayer(playerEB);
				playerEB.getPlayer().sendMessage("Kit "+kit.getItem().getItemMeta().getDisplayName()+ChatColor.RESET+" uspesne nastaveny.");
				break;
			}
		}
		
		for(ItemStack weaponItem : WeaponChooser.getItemsWeapons().keySet()) {

			if(is.equals(weaponItem)){
				Weapon weapon= WeaponChooser.getItemsWeapons().get(weaponItem);
				if(weapon.getPrice()==-1) {
					playerEB.getPlayer().sendMessage("Tato zbran sa neda nastavit. Da sa ziskat iba z air dropu.");
					break;
				}
				if(!playerEB.getBankAccount().hadBoughtItem(weapon.getIndex())) {
					p.sendMessage("Tuto zbran nemas zakupenu.");
					break;
				}
				
				Weapon copyWeapon = (Weapon) weapon.clone();
				playerEB.setWeapon(copyWeapon);
				playerEB.getWeapon().setPlayerEB(playerEB);
				playerEB.getWeapon().setSetThisRound(true);
				playerEB.getPlayer().sendMessage("Zbran "+weaponItem.getItemMeta().getDisplayName()+ChatColor.RESET+" uspesne nastavena.");
				break;
			}
		}
		
		for(ShopItem shopItem : Shop.getShopItemsKits()) {
			if(is.equals(shopItem.getIs())) {
				playerEB.getBankAccount().wantsToBuy(shopItem);
			}
		}
		
		for(ShopItem shopItem : Shop.getShopItemsWeapons()) {
			if(is.equals(shopItem.getIs())) {
				playerEB.getBankAccount().wantsToBuy(shopItem);
			}
		}
		
		String clickedName = is.getItemMeta().getDisplayName();
		if(clickedName!=null) {
			clickedName = ChatColor.stripColor(clickedName);
			for(int i=0;i < Game.getInstance().getMapChooser().getMapNames().size();i++) {
				if(clickedName.startsWith(i+"")) {
					Game.getInstance().getMapChooser().playerVoting(Game.getInstance().getPlayer(p),i);
					break;
				}
			}
		}
		e.setCancelled(true);
	}
	
}
