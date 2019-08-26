package main.kits;

import java.util.ArrayList;
import java.util.List;

import main.player.PlayerEB;

public class KitsManager {

	private List<Kit> kits = new ArrayList<Kit>();
	
	public KitsManager() {
		kits.add(new BasicKit());
		kits.add(new CamperKit());
		kits.add(new ArchitectKit());
		kits.add(new EngineerKit());
	}
	
	public List<Kit> getKits() {
		return kits;
	}
	
	public Kit createNewKit(Kit kit, PlayerEB playerEB) {
		if(kit instanceof BasicKit) {
			return new BasicKit(playerEB);
		}else if(kit instanceof ArchitectKit) {
			return new ArchitectKit(playerEB);
		}else if(kit instanceof CamperKit) {
			return new CamperKit(playerEB);
		}else if(kit instanceof EngineerKit) {
			return new EngineerKit(playerEB);
		}
		return null;
	}

}
