package main.player;

import org.bukkit.entity.Player;

import main.kits.BasicKit;
import main.kits.Kit;
import main.weapons.Weapon;

public class PlayerEB {

	private Player player;
	private boolean inRunningGame = false;
	private Kit kit = new BasicKit(this);
	private StatusBoard statusBoard;
	private Weapon weapon;
	private BankAccount bankAccount;
	private boolean vip;
	
	public PlayerEB(Player player) {
		this.player = player;
		this.statusBoard = new StatusBoard(this);
		this.bankAccount = new BankAccount(this);
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

	public boolean isInRunningGame() {
		return inRunningGame;
	}

	public void setInRunningGame(boolean inRunningGame) {
		this.inRunningGame = inRunningGame;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}
	
}
