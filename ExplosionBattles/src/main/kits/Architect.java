package main.kits;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Stairs;
import org.bukkit.metadata.FixedMetadataValue;

import main.Main;
import net.md_5.bungee.api.ChatColor;

public class Architect extends Kit{

	private final String name = "Architect";
	private long lastFire = 0;
	private long lastBuild = 0;
	private double cooldownBuild = 0.5;
	private final double gunCooldown = 6;
	
	public Architect() {
		setGunCooldown(gunCooldown);
		setName(name);
	}

	@Override
	public void startInit() {
		ItemStack is = new ItemStack(Material.BLAZE_ROD,1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GRAY+""+ChatColor.BOLD+"Sniper Blaze");
		is.setItemMeta(im);
		getPlayerEB().getPlayer().getInventory().setItem(0, is);
		ItemStack is2 = new ItemStack(Material.IRON_AXE,1);
		ItemMeta im2 = is2.getItemMeta();
		im2.setDisplayName("Stairs");
		is2.setItemMeta(im2);
		getPlayerEB().getPlayer().getInventory().setItem(1, is2);
		ItemStack is3 = new ItemStack(Material.IRON_PICKAXE,1);
		ItemMeta im3 = is3.getItemMeta();
		im3.setDisplayName("Wall");
		is3.setItemMeta(im3);
		getPlayerEB().getPlayer().getInventory().setItem(2, is3);
		ItemStack is4 = new ItemStack(Material.IRON_SPADE,1);
		ItemMeta im4 = is4.getItemMeta();
		im4.setDisplayName("Platform");
		is4.setItemMeta(im4);
		getPlayerEB().getPlayer().getInventory().setItem(3, is4);
	}

	@Override
	public void onInteract(ItemStack it) {
		
		if(it.getType()==Material.IRON_AXE||it.getType()==Material.IRON_PICKAXE||it.getType()==Material.IRON_SPADE) {
			long actualTime = System.currentTimeMillis();
			double diff = actualTime - lastBuild;
			double seconds = diff / 1000;
			if(seconds<cooldownBuild) {
				playerEB.getPlayer().sendMessage("Stavas moc rychlo.");
				return;
			}
			lastBuild = System.currentTimeMillis();
		}
		
		if(it.getType()==Material.BLAZE_ROD) {
			wantsToFire();
		}else if(it.getType()==Material.IRON_AXE) {
			boolean b = wantsToBuildStairs();
			if(!b) {
				playerEB.getPlayer().sendMessage("Na schody nie je dost miesta.");
			}
		}else if(it.getType()==Material.IRON_PICKAXE) {
			boolean b = wantsToBuildWall();
			if(!b) {
				playerEB.getPlayer().sendMessage("Na stenu nie je dost miesta.");
			}
		}else if(it.getType()==Material.IRON_SPADE) {
			boolean b = wantsToBuildPlatform();
			if(!b) {
				playerEB.getPlayer().sendMessage("Na platformu nie je dost miesta.");
			}
		}
	}
	
	private void wantsToFire() {
		long actualTime = System.currentTimeMillis();
		double diff = actualTime - lastFire;
		double seconds = diff / 1000;
		if(seconds<getGunCooldown()) {
			
		    double waitSeconds = getGunCooldown()-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
			playerEB.getPlayer().sendMessage("Na dalsiu strelu pockaj este "+waitSeconds+" sec.");
			return;
		}
		lastFire = System.currentTimeMillis();
		fireFireball();
	}
	
	public void fireFireball() {
		Player p = getPlayerEB().getPlayer();
		Fireball fb = p.launchProjectile(Fireball.class);
		fb.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
		fb.setMetadata("fireball", new FixedMetadataValue(Main.getPlugin(), p.getName()));
	}
	
