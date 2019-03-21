package main.maps;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import main.Main;
import main.configuration.Configuration;

public class WorldEB {

	private Configuration c = new Configuration(Main.getPlugin());
	private final String WORLD_NAME = c.getConfig().getString("misc.world-name");
	private final String PATH = Main.getPlugin().getDataFolder().getPath()+"/";
	
	private World world;
	private final Logger LOG = Bukkit.getServer().getLogger();
	
	public WorldEB() {
		
		world = Bukkit.getServer().getWorld(PATH+WORLD_NAME);
		
	}
	
	public void loadWorld() {	
		
		if(world!=null) {
			LOG.info("ExplosionBattles >> Svet uz je nacitany.");
			return;
		}
		
		WorldCreator wg = new WorldCreator(PATH+WORLD_NAME);
		wg.generateStructures(false);
		wg.generator(new Generator());
		
		this.world = wg.createWorld();
		this.world.setAutoSave(false);
		this.world.setKeepSpawnInMemory(false);
		
		LOG.info("ExplosionBattles >> Svet uspesne nacitany.");
		
	}
	
	public void unloadWorld() {
		
		if(world==null) {
			LOG.info("ExplosionBattles >> Svet sa nepodarilo odnacitat.");
			return;
		}

		for(Player p : world.getPlayers()) {
			p.kickPlayer("You are standing in restarting world. Join again and move to another world!");
		}
		
		if(Bukkit.getServer().unloadWorld(PATH+WORLD_NAME, false)) {
			LOG.info("ExplosionBattles >> Svet uspesne odnacitany.");
		}else {
			LOG.info("ExplosionBattles >> Svet sa nepodarilo odnacitat.");
		}	
	}
	
	public World getWorld() {
		return world;
	}
}
