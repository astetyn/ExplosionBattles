package main.player.consumables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import main.player.PlayerEB;
import main.player.shop.ShopItem;

public class ConsumablesManager {

	private PlayerEB playerEB;
	private HashMap<Consumable,Integer> boughtConsumables = new HashMap<Consumable,Integer>();
	private List<Consumable> consumablesList = new ArrayList<Consumable>();
	private final int maxBought = 3;
	
	public ConsumablesManager(PlayerEB playerEB) {
		this.playerEB = playerEB;
		consumablesList.add(new MedKit(playerEB));
		consumablesList.add(new Smoke(playerEB));
	}
	
	public void onInteract(ItemStack is) {
		for(Consumable con : consumablesList) {
			if(is.isSimilar(con.getItem())) {
				con.onInteract();
			}
		}
	}
	
	public void addAllToInventory() {
		for(Consumable con : boughtConsumables.keySet()) {
			int amount = boughtConsumables.get(con);
			int freeSlot = playerEB.getPlayer().getInventory().firstEmpty();
			ItemStack is = new ItemStack(con.getItem());
			is.setAmount(amount);
			playerEB.getPlayer().getInventory().setItem(freeSlot, is);
		}
		boughtConsumables.clear();
	}

	public void addBoughtItem(ShopItem shopItem, int amount) {
		if(boughtConsumables.containsKey(shopItem)) {
			int actual = boughtConsumables.get(shopItem);
			boughtConsumables.put((Consumable) shopItem, actual+amount);
		}else {
			boughtConsumables.put((Consumable) shopItem, amount);
		}
	}
	
	public boolean hasMaxItems(ShopItem shopItem) {
		if(boughtConsumables.containsKey(shopItem)) {
			if(boughtConsumables.get(shopItem)>=maxBought) {
				return true;
			}
		}
		return false;
	}
	
	public int getBought(ShopItem shopItem) {
		if(boughtConsumables.containsKey(shopItem)) {
			return boughtConsumables.get(shopItem);
		}
		return 0;
	}

	public int getMaxBought() {
		return maxBought;
	}

	public List<Consumable> getConsumablesList() {
		return consumablesList;
	}

}