	public boolean wantsToBuildPlatform() {
		
		Player p = getPlayerEB().getPlayer();
		Location loc = p.getLocation();
		World world = loc.getWorld();
		float yaw = (loc.getYaw());
		if(yaw<0) {
			yaw+=360;
		}
		
		if(yaw<45||yaw>315) {
			Location l = new Location(world,loc.getX()+2,loc.getY(),loc.getZ()+1);
			if(checkAreaPlatform(l,false,true)) {
				buildPlatform(l,false,true);
				return true;
			}
			return false;
		}else if(yaw<135) {
			Location l = new Location(world,loc.getX()-1,loc.getY(),loc.getZ()+2);
			if(checkAreaPlatform(l,false,false)==true) {
				buildPlatform(l,false,false);
				return true;
			}
			return false;
		}else if(yaw<225) {
			Location l = new Location(world,loc.getX()-2,loc.getY(),loc.getZ()-1);
			if(checkAreaPlatform(l,true,false)==true) {
				buildPlatform(l,true,false);
				return true;
			}
			return false;
		}else if(yaw<315) {
			Location l = new Location(world,loc.getX()+1,loc.getY(),loc.getZ()-2);
			if(checkAreaPlatform(l,true,true)==true) {
				buildPlatform(l,true,true);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean checkAreaPlatform(Location loc, boolean addX, boolean addZ) {
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		if(addX==true) {
			if(addZ==true) {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x+i,y-1,z+j).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x+i,y-1,z+j).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}else {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x+j,y-1,z-i).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x+j,y-1,z-i).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}
			
		}else {
			if(addZ==true) {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x-j,y-1,z+i).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x-j,y-1,z+i).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}else {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x-i,y-1,z-j).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x-i,y-1,z-j).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}
		}
	}
	
	public void buildPlatform(Location loc, boolean addX, boolean addZ) {
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		if(addX==true) {
			if(addZ==true) {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x+i,y-1,z+j);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}
			}else {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x+j,y-1,z-i);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}
			}
			
		}else {
			if(addZ==true) {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x-j,y-1,z+i);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}

			}else {
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x-i,y-1,z-j);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}
			}
		}
	}
	
	public boolean wantsToBuildWall() {
		Player p = getPlayerEB().getPlayer();
		Location loc = p.getLocation();
		World world = loc.getWorld();
		float yaw = (loc.getYaw());
		if(yaw<0) {
			yaw+=360;
		}
		
		if(yaw<45||yaw>315) {
			Location l = new Location(world,loc.getX()+2,loc.getY(),loc.getZ()+2);
			if(checkAreaWall(l,false,true)) {
				buildWall(l,false,true);
				return true;
			}
			return false;
		}else if(yaw<135) {
			Location l = new Location(world,loc.getX()-2,loc.getY(),loc.getZ()+2);
			if(checkAreaWall(l,false,false)==true) {
				buildWall(l,false,false);
				return true;
			}
			return false;
		}else if(yaw<225) {
			Location l = new Location(world,loc.getX()-2,loc.getY(),loc.getZ()-2);
			if(checkAreaWall(l,true,false)==true) {
				buildWall(l,true,false);
				return true;
			}
			return false;
		}else if(yaw<315) {
			Location l = new Location(world,loc.getX()+2,loc.getY(),loc.getZ()-2);
			if(checkAreaWall(l,true,true)==true) {
				buildWall(l,true,true);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean checkAreaWall(Location loc, boolean addX, boolean addZ) {
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		if(addX==true) {
			if(addZ==true) {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x,y+i,z+j).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x,y+i,z+j).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}else {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x+j,y+i,z).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x+j,y+i,z).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}
			
		}else {
			if(addZ==true) {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x-j,y+i,z).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x-j,y+i,z).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}else {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						if(loc.getWorld().getBlockAt(x,y+i,z-j).getType().equals(Material.BEDROCK)||loc.getWorld().getBlockAt(x,y+i,z-j).getType().equals(Material.BARRIER)) {
							return false;
						}
					}
				}
				return true;
			}
		}
	}
	
	public void buildWall(Location loc, boolean addX, boolean addZ) {
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		if(addX==true) {
			if(addZ==true) {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x,y+i,z+j);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}
			}else {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x+j,y+i,z);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}
			}
			
		}else {
			if(addZ==true) {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x-j,y+i,z);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}

			}else {
				for(int i=0;i<3;i++) {
					for(int j=0;j<5;j++) {
						Block b = loc.getWorld().getBlockAt(x,y+i,z-j);
						b.setType(Material.QUARTZ_BLOCK);
					}
				}
			}
		}
	}
	
	public boolean wantsToBuildStairs() {
		Player p = getPlayerEB().getPlayer();
		Location miesto = p.getLocation();
		World svet = miesto.getWorld();
		float yaw = (miesto.getYaw());
		if(yaw<0) {
			yaw+=360;
		}
		
		if(yaw<45||yaw>315) {
			Location l = new Location(svet,miesto.getX()+2,miesto.getY(),miesto.getZ()+2);
			
			if(checkAreaStairs(l,false,true)) {
				buildStairs(l,false,true);
				return true;
			}else {
				return false;
			}
		}else if(yaw<135) {
			Location l = new Location(svet,miesto.getX()-2,miesto.getY(),miesto.getZ()+2);
			
			if(checkAreaStairs(l,false,false)==true) {
				buildStairs(l,false,false);
				return true;
			}else {
				return false;
			}
		}else if(yaw<225) {
			Location l = new Location(svet,miesto.getX()-2,miesto.getY(),miesto.getZ()-2);
			
			if(checkAreaStairs(l,true,false)==true) {
				buildStairs(l,true,false);
				return true;
			}else {
				return false;
			}
		}else if(yaw<315) {
			Location l = new Location(svet,miesto.getX()+2,miesto.getY(),miesto.getZ()-2);
			
			if(checkAreaStairs(l,true,true)==true) {
				buildStairs(l,true,true);
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	private void buildStairs(Location start,boolean pridavajX,boolean pridavajZ) {
		int x = start.getBlockX();
		int z = start.getBlockZ();
		int y = start.getBlockY();
		if(pridavajX==true) {
			if(pridavajZ==true) {
				//PRIDAVA SA X Z
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						Block b = start.getWorld().getBlockAt(x+h,y+h,z+i);
						b.setType(Material.QUARTZ_STAIRS);
						BlockState state = b.getState();
						Stairs s = (Stairs) b.getState().getData();
						s.setFacingDirection(BlockFace.EAST);
						state.setData(s);
						state.update(false,false);
					}
				}
			}else {
				//PRIDAVA SA X
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						Block b = start.getWorld().getBlockAt(x+i,y+h,z-h);
						b.setType(Material.QUARTZ_STAIRS);
						BlockState state = b.getState();
						Stairs s = (Stairs) b.getState().getData();
						s.setFacingDirection(BlockFace.NORTH);
						state.setData(s);
						state.update(false,false);
					}
				}
			}
			
		}else {
			if(pridavajZ==true) {
				//PRIDAVA SA Z
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						Block b = start.getWorld().getBlockAt(x-i,y+h,z+h);
						b.setType(Material.QUARTZ_STAIRS);
						BlockState state = b.getState();
						Stairs s = (Stairs) b.getState().getData();
						s.setFacingDirection(BlockFace.SOUTH);
						state.setData(s);
						state.update(false,false);
					}
				}

			}else {
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						Block b = start.getWorld().getBlockAt(x-h,y+h,z-i);
						b.setType(Material.QUARTZ_STAIRS);
						BlockState state = b.getState();
						Stairs s = (Stairs) b.getState().getData();
						s.setFacingDirection(BlockFace.WEST);
						state.setData(s);
						state.update(false,false);
					}
				}
			}
		}
	}
	
	private boolean checkAreaStairs(Location start,boolean pridavajX,boolean pridavajZ) {
		int x = start.getBlockX();
		int z = start.getBlockZ();
		int y = start.getBlockY();
		if(pridavajX==true) {
			if(pridavajZ==true) {
				//PRIDAVA SA X Z
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						for(int j=0;j<5;j++) {
							if(start.getWorld().getBlockAt(x+i,y+h,z+j).getType().equals(Material.BEDROCK)||start.getWorld().getBlockAt(x+i,y+h,z+j).getType().equals(Material.BARRIER)) {
								return false;
							}
						}
					}
				}
				return true;
			}else {
				//PRIDAVA SA X
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						for(int j=0;j<5;j++) {
							if(start.getWorld().getBlockAt(x+i,y+h,z-j).getType().equals(Material.BEDROCK)||start.getWorld().getBlockAt(x+i,y+h,z-j).getType().equals(Material.BARRIER)) {
								return false;
							}
						}
					}
				}
				return true;
			}
			
		}else {
			if(pridavajZ==true) {
				//PRIDAVA SA Z
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						for(int j=0;j<5;j++) {
							if(start.getWorld().getBlockAt(x-i,y+h,z+j).getType().equals(Material.BEDROCK)||start.getWorld().getBlockAt(x-i,y+h,z+j).getType().equals(Material.BARRIER)) {
								return false;
							}
						}
					}
				}
				return true;
			}else {
				for(int h=0;h<5;h++) {
					for(int i=0;i<5;i++) {
						for(int j=0;j<5;j++) {
							if(start.getWorld().getBlockAt(x-i,y+h,z-j).getType().equals(Material.BEDROCK)||start.getWorld().getBlockAt(x-i,y+h,z-j).getType().equals(Material.BARRIER)) {
								return false;
							}
						}
					}
				}
				return true;
			}
		}
	}
	
	@Override
	public ItemStack getChooseItem() {
		ItemStack item = new ItemStack(Material.QUARTZ_STAIRS,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.GOLD+"Build your own structures.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Architect");
		item.setItemMeta(im);
		return item;
	}
	
}
