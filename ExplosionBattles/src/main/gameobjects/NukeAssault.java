package main.gameobjects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.configuration.MapConfiguration;
import main.gameobjects.planes.NukePlane;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class NukeAssault extends GameObject {

	private boolean active = false;
	private int ticks = 0;
	private NukePlanesFormation npf;
	private NukePlane midPlane;
	private int dropTick;
	
	public NukeAssault() {
		active = true;
		MapConfiguration wc = new MapConfiguration(Game.getInstance().getMap());
		int maxLoc = Game.getInstance().getConfiguration().getConfig().getInt("game.max-players");
		int locIndex = (int) (Math.random()*maxLoc);
		if(locIndex==maxLoc) {
			locIndex--;
		}
		if(locIndex==0) {
			locIndex = 1;
		}

		String loc1 = "loc"+String.valueOf(locIndex);
		String loc2 = "loc"+String.valueOf(locIndex+1);
		Location locationInArena = (Location) wc.getConfig().get(loc1);
		Location locationInArena2 = (Location) wc.getConfig().get(loc2);
		
		int x1 = locationInArena.getBlockX();
		int x2 = locationInArena2.getBlockX();
		
		int z1 = locationInArena.getBlockZ();
		int z2 = locationInArena2.getBlockZ();
		
		Location checkArea = locationInArena;
		Block b = checkArea.getBlock();
		int cycle = 0;
		while(b.getType()!=Material.BARRIER) {
			checkArea.add(new Location(checkArea.getWorld(),0,1,0));
			b = checkArea.getBlock();
			cycle++;
			if(cycle==200) {
				break;
			}
		}
		int height = checkArea.getBlockY();
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			String message = MsgCenter.ALLERT+ChatColor.WHITE+"Pozor! Atomová bomba bude čoskoro zhodená! "+MsgCenter.ALLERT;
			playerEB.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
		}
		
		Location startLoc = new Location(locationInArena.getWorld(), x1, height, z1);
		Location endLoc = new Location(locationInArena2.getWorld(), x2, height, z2);
		
		
		npf = new NukePlanesFormation(startLoc, endLoc);
		midPlane = npf.getMidPlane();
	
		dropTick = (int) (Math.random()*midPlane.getMaxTicks());
		if(dropTick==0) {
			dropTick = 1;
		}
	}

	@Override
	public void tick() {
		ticks++;
		
		if(ticks%20!=0) {
			return;
		}
		npf.tick();
		if(ticks/20==dropTick) {
			releaseTheNuke();
		}

		if(!npf.getMidPlane().isActive()) {
			active = false;
		}
	}

	private void releaseTheNuke() {
		Location loc = npf.getMidPlane().getMidLocation();
		loc.add(0,-1,0);
		Entity nuke = loc.getWorld().spawn(loc,TNTPrimed.class);
		nuke.setMetadata("nuke", new FixedMetadataValue(Main.getPlugin(),"20"));
		((TNTPrimed)nuke).setFuseTicks(100);
		((TNTPrimed)nuke).setYield(40);
	}
	
	@Override
	public boolean isActive() {
		return active;
	}

}

class NukePlanesFormation {
	
	private NukePlane planeMid;
	private NukePlane planeLeft;
	private NukePlane planeRight;
	
	public NukePlanesFormation(Location startLocMid, Location endLocMid) {
		
		planeMid = new NukePlane(startLocMid, endLocMid);
		int rotation = planeMid.getRotation();
		int width = planeMid.getPlaneConstruction()[0].length;
		
		Location leftStartLoc = startLocMid.clone();
		Location rightStartLoc = startLocMid.clone();
		
		Location leftEndLoc = endLocMid.clone();
		Location rightEndLoc = endLocMid.clone();
		
		int shiftL = 5;
		int shiftW = 3;
		
		if(rotation==0) {
			leftStartLoc.add(-shiftL,0,-width-shiftW);
			rightStartLoc.add(-shiftL,0,width+shiftW);
			
			leftEndLoc.add(-shiftL,0,-width-shiftW);
			rightEndLoc.add(-shiftL,0,width+shiftW);
		}else if(rotation==90) {
			leftStartLoc.add(width+shiftW,0,-shiftL);
			rightStartLoc.add(-width-shiftW,0,-shiftL);
			
			leftEndLoc.add(width+shiftW,0,-shiftL);
			rightEndLoc.add(-width-shiftW,0,-shiftL);
		}else if(rotation==180) {
			leftStartLoc.add(shiftL,0,width+shiftW);
			rightStartLoc.add(shiftL,0,-width-shiftW);
			
			leftEndLoc.add(shiftL,0,width+shiftW);
			rightEndLoc.add(shiftL,0,-width-shiftW);
		}else {
			leftStartLoc.add(-width-shiftW,0,shiftL);
			rightStartLoc.add(width+shiftW,0,shiftL);
			
			leftEndLoc.add(-width-shiftW,0,shiftL);
			rightEndLoc.add(width+shiftW,0,shiftL);
		}
		
		planeLeft = new NukePlane(leftStartLoc, leftEndLoc);
		planeRight = new NukePlane(rightStartLoc, rightEndLoc);
		
	}
	
	public void tick() {
		planeMid.tick();
		planeLeft.tick();
		planeRight.tick();
	}
	
	public NukePlane getMidPlane() {
		return planeMid;
	}
}
