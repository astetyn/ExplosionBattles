package main.player.shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import main.MsgCenter;
import main.kits.Kit;
import main.kits.KitsManager;
import main.kits.data.KitData;
import main.player.PlayerEB;
import main.player.UserAccount;
import main.player.consumables.Consumable;
import main.weapons.Weapon;
import main.weapons.WeaponsManager;
import main.weapons.data.WeaponData;

public class Shop {

	private PlayerEB playerEB;
	private List<ShopItem> shopItemsKits = new ArrayList<ShopItem>();
	private List<ShopItem> shopItemsWeapons = new ArrayList<ShopItem>();
	private List<ShopItem> shopItemsConsumables = new ArrayList<ShopItem>();
	private List<ShopItem> shopItemsLimited = new ArrayList<ShopItem>();
	private List<ShopItem> shopItemsAll = new ArrayList<ShopItem>();
	private ShopInventory shopInventory;
	private BuyConfirmationInventory buyConfirmationInventory;
	private boolean waitingForConfirmation = false;
	
	public Shop(PlayerEB playerEB) {
		this.playerEB = playerEB;
		addItemsToList();
		shopInventory = new ShopInventory(playerEB, shopItemsKits, shopItemsWeapons, shopItemsConsumables, shopItemsLimited);
	}
	
	public void onClick(ShopItem clickedShopItem) {
		if(clickedShopItem.getPrice()==-1) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Táto zbraň sa nedá nastaviť. Dá sa získať iba zo supply package.");
			return;
		}
		
		if(clickedShopItem instanceof Consumable) {
			wantsToBuyConsumable(clickedShopItem);
			return;
		}
		
		boolean boughtAlready = playerEB.getUserAccount().hasBoughtItem(clickedShopItem.getIndex());
		if(playerEB.isVip()&&clickedShopItem.isAvaibleForVip()) {
			boughtAlready = true;
		}
		
		if(boughtAlready) {
			equip(clickedShopItem);
		}else {
			wantsToBuy(clickedShopItem);
		}
	}
	
	private void wantsToBuyConsumable(ShopItem shopItem) {
		UserAccount ua = playerEB.getUserAccount();
		int price = shopItem.getPrice();
		int playersCoins = ua.getCoins();
		if(playersCoins<price) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Nemáš dosť peňazí.");
			return;
		}
		if(playerEB.getConsumablesManager().hasMaxItems(shopItem)) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Už máš maximálny počet týchto predmetov.");
			return;
		}
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Prebieha transakcia...");
		int difference = playersCoins - price;
		ua.setCoins(difference);
		playerEB.getConsumablesManager().addBoughtItem(shopItem,1);
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Kúpil si si novú vec do ďalšej hry!");
		shopInventory.reloadItems();
		playerEB.getStatusBoard().tick(-1);
	}
	
	private void equip(ShopItem shopItem) {
		if(shopItem instanceof KitData) {
			Kit kit = new KitsManager().createNewKit((KitData) shopItem, playerEB);
			playerEB.setKit(kit);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Kit "+ChatColor.GOLD+ChatColor.BOLD+shopItem.getItem().getItemMeta().getDisplayName()+ChatColor.GRAY+" úspešne nastavený.");
		}else if (shopItem instanceof WeaponData) {
			Weapon weapon = new WeaponsManager().createNewWeapon((WeaponData) shopItem, playerEB);		
			playerEB.setWeapon(weapon);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Zbraň "+ChatColor.GOLD+ChatColor.BOLD+shopItem.getItem().getItemMeta().getDisplayName()+ChatColor.GRAY+" úspešne nastavená.");
		}
		playerEB.getPlayer().getOpenInventory().close();
	}
	
	private void wantsToBuy(ShopItem shopItem) {
		
		UserAccount ua = playerEB.getUserAccount();
		int price = shopItem.getPrice();
		int playersCoins = ua.getCoins();
		if(playersCoins<price) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Nemáš dosť peňazí.");
			return;
		}
		buyConfirmationInventory = new BuyConfirmationInventory(playerEB, shopItem);
		waitingForConfirmation = true;
	}
	
	public void continueWithBuy(int i) {
		waitingForConfirmation = false;
		if(i==0) {
			ShopItem shopItem = buyConfirmationInventory.getShopItem();
			UserAccount ua = playerEB.getUserAccount();
			int price = shopItem.getPrice();
			int playersCoins = ua.getCoins();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Prebieha transakcia...");
			int difference = playersCoins - price;
			ua.addItem(shopItem.getIndex());
			ua.setCoins(difference);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Transakcia prebehla úspešne, máš dostupnú novú vec!");
			shopInventory.reloadItems();
			playerEB.getStatusBoard().tick(-1);
			equip(shopItem);
		}else {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Platba zrušená.");
			playerEB.getPlayer().getOpenInventory().close();
		}
	}
	
	public void openInventory() {
		shopInventory.openInventory();
	}
	
	private void addItemsToList() {
		for(KitData kitData : new KitsManager().getKitsData()) {
			ShopItem si = kitData;
			if(si.isLimited()) {
				shopItemsLimited.add(si);
			}else {
				shopItemsKits.add(si);
			}
			shopItemsAll.add(si);
		}
		for(WeaponData weaponData : new WeaponsManager().getWeaponsData()) {
			ShopItem si = weaponData;
			if(si.isLimited()) {
				shopItemsLimited.add(si);
			}else {
				shopItemsWeapons.add(si);
			}
			shopItemsAll.add(si);
		}
		playerEB.getConsumablesManager();
		for(Consumable consumable : playerEB.getConsumablesManager().getConsumablesList()) {
			ShopItem si = consumable;
			if(si.isLimited()) {
				shopItemsLimited.add(si);
			}else {
				shopItemsConsumables.add(si);
			}
			shopItemsAll.add(si);
		}
	}

	public List<ShopItem> getShopItemsAll() {
		return shopItemsAll;
	}

	public BuyConfirmationInventory getBuyConfirmationInventory() {
		return buyConfirmationInventory;
	}

	public boolean isWaitingForConfirmation() {
		return waitingForConfirmation;
	}

	public void setWaitingForConfirmation(boolean waitingForConfirmation) {
		this.waitingForConfirmation = waitingForConfirmation;
	}

}
