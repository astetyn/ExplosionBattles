package main.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.Game;
import main.Main;
import main.PlayerEB;
import main.configuration.WorldConfiguration;
import main.maps.MapPlayerChecker;
import main.maps.world.WorldTeleport;
import main.maps.world.WorldsEB;

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
		PlayerEB playerEB = Game.getInstance().getPlayer(p);

		if(argsSize!=1) {
			if(argsSize>=2) {
				String command = args[0].toLowerCase();
				if(command.equals("report")){
					
					String reportMessage = "";
					
					for(int i = 1;i<args.length;i++) {
						reportMessage+=args[i]+" ";
					}
					
					reportMessage = StringUtils.stripAccents(reportMessage);
					if(!Main.getPlugin().getDataFolder().exists()) {
						Main.getPlugin().getDataFolder().mkdirs();
					}
					File reportFolder = new File(Main.getPlugin().getDataFolder()+"/reports");
					if(!reportFolder.exists()) {
						reportFolder.mkdirs();
					}
					
					int numberOfFiles = reportFolder.listFiles().length;
					
					try {
						File file = new File(reportFolder.getPath()+"/"+numberOfFiles+p.getName()+".txt");
						file.createNewFile();
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						bw.write(reportMessage);
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					p.sendMessage("Dakujeme za prispevok a ospravedlnte nedokonalosti. Prispeli ste tym do vyvoja tejto minihry!");
					return true;
				}
			}
			
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
			if(!Game.getInstance().isPlayerInGame(p)) {
				p.sendMessage("Nenachadzas sa v hre.");
				return true;
			}
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
				
				WorldTeleport wt = new WorldTeleport(p,true);
				wt.teleport();
				
				return true;
			}else if(command.equals("save")) {
				WorldsEB worldsEB = new WorldsEB();
				worldsEB.saveWorld();
				p.sendMessage("Mozno uspesne ulozene.");
				return true;
			}
		}else if(argsSize==2) {
			String command = args[0];
			String mapName = args[1];
			String[] options = {"create","delete","addspawn","removespawn","spec","rain","night"};
			
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
					if(!worldName.equals(WorldsEB.getWorldNameSaved())) {
						p.sendMessage("V tomto svete sa mapa nastavit neda! Prejdi do EB sveta cez /eb world");
						return true;
					}
					Location loc = p.getLocation();
					loc.setWorld(Bukkit.getWorld(WorldsEB.getFullWorldName()));
					boolean b = wc.addspawn(loc);
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
					Location loc = p.getLocation();
					loc.setWorld(Bukkit.getWorld(WorldsEB.getFullWorldName()));
					wc.setSpawnSpecator(loc);
					p.sendMessage("Spawn na spectatora uspesne nastaveny.");
					return true;
				}else if(command.equals("rain")) {
					boolean tg = wc.getConfig().getBoolean("rain");
					if(tg) {
						tg = false;
						p.sendMessage("Dazd vypnuty.");
					}else {
						tg = true;
						p.sendMessage("Dazd zapnuty.");
					}
					wc.getConfig().set("rain", tg);
					wc.saveConfig();
				}else if(command.equals("night")) {
					boolean tg = wc.getConfig().getBoolean("night");
					if(tg) {
						tg = false;
						p.sendMessage("Noc vypnuta.");
					}else {
						tg = true;
						p.sendMessage("Noc zapnuta.");
					}
					wc.getConfig().set("night", tg);
					wc.saveConfig();
				}else {
					p.sendMessage("Tato mapa uz existuje.");
				}
			}
			return true;
		}
		return false;
	}
	
}
