package main.listeners;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
import main.MsgCenter;
import main.configuration.Configuration;
import main.configuration.LeaderBoard;
import main.configuration.MapConfiguration;
import main.maps.MapPlayerChecker;
import main.maps.world.WorldTeleport;
import main.maps.world.WorldsEB;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

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
			if(args.length==1) {
				if(args[0].equals("invitation")) {
					sendToBungeeCord((Player) sender, "invitation");
					sender.sendMessage("Žiadosť poslaná bungeecordu.");
					return true;
				}
			}
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
						e.printStackTrace();
					}	
					p.sendMessage(MsgCenter.PREFIX+ChatColor.GREEN+"Ďakujeme za príspevok a ospravedlneňte nedokonalosti. Prispeli ste tým do vývoja tejto minihry!");
					return true;
				}
			}
			
			return false;
		}
		
		String command = args[0].toLowerCase();
		
		if(command.equals("join")) {
			if(playerEB!=null) {
				p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Už si pripojený/á v hre.");
				return true;
			}
			Game.getInstance().playerJoin(p);
			return true;
		}else if(command.equals("leave")) {
			if(!Game.getInstance().isPlayerInGame(p)) {
				p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Nenachádzaš sa v hre.");
				return true;
			}
			Game.getInstance().playerForceLeave(playerEB);
			p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Odpojil/a si sa z hry.");
			return true;
		}else if(command.equals("top")) {
			new LeaderBoard().showLeaderBoard(p);
			return true;
		}
		return false;
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
			}else if(command.equals("lobby")) {
				MapConfiguration wc = new MapConfiguration("lobby");
				boolean configExists = wc.configExists();
				
				Location loc = p.getLocation();
				
				p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Spawn lobby úspešne nastavený.");
				
				if(!configExists) {
					wc.getConfig().addDefault("spawnlobby", loc);
					wc.getConfig().options().copyDefaults(true);
					wc.saveConfig();
					return true;
				}
				wc.getConfig().set("spawnlobby", loc);
				wc.saveConfig();
				return true;
			}else if(command.equals("world")) {
				
				WorldTeleport wt = new WorldTeleport(p,true);
				wt.teleport();
				
				return true;
			}else if(command.equals("save")) {
				WorldsEB worldsEB = new WorldsEB();
				worldsEB.saveWorld();
				p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Svet možno uložený.");
				return true;
			}else if(command.equals("reload")) {
				Game.getInstance().setConfiguration(new Configuration(Main.getPlugin()));
				p.sendMessage("EB config reloaded.");
				return true;
			}
		}else if(argsSize==2) {
			String command = args[0];
			String mapName = args[1];
			String[] options = {"create","delete","add","remove","spectator","rain","night"};
			
			boolean bo = false;
			for(String s : options) {
				if(s.equals(command)) {
					bo=true;
				}
			}
			if(!bo) {
				return false;
			}
			
			MapConfiguration wc = new MapConfiguration(mapName);
			
			boolean configExists = wc.configExists();
			
			if(!configExists) {
				if(command.equals("create")) {
					wc.createConfig();
					p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Mapa úspešne vytvorená.");
					return true;
				}
				p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Takáto mapa neexistuje.");
				return true;
			}else {
				if(command.equals("delete")) {
					wc.deleteConfig();
					p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Mapa úspešne vymazaná.");
				}else if(command.equals("add")) {
					World w = p.getLocation().getWorld();
					String[] arr = w.getName().split("/");
					String worldName = arr[arr.length-1];
					if(!worldName.equals(new WorldsEB().getWorldNameSaved())) {
						p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"V tomto svete sa mapa nastaviť nedá! Prejdi do EB sveta cez /ebs world.");
						return true;
					}
					Location loc = p.getLocation();
					loc.setWorld(Bukkit.getWorld(new WorldsEB().getFullWorldName()));
					boolean b = wc.addspawn(loc);
					if(b) {
						p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Spawn úspešne nastavený.");
					}else {
						p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Už si nastavil všetky spawny.");
					}
					return true;
				}else if(command.equals("remove")) {
					boolean b = wc.removespawn();
					if(b) {
						p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Spawn úspešne vymazaný.");
					}else {
						p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Žiadny spawn sa už nedá vymazať.");
					}
				}else if(command.equals("spectator")) {
					Location loc = p.getLocation();
					loc.setWorld(Bukkit.getWorld(new WorldsEB().getFullWorldName()));
					wc.setSpawnSpecator(loc);
					p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Spawn na spectatora úspešne nastavený.");
					return true;
				}else if(command.equals("rain")) {
					boolean tg = wc.getConfig().getBoolean("rain");
					if(tg) {
						tg = false;
						p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Dážď vypnutý.");
					}else {
						tg = true;
						p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Dážď zapnutý.");
					}
					wc.getConfig().set("rain", tg);
					wc.saveConfig();
				}else if(command.equals("night")) {
					boolean tg = wc.getConfig().getBoolean("night");
					if(tg) {
						tg = false;
						p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Noc vypnutá.");
					}else {
						tg = true;
						p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Noc zapnutá.");
					}
					wc.getConfig().set("night", tg);
					wc.saveConfig();
				}else {
					p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Takáto mapa už existuje.");
				}
			}
			return true;
		}
		return false;
	}
	
	private void sendToBungeeCord(Player p, String channel){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(Main.getPlugin(), "explosionbattles", b.toByteArray());
        try {
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}
