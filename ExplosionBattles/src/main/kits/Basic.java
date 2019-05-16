package main.kits;

import main.kits.data.BasicData;
import main.kits.data.KitData;
import main.player.PlayerEB;

public class Basic extends Kit {
	
	private KitData kitData = new BasicData();
	
	public Basic(PlayerEB playerEB) {
		setPlayer(playerEB);
	}

	@Override
	public KitData getKitData() {
		return kitData;
	}
	
}
