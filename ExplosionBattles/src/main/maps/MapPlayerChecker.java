package main.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.configuration.MapConfiguration;
import net.md_5.bungee.api.ChatColor;

public class MapPlayerChecker {

	List<File> files = new ArrayList<File>();
	List<String> fileNames = new ArrayList<String>();
	File mapFolder = new File(Main.getPlugin().getDataFolder()+"/maps");
	
	public void loadFiles() {

		for(File f : mapFolder.listFiles()) {
			String name = f.getName();
			name = name.replaceAll(".yml", "");
			if(name.equals("config")||name.equals("lobby")) {
				continue;
			}
			fileNames.add(name);
			files.add(f);
		}
	}
	
	public void showList(Player p) {
		
		if(fileNames.isEmpty()) {
			p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Žiadne mapy este neboli vytvorené. Vytvoríš ich cez /ebsetup create <name>");
		}
		
		for(String name : fileNames) {
			MapConfiguration wc = new MapConfiguration(name);
			int spawns = wc.getSpawns();
			int maxSpawns = Game.getInstance().getConfiguration().getConfig().getInt("game.max-players");
			boolean completed = new MapSystemChecker(name).check();
			if(completed) {
				p.sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"HOTOVÁ"+ChatColor.GRAY+" mapa: "+ChatColor.YELLOW+name+ChatColor.GRAY+". Spawny: "+ChatColor.YELLOW+ spawns+"/"+maxSpawns+ChatColor.GRAY+". Spectator: "+ChatColor.YELLOW+wc.spectatorExists());
			}else {
				p.sendMessage(MsgCenter.PREFIX+ChatColor.DARK_RED+"NEDOKONČENÁ"+ChatColor.GRAY+" mapa: "+ChatColor.YELLOW+name+ChatColor.GRAY+". Spawny: "+ChatColor.YELLOW+ spawns+"/"+maxSpawns+ChatColor.GRAY+". Spectator: "+ChatColor.YELLOW+wc.spectatorExists());
			}
			
		}
	}
	
	public List<String> getMaps() {
		return fileNames;
	}
	
}
