package main.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import main.Main;
import net.md_5.bungee.api.ChatColor;

public class LeaderBoard {

	private final String PATH = Main.getPlugin().getDataFolder().getPath()+"/accounts";
	private File accountsFolder = new File(PATH);
	
	public void showLeaderBoard(Player p) {
		String text = "";
		text+=ChatColor.GOLD+"--- TOP "+ChatColor.BLUE+ChatColor.BOLD+"EP"+ChatColor.GOLD+" ---\n";
		int i = 0;
		for(MemberInLeaderBoard milb : getSortedList()) {
			text+=ChatColor.YELLOW+milb.getName()+" > "+ChatColor.WHITE+milb.getPoints()+ChatColor.BLUE+" EP \n";
			i++;
			if(i==10) {
				break;
			}
		}
		text+=ChatColor.GOLD+"--- TOP "+ChatColor.BLUE+ChatColor.BOLD+"EP"+ChatColor.GOLD+" ---\n";
		p.sendMessage(text);
	}
	
	private List<MemberInLeaderBoard> getSortedList() {
		List<MemberInLeaderBoard> list = getList();
		Collections.sort(list, new MemberPointsComparator());
		return list;
	}
	
	private List<MemberInLeaderBoard> getList() {
		
		List<MemberInLeaderBoard> list = new ArrayList<MemberInLeaderBoard>();
		
		for(File f : accountsFolder.listFiles()) {
			String name;
			int points;
			FileConfiguration config = YamlConfiguration.loadConfiguration(f);
			String fileName = f.getName();
			name = fileName.replaceAll(".yml", "");
			points = config.getInt("epoints");
			MemberInLeaderBoard milb = new MemberInLeaderBoard(name, points);
			list.add(milb);
		}
		
		return list;
	}
	
}
