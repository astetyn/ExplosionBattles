package main.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MapChooser {

	private HashMap<String,Integer> voteMap = new HashMap<String,Integer>();
	private HashMap<PlayerEB,String> playerMap = new HashMap<PlayerEB,String>();
	private String winMap = "";
	private int maxVotes = 0;
	private List<String> mapNames;
	
	public MapChooser() {
		this.mapNames = getMapNames();
	}
	
	public String getWinMap() {
		if(winMap.isEmpty()) {
			if(mapNames.size()==0) {
				return "";
			}else {
				return mapNames.get(0);
			}
		}
		return winMap;
	}
	
	public void playerVoting(PlayerEB playerEB,int i) {
		this.mapNames = getMapNames();
		String mapName = mapNames.get(i);
		
		if(playerMap.containsKey(playerEB)) {
			
			int numberOfVotes = voteMap.get(playerMap.get(playerEB));
			numberOfVotes--;
			voteMap.put(mapName,numberOfVotes);
			
		}
		int numberOfVotes = voteMap.get(mapName);
		numberOfVotes++;
		if(numberOfVotes>maxVotes) {
			maxVotes = numberOfVotes;
			winMap = mapName;
		}
		voteMap.put(mapName,numberOfVotes);
		playerMap.put(playerEB, mapName);
		
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Úspešne si hlasoval za mapu: "+ChatColor.DARK_GREEN+ChatColor.BOLD+mapName);
	}
	
	public void notifyAllPlayers() {
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			String msg = "";
			msg+=ChatColor.YELLOW+""+ChatColor.BOLD+"-- Mapy na výber --"+"\n";
			for(String mapName : mapNames) {	
				msg+=ChatColor.GRAY+">> "+ChatColor.DARK_GREEN+mapName+"\n";
			}
			msg+=ChatColor.YELLOW+""+ChatColor.BOLD+"-- Mapy na výber --";
			playerEB.getPlayer().sendMessage(msg);
		}
	}
	
	public List<String> getMapNames() {

		List<String> mapNames = new ArrayList<String>();
		File mapFolder = new File(Main.getPlugin().getDataFolder().getPath()+"/maps");
		
		for(File f : mapFolder.listFiles()) {
			String name = f.getName();
			name = name.replaceAll(".yml", "");
			if(name.equals("config")||name.equals("lobby")) {
				continue;
			}
			voteMap.put(name, 0);
			mapNames.add(name);
		}
		return mapNames;
	}

}
