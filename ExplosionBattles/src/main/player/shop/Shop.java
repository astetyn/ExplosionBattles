package main.player.shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import main.MsgCenter;
import main.consumables.Consumable;
import main.kits.Kit;
import main.kits.KitsManager;
import main.player.PlayerEB;
import main.player.UserAccount;
import main.weapons.Weapon;
import main.weapons.WeaponsManager;

public class Shop {

	private PlayerEB playerEB;
	private List<Buyable> shopItemsKits = new ArrayList<Buyable>();
	private List<Buyable> shopItemsWeapons = new ArrayList<Buyable>();
	private List<Buyable> shopItemsConsumables = new ArrayList<Buyable>();
	private List<Buyable> shopItemsAll = new ArrayList<Buyable>();
	private ShopInventory shopInventory;
	private BuyConfirmationInventory buyConfirmationInventory;
	private boolean waitingForConfirmation = false;
	
	public Shop(PlayerEB playerEB) {
		this.playerEB = playerEB;
		addItemsToList();
		shopInventory = new ShopInventory(playerEB, shopItemsKits, shopItemsWeapons, shopItemsConsumables);
	}
	
	public void onClick(Buyable clickedShopItem) {
		if(clickedShopItem.getPrice()==-1) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Táto zbraň sa nedá nastaviť. Dá sa získať iba zo supply package.");
			return;
		}
		
		if(clickedShopItem instanceof Consumable) {
			wantsToBuyConsumable(clickedShopItem);
			return;
		}
		
		boolean boughtAlready = playerEB.getUserAccount().hasBoughtItem(clickedShopItem.getIndex());
		
		if(boughtAlready) {
			equip(clickedShopItem);
		}else {
			wantsToBuy(clickedShopItem);
		}
	}
	
	private void wantsToBuyConsumable(Buyable shopItem) {
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
		playerEB.getStatusBoard().setTime(-1);
	}
	
	private void equip(Buyable shopItem) {
		if(shopItem instanceof Kit) {
			Kit kit = new KitsManager().createNewKit((Kit) shopItem, playerEB);
			playerEB.setKit(kit);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Kit "+ChatColor.GOLD+ChatColor.BOLD+shopItem.getItem().getItemMeta().getDisplayName()+ChatColor.GRAY+" úspešne nastavený.");
		}else if (shopItem instanceof Weapon) {
			Weapon weapon = WeaponsManager.createWeapon((Weapon) shopItem, playerEB);
			playerEB.setWeapon(weapon);
			playerEB.setChosenWeapon(true);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Zbraň "+ChatColor.GOLD+ChatColor.BOLD+shopItem.getItem().getItemMeta().getDisplayName()+ChatColor.GRAY+" úspešne nastavená.");
		}
		playerEB.getPlayer().getOpenInventory().close();
	}
	
	private void wantsToBuy(Buyable shopItem) {
		
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
			Buyable shopItem = buyConfirmationInventory.getShopItem();
			UserAccount ua = playerEB.getUserAccount();
			int price = shopItem.getPrice();
			int playersCoins = ua.getCoins();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Prebieha transakcia...");
			int difference = playersCoins - price;
			ua.addItem(shopItem.getIndex());
			ua.setCoins(difference);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Transakcia prebehla úspešne, máš dostupnú novú vec!");
			shopInventory.reloadItems();
			playerEB.getStatusBoard().setTime(-1);
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
		for(Kit kit : new KitsManager().getKits()) {
			Buyable si = kit;
			shopItemsKits.add(si);
			shopItemsAll.add(si);
		}
		for(Weapon weapon : new WeaponsManager().getWeapons()) {
			Buyable si = weapon;
			shopItemsWeapons.add(si);
			shopItemsAll.add(si);
		}
		playerEB.getConsumablesManager();
		for(Consumable consumable : playerEB.getConsumablesManager().getConsumablesList()) {
			Buyable si = consumable;
			shopItemsConsumables.add(si);
			shopItemsAll.add(si);
		}
	}

	public List<Buyable> getShopItemsAll() {
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
