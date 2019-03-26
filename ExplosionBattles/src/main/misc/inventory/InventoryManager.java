package main.misc.inventory;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.Game;
import main.PlayerEB;
import main.STATE;

public class InventoryManager {

	private static InventoryManager inventorySaver = new InventoryManager();
	
	private HashMap<PlayerEB,ItemStack[]> savedInventories = new HashMap<PlayerEB,ItemStack[]>();
	
	public void giveItemsByState(PlayerEB playerEB) {
		STATE state = Game.getInstance().getStateManager().getState();
		switch(state) {
		case LOBBY_WAITING: case LOBBY_LAUNCHING:{
			new LobbyInventory(playerEB);
			break;
		}
		case GAME_RUNNING: case ENDING:{
			new SpectatorInventory(playerEB);
			break;
		}
		}
	}
	
	public void giveItemsByStateAll() {
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			STATE state = Game.getInstance().getStateManager().getState();
			switch(state) {
			case LOBBY_WAITING: case LOBBY_LAUNCHING:{
				new LobbyInventory(playerEB);
				break;
			}
			case GAME_RUNNING: case ENDING:{
				new SpectatorInventory(playerEB);
				break;
			}
			}
		}
	}
	
	public void clearInventoryAll() {
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.getPlayer().getInventory().clear();
		}
	}
	
	public void saveAndClearInventory(PlayerEB playerEB) {
		saveInventory(playerEB);
		playerEB.getPlayer().getInventory().clear();
		playerEB.getPlayer().updateInventory();
	}
	
	public void saveInventory(PlayerEB playerEB) {
		savedInventories.put(playerEB, copyInventory(playerEB.getPlayer().getInventory()));
	}
	
	public void loadInventory(PlayerEB playerEB) {
		playerEB.getPlayer().getInventory().clear();
		playerEB.getPlayer().getInventory().setContents(savedInventories.get(playerEB));
		playerEB.getPlayer().updateInventory();
		savedInventories.remove(playerEB);
	}
	
	private ItemStack[] copyInventory(Inventory inv) {
	    ItemStack[] original = inv.getContents();
	    ItemStack[] copy = new ItemStack[original.length];
	    for(int i = 0; i < original.length; i++) {
	        if(original[i] != null) {
	            copy[i] = new ItemStack(original[i]);
	        }
	    }
	    return copy;
	}
	
	public static InventoryManager getInstance() {
		return inventorySaver;
	}
	
}
