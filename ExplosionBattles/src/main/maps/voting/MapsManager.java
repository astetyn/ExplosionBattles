package main.maps.voting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.configuration.MapConfiguration;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class MapsManager {

	private HashMap<PlayerEB,MapVoteItem> playersVoted = new HashMap<PlayerEB,MapVoteItem>();
	private List<MapVoteItem> mapItems = new ArrayList<MapVoteItem>();
	
	public MapsManager() {
		reloadMaps();
	}
	
	public String getWinMap() {
		MapVoteItem maxMvi = null;
		for(MapVoteItem mvi : mapItems) {
			if(maxMvi==null) {
				maxMvi = mvi;
			}
			if(maxMvi.getVotes()<mvi.getVotes()) {
				maxMvi = mvi;
			}
		}
		return maxMvi.getMapName();
	}
	
	public void notifyAllPlayers() {
		for(PlayerEB playerEB : Game.getInstance().getPlayersInGame()) {
			String msg = "";
			msg+=ChatColor.YELLOW+""+ChatColor.BOLD+"-- Mapy na výber --"+"\n";
			for(MapVoteItem mvi : mapItems) {	
				msg+=ChatColor.GRAY+">> "+ChatColor.GREEN+mvi.getMapName()+ChatColor.DARK_GRAY+" ["+mvi.getBuilder()+"]\n";
			}
			msg+=ChatColor.YELLOW+""+ChatColor.BOLD+"-- Mapy na výber --";
			playerEB.getPlayer().sendMessage(msg);
		}
	}
	
	public void onClick(PlayerEB playerEB, ItemStack is) {
		for(MapVoteItem mvi : mapItems) {
			ItemStack mIs = mvi.getIs();
			if(mIs.equals(is)) {
				wantsToVote(playerEB, mvi);
				break;
			}
		}
	}
	
	private void wantsToVote(PlayerEB playerEB, MapVoteItem mvi) {
		if(playersVoted.containsKey(playerEB)) {
			MapVoteItem mviOld = playersVoted.get(playerEB);
			int votesOld = mviOld.getVotes();
			mviOld.setVotesAndReload(votesOld-1);	
		}
		int votes = mvi.getVotes();
		mvi.setVotesAndReload(votes+1);
		playersVoted.put(playerEB, mvi);
		String mapName = mvi.getMapName();
		playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Hlasoval/a si za mapu: "+ChatColor.GREEN+ChatColor.BOLD+mapName);
		playerEB.getPlayer().closeInventory();
	}

	public void reloadMaps() {
		List<String> loadedMaps = getLoadedMaps();
		mapItems.clear();
		for(int i = 0; i<loadedMaps.size();i++) {
			String mapName = loadedMaps.get(i);
			MapConfiguration wc = new MapConfiguration(mapName);
			String buildersName = wc.getConfig().getString("builder");
			MapVoteItem mvi = new MapVoteItem(mapName, buildersName);
			mapItems.add(mvi);
		}
	}
	
	public List<String> getLoadedMaps() {

		List<String> mapNames = new ArrayList<String>();
		File mapFolder = new File(Main.getPlugin().getDataFolder().getPath()+"/maps");
		
		for(File f : mapFolder.listFiles()) {
			String name = f.getName();
			name = name.replaceAll(".yml", "");
			if(name.equals("lobby")) {
				continue;
			}
			mapNames.add(name);
		}
		return mapNames;
	}
	
	public List<MapVoteItem> getMapItems() {
		return mapItems;
	}
	
}
