package main.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class LocationS implements ConfigurationSerializable {
	
	private String world;
	private double x,y,z;
	
	public LocationS(String world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("world", world);
		result.put("x",x);
		result.put("y", y);
		result.put("z", z);
		return result;
	}
	
	public static LocationS deserialize(Map<String, Object> args) {
		
		String world = "";
		double x=0,y=0,z=0;
		
		if(args.containsKey("world")) {
            world = (String) args.get("world");
        }
		
		if(args.containsKey("x")) {
            x = ((Double)args.get("x")).doubleValue();
        }

        if(args.containsKey("y")) {
            y = ((Double)args.get("y")).doubleValue();
        }

        if(args.containsKey("z")) {
            z = ((Double)args.get("z")).doubleValue();
        }
        
		return new LocationS(world,x,y,z);
	}
	
	
}
