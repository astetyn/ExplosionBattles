package main.maps.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldTeleport {

	private Player player;
	
	public WorldTeleport(Player p) {
		this.player = p;
	}
	
	public void teleport() {
		
		WorldEB worldEB = new WorldEB();
		worldEB.loadWorld();
		Location loc = worldEB.getWorld().getSpawnLocation();
		if(isAreaClear(loc)) {
			fillArea(loc);
		}
		player.teleport(loc);
		
	}
	
	private void fillArea(Location loc) {
		
		World world = loc.getWorld();
		
		int startX = loc.getBlockX()-3;
		int startY = loc.getBlockY()-1;
		int startZ = loc.getBlockZ()-3;
		
		for(int x=startX;x<startX+6;x++) {
			for(int z=startZ;z<startZ+6;z++) {
				Block b = world.getBlockAt(new Location(world,x,startY,z));
				b.setType(Material.STONE);
			}
		}
		
	}
	
	private boolean isAreaClear(Location loc) {
		
		World world = loc.getWorld();
		
		int startX = loc.getBlockX()-3;
		int startY = loc.getBlockY()-1;
		int startZ = loc.getBlockZ()-3;
		
		for(int x=startX;x<startX+6;x++) {
			for(int z=startZ;z<startZ+6;z++) {
				Block b = world.getBlockAt(new Location(world,x,startY,z));
				if(!b.getType().equals(Material.AIR)) {
					return false;
				}
			}
		}
		return true;
	}
	
}
