package main.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import main.kits.actions.Bush;
import main.kits.actions.MedKit;
import main.kits.actions.Smoke;
import main.kits.data.CamperData;
import main.kits.data.KitData;
import main.player.PlayerEB;

public class Camper extends Kit {

	private Smoke smoke;
	private MedKit medkit;
	private Bush bush;
	private KitData kitData = new CamperData();
	
	public Camper(PlayerEB playerEB) {
		setPlayer(playerEB);
	}

	@Override
	public void startInit() {
		bush = new Bush(getPlayerEB(),1);
		smoke = new Smoke(getPlayerEB(),2);
		medkit = new MedKit(getPlayerEB(),3);
		
	}

	@Override
	public void onInteract(ItemStack is) {
		if(is.getType() == Material.INK_SACK) {
			smoke.wantsToUseSmoke(getPlayerEB().getPlayer().getLocation());
		}
		if(is.getType() == Material.ENDER_CHEST) {
			medkit.wantsToUseMedKit();
		}
		if(is.getType() == Material.LEAVES) {
			bush.wantsToUseBush();
		}
	}
	
	@Override
	public void tick() {
		smoke.tick();
		medkit.tick();
	}

	@Override
	public KitData getKitData() {
		return kitData;
	}

}
