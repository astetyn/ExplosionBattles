package main.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.kits.Kit;
import main.kits.KitChooser;
import main.player.PlayerEB;
import main.weapons.Weapon;
import main.weapons.WeaponChooser;
import net.md_5.bungee.api.ChatColor;

public class Shop {

	private PlayerEB playerEB;
	private static List<ShopItem> shopItemsKits = new ArrayList<ShopItem>();
	private static List<ShopItem> shopItemsWeapons = new ArrayList<ShopItem>();
	private Inventory shopInventory;
	
	//Hrac tukol v lobby inventore na shop, teraz je v shope
	public Shop(PlayerEB playerEB) {
		shopItemsKits.clear();
		shopItemsWeapons.clear();
		this.playerEB = playerEB;
		addItemsToList();
		this.shopInventory = Bukkit.createInventory(null, 36,ChatColor.GOLD+""+ChatColor.BOLD+"-= SHOP =-");
		addItemsToInv();
		showInventory();
	}
	
	private void addItemsToInv() {
		for(int j=0;j<9;j++) {
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 5);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("Kits");
			is.setItemMeta(im);
			shopInventory.setItem(j, is);
		}
		int i = 9;
		for(ShopItem si : shopItemsKits) {
			if(si.getPrice()==-1) {
				continue;
			}
			ItemStack is = si.getIs();
			ItemMeta im = is.getItemMeta();
			List<String> lore = im.getLore();
			lore.add("Cena: "+si.getPrice());
			lore.add("Zadarmo pre VIP: "+si.isAvaibleForVip());
			if(playerEB.getBankAccount().hadBoughtItem(si.getIndex())) {
				lore.add("UZ VLASTNIS");
			}
			im.setLore(lore);
			is.setItemMeta(im);
			shopInventory.setItem(i, is);
			i++;
		}
		for(int j=18;j<27;j++) {
			ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 5);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("Weapons");
			is.setItemMeta(im);
			shopInventory.setItem(j, is);
		}
		i = 27;
		for(ShopItem si : shopItemsWeapons) {
			if(si.getPrice()==-1) {
				continue;
			}
			ItemStack is = si.getIs();
			ItemMeta im = is.getItemMeta();
			List<String> lore = im.getLore();
			lore.add("Cena: "+si.getPrice());
			lore.add("Zadarmo pre VIP: "+si.isAvaibleForVip());
			if(playerEB.getBankAccount().hadBoughtItem(si.getIndex())) {
				lore.add("UZ VLASTNIS");
			}
			im.setLore(lore);
			is.setItemMeta(im);
			shopInventory.setItem(i, is);
			i++;
		}
	}
	
	private void showInventory() {
		playerEB.getPlayer().openInventory(shopInventory);
	}
	
	private void addItemsToList() {
		for(Kit kit : KitChooser.getKits()) {
			shopItemsKits.add(new ShopItem(kit.getIndex(), kit.getItem(),kit.getPrice(),kit.isAvaibleForVip()));
		}
		for(Weapon weapon : WeaponChooser.getWeapons()) {
			shopItemsWeapons.add(new ShopItem(weapon.getIndex(), weapon.getItem(),weapon.getPrice(),weapon.isAvaibleForVip()));
		}
	}
	
	public static List<ShopItem> getShopItemsKits(){
		return shopItemsKits;
	}
	
	public static List<ShopItem> getShopItemsWeapons(){
		return shopItemsWeapons;
	}

}
