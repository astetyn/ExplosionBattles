package main.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import main.kits.actions.BombAssault;
import main.kits.actions.Fortress;
import main.kits.data.EngineerData;
import main.kits.data.KitData;
import main.player.PlayerEB;

public class Engineer extends Kit {
	
	private BombAssault bombAssault;
	private Fortress fortress;
	private KitData kitData = new EngineerData();
	
	public Engineer(PlayerEB playerEB) {
		setPlayer(playerEB);
	}

	@Override
	public void startInit() {
		fortress = new Fortress(getPlayerEB(), 1);
		bombAssault = new BombAssault(getPlayerEB(),2);
	}
	
	@Override
	public void onInteract(ItemStack is) {
		if(is.getType()==Material.ANVIL) {
			fortress.wantsToBuildFortress();
		}else if(is.getType()==Material.NETHER_STAR) {
			bombAssault.wantsToCallAssault();
		}
	}
	
	@Override
	public void tick() {
		if(bombAssault!=null) {
			bombAssault.tick();
		}
	}

	@Override
	public KitData getKitData() {
		return kitData;
	}
}
