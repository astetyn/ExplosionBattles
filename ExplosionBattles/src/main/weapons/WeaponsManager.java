package main.weapons;

import java.util.ArrayList;
import java.util.List;

import main.player.PlayerEB;
import main.weapons.data.AssaultShooterData;
import main.weapons.data.HeavyExplosiveSniperData;
import main.weapons.data.LightSniperData;
import main.weapons.data.MiniGunData;
import main.weapons.data.WeaponData;

public class WeaponsManager {
	
	private List<WeaponData> weaponsData = new ArrayList<WeaponData>();
	
	public WeaponsManager() {
		weaponsData.add(new AssaultShooterData());
		weaponsData.add(new LightSniperData());
		weaponsData.add(new MiniGunData());
		weaponsData.add(new HeavyExplosiveSniperData());
	}

	public List<WeaponData> getWeaponsData() {
		return weaponsData;
	}
	
	public Weapon createNewWeapon(WeaponData weaponData, PlayerEB playerEB) {
		if(weaponData instanceof AssaultShooterData) {
			return new AssaultShooter(playerEB);
		}else if(weaponData instanceof LightSniperData) {
			return new LightSniper(playerEB);
		}else if(weaponData instanceof MiniGunData) {
			return new MiniGun(playerEB);
		}
		return null;
	}

}
