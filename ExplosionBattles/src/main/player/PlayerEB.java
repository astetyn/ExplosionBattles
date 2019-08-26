package main.player;

import org.bukkit.entity.Player;

import main.consumables.ConsumablesManager;
import main.kits.BasicKit;
import main.kits.Kit;
import main.player.layouts.InventoryLayout;
import main.player.layouts.LobbyInventoryLayout;
import main.player.shop.Shop;
import main.weapons.AssaultShooter;
import main.weapons.Weapon;

public class PlayerEB {

	private Player player;
	private GameStage gameStage;
	private Kit kit;
	private Weapon weapon;
	private boolean chosenWeapon;
	private StatusBoard statusBoard;
	private UserAccount userAccount;
	private Shop shop;
	private PlayerDataSaver playerDataSaver;
	private InventoryLayout inventoryLayout;
	private ConsumablesManager consumablesManager;
	private boolean vip;
	
	public PlayerEB(Player player) {
		this.player = player;
		this.consumablesManager = new ConsumablesManager(this);
		this.gameStage = GameStage.LOBBY_WAITING;
		this.kit = new BasicKit(this);
		this.weapon = new AssaultShooter(this);
		this.chosenWeapon = false;
		this.statusBoard = new StatusBoard(this);
		this.userAccount = new UserAccount(this);
		this.shop = new Shop(this);
		this.playerDataSaver = new PlayerDataSaver(this);
		this.inventoryLayout = new LobbyInventoryLayout(this);
		this.vip = player.hasPermission("explosionbattles.vip");
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}
	
	public Kit getKit() {
		return kit;
	}

	public StatusBoard getStatusBoard() {
		return statusBoard;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public boolean isVip() {
		return vip;
	}

	public PlayerDataSaver getPlayerDataSaver() {
		return playerDataSaver;
	}

	public GameStage getGameStage() {
		return gameStage;
	}

	public void setGameStage(GameStage gameStage) {
		this.gameStage = gameStage;
	}

	public Shop getShop() {
		return shop;
	}

	public InventoryLayout getInventoryLayout() {
		return inventoryLayout;
	}

	public void setInventoryLayout(InventoryLayout inventoryLayout) {
		this.inventoryLayout = inventoryLayout;
	}

	public ConsumablesManager getConsumablesManager() {
		return consumablesManager;
	}

	public boolean isChosenWeapon() {
		return chosenWeapon;
	}

	public void setChosenWeapon(boolean chosenWeapon) {
		this.chosenWeapon = chosenWeapon;
	}
	
}
