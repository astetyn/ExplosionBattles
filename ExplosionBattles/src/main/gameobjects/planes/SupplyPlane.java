package main.gameobjects.planes;

import org.bukkit.Location;
import org.bukkit.Material;

public class SupplyPlane extends Plane {
	
	private static Material air = Material.BARRIER;
	private static Material front = Material.GLASS;
	private static Material light = Material.SEA_LANTERN;
	private static Material hull = Material.COAL_BLOCK;
	private static Material wing = Material.DOUBLE_STEP;
	
	private final static Material PLANE_CONSTRUCTION[][] = {
	{air,air,air,air,front,air,air,air,air},
	{air,air,air,front,front,front,air,air,air},
	{air,air,air,hull,light,hull,air,air,air},
	{air,air,wing,hull,hull,hull,wing,air,air},
	{air,wing,wing,hull,hull,hull,wing,wing,air},
	{wing,wing,wing,hull,hull,hull,wing,wing,wing},
	{air,air,hull,hull,hull,hull,hull,air,air},
	{air,air,air,hull,hull,hull,air,air,air},
	{air,air,wing,hull,wing,hull,wing,air,air},
	{air,wing,wing,hull,wing,hull,wing,wing,air}};

	
	public SupplyPlane(Location startLoc, Location endLoc) {
		super(startLoc, endLoc, 2, PLANE_CONSTRUCTION);
	}

}
