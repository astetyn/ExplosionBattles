package main.weapons;

import java.util.ArrayList;
import java.util.List;

import main.player.PlayerEB;

public class WeaponsManager {
	
	private List<Weapon> weapons= new ArrayList<Weapon>();
	
	public WeaponsManager() {
		weapons.add(new AssaultShooter());
		weapons.add(new MiniGun());
		weapons.add(new LightSniper());
		weapons.add(new SnapdragonGun());
		weapons.add(new HeavyExplosiveSniper());
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}
	
	/**	Use when you want to create new weapon based on the previous weapon.
	 * Method will check type of the previous gun and create the new with the same type.
	 * 
	 * @param weapon can be created without player
	 * @param playerEB will be hooked to the gun
	 * @return New weapon with player with completely new data.
	 */
	public static Weapon createWeapon(Weapon weapon, PlayerEB playerEB) {
		if(weapon instanceof AssaultShooter) {
			return new AssaultShooter(playerEB);
		}else if(weapon instanceof LightSniper) {
			return new LightSniper(playerEB);
		}else if(weapon instanceof MiniGun) {
			return new MiniGun(playerEB);
		}else if(weapon instanceof SnapdragonGun) {
			return new SnapdragonGun(playerEB);
		}
		return null;
	}

}
