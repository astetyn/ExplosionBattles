package main.gameobjects.planes;

import org.bukkit.Location;
import org.bukkit.Material;

public class NukePlane extends Plane {

	private static Material air = Material.BARRIER;
	private static Material front = Material.GLASS;
	private static Material hull = Material.COAL_BLOCK;
	private static Material wing = Material.DOUBLE_STEP;
	
	private static final Material[][] PLANE_CONSTRUCTION = {
	{air,air,front,air,air},
	{wing,wing,hull,wing,wing},
	{air,air,hull,air,air},
	{air,air,hull,air,air},
	{air,wing,hull,wing,air}};
	
	public NukePlane(Location startLoc, Location endLoc) {
		super(startLoc, endLoc, 4, PLANE_CONSTRUCTION);
	}

}
