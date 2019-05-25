package main.maps.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import main.Game;
import main.Main;

public class WorldsEB {

	private String WORLD_NAME;
	private String WORLD_NAME_SAVED;
	private final String PATH = Main.getPlugin().getDataFolder().getPath()+"/";
	
	private String FULL_WORLD_NAME;
	private String FULL_WORLD_NAME_SAVED;

	private World world;
	private World savedWorld;
	
	private final Logger LOG = Bukkit.getServer().getLogger();
	
	private boolean rain;
	private boolean night;
	
	public WorldsEB() {
		
		WORLD_NAME = Game.getInstance().getConfiguration().getConfig().getString("world.world-name");
		WORLD_NAME_SAVED = WORLD_NAME + "-BACKUP";
		FULL_WORLD_NAME = PATH + WORLD_NAME;
		FULL_WORLD_NAME_SAVED = PATH + WORLD_NAME_SAVED;
		
		savedWorld = Bukkit.getServer().getWorld(FULL_WORLD_NAME_SAVED);
		world = Bukkit.getServer().getWorld(FULL_WORLD_NAME);

	}
	
	public void restartGameWorld() {
		
		unloadGameWorld();
		copyWorld(FULL_WORLD_NAME_SAVED,FULL_WORLD_NAME);
		loadGameWorld();
		
	}
	
	public void saveWorld() {
		
		if(savedWorld==null) {
			return;
		}
		
		List<Entity> entities = savedWorld.getEntities();
		for (Entity entity : entities){
			if(entity instanceof Monster||entity instanceof Animals){
				entity.remove();
			}
		}
		
		savedWorld.save();
		restartGameWorld();
		
	}
	
	public void loadSavedWorld() {
		
		if(savedWorld==null) {
			WorldCreator wg = new WorldCreator(FULL_WORLD_NAME_SAVED);
			wg.generateStructures(false);
			wg.generator(new CustomWorldGenerator());
			
			savedWorld = wg.createWorld();
			savedWorld.setAutoSave(false);
			savedWorld.setKeepSpawnInMemory(false);
		}
		savedWorld = Bukkit.getServer().getWorld(FULL_WORLD_NAME_SAVED);
	}
	
	public void loadGameWorld() {	
		
		if(world==null) {
			WorldCreator wg = new WorldCreator(FULL_WORLD_NAME);
			wg.generateStructures(false);
			wg.generator(new CustomWorldGenerator());
			
			world = wg.createWorld();
			world.setAutoSave(false);
			world.setKeepSpawnInMemory(false);
		}
		world = Bukkit.getServer().getWorld(FULL_WORLD_NAME);
	}	
	
	public void unloadSavedWorld() {
		
		if(savedWorld==null) {
			LOG.info("ExplosionBattles >> Svet sa nepodarilo odnacitat.");
			return;
		}

		for(Player p : savedWorld.getPlayers()) {
			p.kickPlayer("You are standing in restarting world. Join again and move to another world!");
		}
		
		if(Bukkit.getServer().unloadWorld(FULL_WORLD_NAME_SAVED, false)) {
			LOG.info("ExplosionBattles >> Svet uspesne odnacitany.");
			savedWorld = null;
		}else {
			LOG.info("ExplosionBattles >> Svet sa nepodarilo odnacitat.");
		}	
	}
	
	public void unloadGameWorld() {
		
		if(world==null) {
			LOG.info("ExplosionBattles >> Svet sa nepodarilo odnacitat.");
			return;
		}

		for(Player p : world.getPlayers()) {
			p.kickPlayer("You are standing in restarting world. Join again and move to another world!");
		}
		
		if(Bukkit.getServer().unloadWorld(FULL_WORLD_NAME, false)) {
			LOG.info("ExplosionBattles >> Svet uspesne odnacitany.");
			world = null;
		}else {
			LOG.info("ExplosionBattles >> Svet sa nepodarilo odnacitat.");
		}	
	}
	
	private void copyWorld(String source, String target){

		File sourceFolder = new File(source);
		File targetFolder = new File(target);
		
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(sourceFolder.getName())) {
	            if(sourceFolder.isDirectory()) {
	                if(!targetFolder.exists()) {
	                	targetFolder.mkdirs();
	                }
	                String files[] = sourceFolder.list();
	                for (String file : files) {
	                    File srcFile = new File(sourceFolder, file);
	                    File destFile = new File(targetFolder, file);
	                    copyFiles(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(sourceFolder);
	                OutputStream out = new FileOutputStream(targetFolder);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0) {
	                    out.write(buffer, 0, length);
	                }
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	private void copyFiles(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyFiles(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
	
	public World getGameWorld() {
		return world;
	}
	
	public World getSavedWorld() {
		return savedWorld;
	}

	public boolean isRain() {
		return rain;
	}

	public void setRain(boolean rain) {
		this.rain = rain;
	}

	public boolean isNight() {
		return night;
	}

	public void setNight(boolean night) {
		this.night = night;
	}
	
	public String getFullWorldName() {
		return FULL_WORLD_NAME;
	}
	
	public String getWorldNameSaved() {
		return WORLD_NAME_SAVED;
	}
}
