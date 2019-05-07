package main.misc.airdrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Plane {
	
	private double divider = 2;

	private double pushX;
	private double pushZ;
	
	private double x;
	private int y;
	private double z;
	private World world;
	
	private int rotation;
	private int tickCounter = 0;
	private int maxTicks;
	
	private boolean planeExists = false;
	private boolean writen = false;
	
	private Material oldMaterials[][] = new Material[5][5];
	private Location oldLocations[][] = new Location[5][5];

	private Material air = Material.BARRIER;
	private Material front = Material.GLASS;
	private Material hull = Material.COAL_BLOCK;
	private Material wing = Material.STEP;
	
	private Material planeConstruction[][] = {
	{air,air,front,air,air},
	{wing,wing,hull,wing,wing},
	{air,air,hull,air,air},
	{air,air,hull,air,air},
	{air,wing,hull,wing,air}};
	
	public Plane(World world,int xTotal,int zTotal,int xStart,int yStart,int zStart) {
		this.planeExists = true;
		this.x = xStart;
		this.y = yStart;
		this.z = zStart;
		this.world = world;
		
		if(Math.abs(xTotal)>Math.abs(zTotal)) {
			maxTicks = (int) ((int) xTotal/divider);
			pushX = divider;
			pushZ = zTotal/(xTotal/divider);
			if(xTotal>0) {
				rotation = 0;
			}else {
				rotation = 90;
			}
		}else {
			maxTicks = (int) ((int) zTotal/divider);
			pushX = xTotal/(zTotal/divider);
			pushZ = divider;
			if(zTotal>0) {
				rotation = 180;
			}else {
				rotation = 270;
			}
		}	
		
	}
	
	public void tick() {
		
		tickCounter++;
		
		if(tickCounter==maxTicks) {
			if(oldLocations[0]!=null) {
				for(int i=0;i<planeConstruction.length;i++) {
					for(int j=0;j<planeConstruction[0].length;j++) {
						Block b = world.getBlockAt(oldLocations[i][j]);
						b.setType(oldMaterials[i][j]);
					}
				}
			}
			planeExists = false;
			return;
		}
		
		x+=pushX;
		z+=pushZ;
		
		if(writen) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Block b = world.getBlockAt(oldLocations[i][j]);
					b.setType(oldMaterials[i][j]);
				}
			}
		}
		
		if(rotation==0) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(world,x-i,y,z+j);
					Block b = world.getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}else if(rotation==90) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(world,x+i,y,z-j);
					Block b = world.getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}else if(rotation==180) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(world,x+j,y,z-i);
					Block b = world.getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}else {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(world,x-j,y,z+i);
					Block b = world.getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}
		writen = true;
	}

	public Location getLocation() {
		return new Location(world,x,y,z);
	}
	
	public boolean isPlaneExists() {
		return planeExists;
	}

	public void setPlaneExists(boolean planeExists) {
		this.planeExists = planeExists;
	}

	public int getMaxTicks() {
		return this.maxTicks;
	}
	
}
