package main.player;

import org.bukkit.entity.Player;

import main.kits.Basic;
import main.kits.Kit;
import main.player.shop.Shop;
import main.weapons.AssaultShooter;
import main.weapons.Weapon;

public class PlayerEB {

	private Player player;
	private GameStage gameStage;
	private Kit kit;
	private StatusBoard statusBoard;
	private Weapon weapon;
	private UserAccount userAccount;
	private Shop shop;
	private PlayerDataSaver playerDataSaver;
	private boolean vip;
	
	public PlayerEB(Player player) {
		this.player = player;
		this.gameStage = GameStage.LOBBY_WAITING;
		this.kit = new Basic(this);
		this.weapon = new AssaultShooter(this);
		this.statusBoard = new StatusBoard(this);
		this.userAccount = new UserAccount(this);
		this.shop = new Shop(this);
		this.playerDataSaver = new PlayerDataSaver(this);
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
	
}
