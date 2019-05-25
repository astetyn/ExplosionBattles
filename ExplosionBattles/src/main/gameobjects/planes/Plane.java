package main.gameobjects.planes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Plane {
	
	private double pushCoef;

	private double pushX;
	private double pushZ;
	
	private Location edgePlaneLoc;
	
	private int rotation;
	private int tickCounter = 0;
	private int maxTicks;
	
	private boolean planeActive = false;
	
	private Location oldLocations[][];
	
	private Material planeConstruction[][];
	
	public Plane(Location startLoc, Location endLoc, int speed, Material planeConstruction[][]) {
		
		planeActive = true;
		pushCoef = speed;
		this.planeConstruction = planeConstruction;
		
		int length = planeConstruction.length;
		int width = planeConstruction[0].length;
		
		oldLocations = new Location[length][width];
		
		int x1 = startLoc.getBlockX();
		int x2 = endLoc.getBlockX();
		
		int z1 = startLoc.getBlockZ();
		int z2 = endLoc.getBlockZ();
		
		int xTotal = (x1-x2)*-1;
		int zTotal = (z1-z2)*-1;
		
		if(Math.abs(xTotal)>Math.abs(zTotal)) {
			maxTicks = (int) ((int) Math.abs(xTotal)/pushCoef);
			if(xTotal>0) {
				pushX = pushCoef;
				pushZ = zTotal/(xTotal/pushCoef);
				rotation = 0;
			}else {
				pushX = pushCoef*-1;
				pushZ = zTotal/(xTotal/pushCoef)*-1;
				rotation = 180;
			}
		}else {
			maxTicks = (int) ((int) Math.abs(zTotal)/pushCoef);
			if(zTotal>0) {
				pushX = xTotal/(zTotal/pushCoef);
				pushZ = pushCoef;
				rotation = 90;
			}else {
				pushX = xTotal/(zTotal/pushCoef)*-1;
				pushZ = pushCoef*-1;
				rotation = 270;
			}
		}	
		edgePlaneLoc = getEdgeFromMid(startLoc, rotation, length, width);
	}
	
	public void tick() {
		
		tickCounter++;
		
		int length = planeConstruction.length;
		int width = planeConstruction[0].length;
		
		if(tickCounter==maxTicks) {
			if(oldLocations[0]!=null) {
				for(int i=0;i<length;i++) {
					for(int j=0;j<width;j++) {
						Block b = edgePlaneLoc.getWorld().getBlockAt(oldLocations[i][j]);
						b.setType(Material.BARRIER,false);
					}
				}
			}
			planeActive = false;
			return;
		}
		
		edgePlaneLoc.add(pushX,0,0);
		edgePlaneLoc.add(0,0,pushZ);
		
		if(tickCounter>1) {
			for(int i=0;i<length;i++) {
				for(int j=0;j<width;j++) {
					Block b = edgePlaneLoc.getWorld().getBlockAt(oldLocations[i][j]);
					b.setType(Material.BARRIER,false);
				}
			}
		}
		
		for(int i=0;i<length;i++) {
			for(int j=0;j<width;j++) {
				int xM = 0;
				int zM = 0;
				if(rotation==0) {
					xM = (length-i);
					zM = j;
				}else if(rotation==90) {
					xM = j*-1;
					zM = (length-i);
				}else if(rotation==180) {
					xM = (length-i)*-1;
					zM = j*-1;
				}else {
					xM = j;
					zM = (length-i)*-1;
				}
				Location loc = new Location(edgePlaneLoc.getWorld(),edgePlaneLoc.getX()+xM,edgePlaneLoc.getY(),edgePlaneLoc.getZ()+zM);
				Block b = edgePlaneLoc.getWorld().getBlockAt(loc);
				oldLocations[i][j] = loc;
				b.setType(planeConstruction[i][j],false);
			}
		}
	}

	private Location getMidFromEdge(Location edgeLoc, int rotation, int length, int width) {
		
		int edgeX = edgeLoc.getBlockX();
		int edgeZ = edgeLoc.getBlockZ();
		
		int realX = 0;
		int realZ = 0;
		int realY = edgeLoc.getBlockY();
		
		if(rotation==0) {
			realX = edgeX + length/2;
			realZ = edgeZ + width/2;
		}else if(rotation==90) {
			realX = edgeX - length/2;
			realZ = edgeZ + width/2;
		}else if(rotation==180) {
			realX = edgeX - length/2;
			realZ = edgeZ - width/2;
		}else {
			realX = edgeX + length/2;
			realZ = edgeZ - width/2;
		}
		return new Location(edgeLoc.getWorld(),realX,realY,realZ);
	}
	
	private Location getEdgeFromMid(Location midLoc, int rotation, int length, int width) {
		
		int edgeX = midLoc.getBlockX();
		int edgeZ = midLoc.getBlockZ();
		
		int realX = 0;
		int realZ = 0;
		int realY = midLoc.getBlockY();
		
		if(rotation==0) {
			realX = edgeX - length/2;
			realZ = edgeZ - width/2;
		}else if(rotation==90) {
			realX = edgeX + length/2;
			realZ = edgeZ - width/2;
		}else if(rotation==180) {
			realX = edgeX + length/2;
			realZ = edgeZ + width/2;
		}else {
			realX = edgeX - length/2;
			realZ = edgeZ + width/2;
		}
		return new Location(midLoc.getWorld(),realX,realY,realZ);
	}
	
	public Location getEdgeLocation() {
		return edgePlaneLoc;
	}
	
	public Location getMidLocation() {
		int length = planeConstruction.length;
		int width = planeConstruction[0].length;
		return getMidFromEdge(edgePlaneLoc, rotation, length, width);
	}
	
	public boolean isActive() {
		return planeActive;
	}

	public int getMaxTicks() {
		return maxTicks;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public Material[][] getPlaneConstruction(){
		return planeConstruction;
	}
	
}
