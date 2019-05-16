package main.gameobjects.airdrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Plane {
	
	private double pushCoef = 2;

	private double pushX;
	private double pushZ;
	
	private Location planeLoc;
	
	private int rotation;
	private int tickCounter = 0;
	private int maxTicks;
	
	private boolean planeExists = false;
	
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
	
	public Plane(int xTotal,int zTotal, Location startLoc) {
		
		this.planeExists = true;
		this.planeLoc = startLoc;
		
		if(Math.abs(xTotal)>Math.abs(zTotal)) {
			maxTicks = (int) ((int) Math.abs(xTotal)/pushCoef);
			if(xTotal>0) {
				pushX = pushCoef;
				pushZ = zTotal/(xTotal/pushCoef);
				rotation = 0;
			}else {
				pushX = pushCoef*-1;
				pushZ = zTotal/(xTotal/pushCoef)*-1;
				rotation = 90;
			}
		}else {
			maxTicks = (int) ((int) Math.abs(zTotal)/pushCoef);
			if(zTotal>0) {
				pushX = xTotal/(zTotal/pushCoef);
				pushZ = pushCoef;
				rotation = 180;
			}else {
				pushX = xTotal/(zTotal/pushCoef)*-1;
				pushZ = pushCoef*-1;
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
						Block b = planeLoc.getWorld().getBlockAt(oldLocations[i][j]);
						b.setType(oldMaterials[i][j]);
					}
				}
			}
			planeExists = false;
			return;
		}
		
		planeLoc.add(new Location(planeLoc.getWorld(),pushX,0,0));
		planeLoc.add(new Location(planeLoc.getWorld(),0,0,pushZ));
		
		if(tickCounter>1) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Block b = planeLoc.getWorld().getBlockAt(oldLocations[i][j]);
					b.setType(oldMaterials[i][j]);
				}
			}
		}
		
		if(rotation==0) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(planeLoc.getWorld(),planeLoc.getX()-i,planeLoc.getY(),planeLoc.getZ()+j);
					Block b = planeLoc.getWorld().getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}else if(rotation==90) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(planeLoc.getWorld(),planeLoc.getX()+i,planeLoc.getY(),planeLoc.getZ()-j);
					Block b = planeLoc.getWorld().getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}else if(rotation==180) {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(planeLoc.getWorld(),planeLoc.getX()-j,planeLoc.getY(),planeLoc.getZ()-i);
					Block b = planeLoc.getWorld().getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}else {
			for(int i=0;i<planeConstruction.length;i++) {
				for(int j=0;j<planeConstruction[0].length;j++) {
					Location loc = new Location(planeLoc.getWorld(),planeLoc.getX()-j,planeLoc.getY(),planeLoc.getZ()+i);
					Block b = planeLoc.getWorld().getBlockAt(loc);
					oldMaterials[i][j] = b.getType();
					b.setType(planeConstruction[i][j]);
					oldLocations[i][j] = loc;
				}
			}
		}
	}

	public Location getLocation() {
		return planeLoc;
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
