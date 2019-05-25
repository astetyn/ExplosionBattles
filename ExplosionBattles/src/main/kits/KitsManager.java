package main.kits;

import java.util.ArrayList;
import java.util.List;

import main.kits.data.ArchitectData;
import main.kits.data.BasicData;
import main.kits.data.CamperData;
import main.kits.data.EngineerData;
import main.kits.data.KitData;
import main.player.PlayerEB;

public class KitsManager {

	private List<KitData> kitsData = new ArrayList<KitData>();
	
	public KitsManager() {
		kitsData.add(new BasicData());
		kitsData.add(new CamperData());
		kitsData.add(new ArchitectData());
		kitsData.add(new EngineerData());
	}
	
	public List<KitData> getKitsData() {
		return kitsData;
	}
	
	public Kit createNewKit(KitData kitData, PlayerEB playerEB) {
		if(kitData instanceof BasicData) {
			return new Basic(playerEB);
		}else if(kitData instanceof ArchitectData) {
			return new Architect(playerEB);
		}else if(kitData instanceof CamperData) {
			return new Camper(playerEB);
		}else if(kitData instanceof EngineerData) {
			return new Engineer(playerEB);
		}
		return null;
	}

}
