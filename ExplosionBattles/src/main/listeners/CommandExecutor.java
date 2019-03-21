package main.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.Game;
import main.Main;
import main.PlayerEB;
import main.configuration.Configuration;
import main.configuration.WorldConfiguration;
import main.maps.MapPlayerChecker;
import main.maps.WorldEB;

public class CommandExecutor {

	private CommandSender sender;
	private Command cmd;
	private String[] args;
	
	public CommandExecutor(CommandSender sender, Command cmd, String label, String[] args) {
		this.sender = sender;
		this.cmd = cmd;
		this.args = args;
	}
	
	public boolean onCommand() {
		if(!(sender instanceof Player)) {
			return false;
		}
		if(cmd.getName().equalsIgnoreCase("explosionbattles")) {	
			return executeCmd();
		}else if(cmd.getName().equalsIgnoreCase("explosionbattlessetup")) {
			return executeCmdSetup();
		}
		return true;
	}
	
	private boolean executeCmd() {
		int argsSize = args.length;
		Player p = (Player) sender;
		PlayerEB playerEB = null;
		for(PlayerEB peb : Game.getInstance().getPlayers()) {
			if(peb.getPlayer().equals(p)) {
				playerEB = peb;
			}
		}
		
		if(argsSize!=1) {
			return false;
		}
		
		String command = args[0].toLowerCase();
		
		if(command.equals("join")) {
			if(playerEB!=null) {
				p.sendMessage("Uz si pripojeny v hre.");
				return true;
			}
			Game.getInstance().playerJoin(p);
			p.sendMessage("Pripojil si sa do hry.");
		}else if(command.equals("leave")) {
			Game.getInstance().playerForceLeave(playerEB);
			p.sendMessage("Odpojil si sa z hry.");
		}else {
			return false;
		}
		return true;
	}
	
	private boolean executeCmdSetup() {
		int argsSize = args.length;
		Player p = (Player) sender;
		
		if(argsSize==1) {
			String command = args[0];
			if(command.equals("list")) {
				MapPlayerChecker mc = new MapPlayerChecker();
				mc.loadFiles();
				mc.showList(p);
				return true;
			}else if(command.equals("spawnlobby")) {
				WorldConfiguration wc = new WorldConfiguration("lobby");
				boolean configExists = wc.configExists();
				
				Location loc = p.getLocation();
				
				if(!configExists) {
					wc.getConfig().addDefault("spawnlobby", loc);
					wc.getConfig().options().copyDefaults(true);
					wc.saveConfig();
					p.sendMessage("Spawn lobby uspesne nastaveny.");
					return true;
				}
				wc.getConfig().set("spawnlobby", loc);
				wc.saveConfig();
				p.sendMessage("Spawn lobby uspesne nastaveny.");
				return true;
			}else if(command.equals("world")) {
				
				WorldEB worldEB = new WorldEB();
				worldEB.loadWorld();
				Location loc = new Location(worldEB.getWorld(),0,150,0);
				p.teleport(loc);
				
				return true;
			}
		}else if(argsSize==2) {
			String command = args[0];
			String mapName = args[1];
			String[] options = {"create","delete","addspawn","removespawn","spawnspec"};
			
			boolean bo = false;
			for(String s : options) {
				if(s.equals(command)) {
					bo=true;
				}
			}
			if(!bo) {
				return false;
			}
			
			WorldConfiguration wc = new WorldConfiguration(mapName);
			
			boolean configExists = wc.configExists();
			
			if(!configExists) {
				if(command.equals("create")) {
					wc.createConfig();
					p.sendMessage("Mapa uspesne zaregistrovana");
					return true;
				}
				p.sendMessage("Takato mapa neexistuje");
				return true;
			}else {
				if(command.equals("delete")) {
					wc.deleteConfig();
					p.sendMessage("Mapa uspesne vymazana");
				}else if(command.equals("addspawn")) {
					World w = p.getLocation().getWorld();
					String[] arr = w.getName().split("/");
					String worldName = arr[arr.length-1];
					Configuration c = new Configuration(Main.getPlugin());
					String worldNameConfig = c.getConfig().getString("misc.world-name");
					if(!worldName.equals(worldNameConfig)) {
						Bukkit.broadcastMessage(worldName+ " "+worldNameConfig);
						p.sendMessage("V tomto svete sa mapa nastavit neda! Prejdi do sveta: "+worldNameConfig);
						return true;
					}
					boolean b = wc.addspawn(p.getLocation());
					if(b) {
						p.sendMessage("Spawn uspesne nastaveny");
					}else {
						p.sendMessage("Uz si nastavil vsetky spawny");
					}
					return true;
				}else if(command.equals("removespawn")) {
					boolean b = wc.removespawn();
					if(b) {
						p.sendMessage("Spawn uspesne zruseny");
					}else {
						p.sendMessage("Ziadny spawn sa uz nema vymazat");
					}
				}else if(command.equals("spec")) {
					wc.setSpawnSpecator(p.getLocation());
					p.sendMessage("Spawn na spectatora uspesne nastaveny.");
					return true;
				}else {
					p.sendMessage("Tato mapa uz existuje.");
				}
			}
			return true;
		}
		return false;
	}
	
}
