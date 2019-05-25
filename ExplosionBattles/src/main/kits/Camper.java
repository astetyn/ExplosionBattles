package main.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import main.kits.actions.Bush;
import main.kits.data.CamperData;
import main.kits.data.KitData;
import main.player.PlayerEB;
import main.player.consumables.MedKit;
import main.player.consumables.Smoke;

public class Camper extends Kit {

	private Bush bush;
	private KitData kitData = new CamperData();
	
	public Camper(PlayerEB playerEB) {
		setPlayer(playerEB);
	}

	@Override
	public void startInit() {
		bush = new Bush(getPlayerEB(),1);
		getPlayerEB().getConsumablesManager().addBoughtItem(new Smoke(getPlayerEB()),3);
		getPlayerEB().getConsumablesManager().addBoughtItem(new MedKit(getPlayerEB()),3);
	}

	@Override
	public void onInteract(ItemStack is) {
		if(is.getType() == Material.LEAVES) {
			bush.wantsToUseBush();
		}
	}

	@Override
	public KitData getKitData() {
		return kitData;
	}

}
