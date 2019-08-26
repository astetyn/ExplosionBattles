package main.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Stairs;

import main.MsgCenter;
import main.player.PlayerEB;
import main.weapons.MiniGun;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ArchitectKit extends Kit {
	
	private long lastBuild = 0;
	private final double cooldownBuild = 0.5;
	private List<ItemStack> tools = new ArrayList<ItemStack>();
	
	public ArchitectKit() {
		super(new MiniGun());
	}
	
	public ArchitectKit(PlayerEB playerEB) {
		super(playerEB, new MiniGun());
	}

	@Override
	public void startInit() {
		
		ItemStack is = new ItemStack(Material.WOOD_PICKAXE,1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Schody");
		is.setItemMeta(im);
		getPlayerEB().getPlayer().getInventory().setItem(1, is);
		tools.add(is);
		
		ItemStack is2 = new ItemStack(Material.WOOD_HOE,1);
		ItemMeta im2 = is2.getItemMeta();
		im2.setDisplayName("Stena");
		is2.setItemMeta(im2);
		getPlayerEB().getPlayer().getInventory().setItem(2, is2);
		tools.add(is2);
		
		ItemStack is3 = new ItemStack(Material.WOOD_SPADE,1);
		ItemMeta im3 = is3.getItemMeta();
		im3.setDisplayName("Platforma");
		is3.setItemMeta(im3);
		getPlayerEB().getPlayer().getInventory().setItem(3, is3);
		tools.add(is3);
	}

	@Override
	public void onInteract(ItemStack is) {
		
		for(ItemStack tool : tools) {
			if(is.equals(tool)) {
				long actualTime = System.currentTimeMillis();
				double diff = actualTime - lastBuild;
				double seconds = diff / 1000;
				if(seconds<cooldownBuild) {
					String message = MsgCenter.PREFIX+ChatColor.GRAY+"Staviaš veľmi rýchlo!";
					getPlayerEB().getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					return;
				}
				lastBuild = System.currentTimeMillis();
			
			
				if(is.getType()==Material.WOOD_PICKAXE) {
					boolean b = wantsToBuildStairs();
					if(!b) {
						getPlayerEB().getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Na schody nie je dosť miesta.");
					}
				}else if(is.getType()==Material.WOOD_HOE) {
					boolean b = wantsToBuildWall();
					if(!b) {
						getPlayerEB().getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Na stenu nie je dosť miesta.");
					}
				}else if(is.getType()==Material.WOOD_SPADE) {
					boolean b = wantsToBuildPlatform();
					if(!b) {
						getPlayerEB().getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Na platformu nie je dosť miesta.");
					}
				}	
			}
		}
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
		Location loc = p.getLocation();
		World world = loc.getWorld();
		float yaw = (loc.getYaw());
		if(yaw<0) {
			yaw+=360;
		}
		
		if(yaw<45||yaw>315) {
			Location l = new Location(world,loc.getX()+2,loc.getY(),loc.getZ()+1);
			
			if(checkAreaStairs(l,false,true)) {
				buildStairs(l,false,true);
				return true;
			}else {
				return false;
			}
		}else if(yaw<135) {
			Location l = new Location(world,loc.getX()-1,loc.getY(),loc.getZ()+2);
			
			if(checkAreaStairs(l,false,false)==true) {
				buildStairs(l,false,false);
				return true;
			}else {
				return false;
			}
		}else if(yaw<225) {
			Location l = new Location(world,loc.getX()-2,loc.getY(),loc.getZ()-1);
			
			if(checkAreaStairs(l,true,false)==true) {
				buildStairs(l,true,false);
				return true;
			}else {
				return false;
			}
		}else if(yaw<315) {
			Location l = new Location(world,loc.getX()+1,loc.getY(),loc.getZ()-2);
			
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
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.QUARTZ_STAIRS,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : getIndex().toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Stavaj podľa seba!");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeapon().getItem().getItemMeta().getDisplayName());
		l.add(ChatColor.AQUA+"Možnosť stavať schody, steny a plošiny.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Architect");
		item.setItemMeta(im);
		return item;
	}

	@Override
	public String getIndex() {
		return "kit_architect";
	}

	@Override
	public int getPrice() {
		return 1800;
	}

	@Override
	public void onTick() {}

	@Override
	public boolean isAlive() {
		return false;
	}
}
