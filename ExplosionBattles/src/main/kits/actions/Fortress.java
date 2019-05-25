package main.kits.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Ladder;

import main.MsgCenter;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;

public class Fortress {

	long lastUse = 0;
	long cooldown = 30*1000;
	private PlayerEB playerEB;
	
	private Material iron = Material.IRON_BLOCK;
	private Material stone = Material.STONE;
	private Material ladder = Material.LADDER;
	private Material air = Material.AIR;
	
	private Material fortressConstruction[][][] = {
	{{air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,stone,stone,stone,air,air},
	 {air,air,stone,ladder,stone,air,air},
	 {air,air,stone,air,stone,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air}
	},
	{{air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,iron,ladder,iron,air,air},
	 {air,air,iron,air,iron,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air}
	},
	{{air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,iron,ladder,iron,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air}
	},
	{{air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,iron,ladder,iron,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air}
	},
	{{air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,iron,ladder,iron,air,air},
	 {air,air,iron,iron,iron,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air}
	},
	{{air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,stone,iron,stone,air,air},
	 {air,air,iron,ladder,iron,air,air},
	 {air,air,stone,iron,stone,air,air},
	 {air,air,air,air,air,air,air},
	 {air,air,air,air,air,air,air}
	},
	{{air,air,air,air,air,air,air},
	 {air,stone,air,stone,air,stone,air},
	 {air,air,stone,iron,stone,air,air},
	 {air,stone,iron,ladder,iron,stone,air},
	 {air,air,stone,iron,stone,air,air},
	 {air,stone,air,stone,air,stone,air},
	 {air,air,air,air,air,air,air}
	},
	{{iron,stone,iron,stone,iron,stone,iron},
	 {stone,stone,stone,stone,stone,stone,stone},
	 {iron,stone,air,air,air,stone,iron},
	 {stone,stone,air,air,air,stone,stone},
	 {iron,stone,air,air,air,stone,iron},
	 {stone,stone,stone,stone,stone,stone,stone},
	 {iron,stone,iron,stone,iron,stone,iron}
	},
	{{iron,iron,iron,iron,iron,iron,iron},
	{iron,air,air,air,air,air,iron},
	{iron,air,air,air,air,air,iron},
	{iron,air,air,air,air,air,iron},
	{iron,air,air,air,air,air,iron},
	{iron,air,air,air,air,air,iron},
	{iron,iron,iron,iron,iron,iron,iron}
	}
	};
	
	public Fortress(PlayerEB playerEB, int inventorySlot) {
		this.playerEB = playerEB;
		ItemStack is = new ItemStack(Material.ANVIL,1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Build Fortress");
		is.setItemMeta(im);
		playerEB.getPlayer().getInventory().setItem(inventorySlot, is);
	}
	
	public boolean timePassed() {
		
		long time = System.currentTimeMillis();
		if(time<lastUse+cooldown) {
			double diff = time - lastUse;
			double seconds = diff / 1000;
			
			double waitSeconds = cooldown/1000-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Na ďalšiu pevnosť počkaj ešte "+ChatColor.YELLOW+ChatColor.BOLD+waitSeconds+ChatColor.GRAY+" sec...");
			return false;
		}
		lastUse = time;
		return true;
	}
	
	public void wantsToBuildFortress() {
		Player p = playerEB.getPlayer();
		Location loc = p.getLocation();
		int rotation = getRotation(loc);
		
		boolean b = isAreaClear(loc, rotation, fortressConstruction, 0);
		if(b) {
			if(timePassed()) {
				buildConstruction(loc, rotation, fortressConstruction, 0);
			}
		}else {
			p.sendMessage(MsgCenter.PREFIX+ChatColor.RED+"Málo miesta na pevnosť.");
		}
	}
	
	private byte getRotation(Location loc) {
		float yaw = (loc.getYaw());
		
		if(yaw<0) {
			yaw+=360;
		}
	
		if(yaw<45||yaw>315) {
			return 0;
		}else if(yaw<135) {
			return 1;
		}else if(yaw<225) {
			return 2;
		}else if(yaw<315) {
			return 3;
		}
		return -1;
	}
	
	private boolean isAreaClear(Location loc, int rotation, Material construction[][][], int offset) {
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		for(int i=0;i<construction.length;i++) {
			for(int j=0;j<construction[0].length;j++) {
				for(int k=0;k<construction[0][0].length;k++) {
					
					Material m = null;
					
					if(rotation==0){
						m = loc.getWorld().getBlockAt(x+construction[0][0].length/2-k,y+i,z+construction[0].length-j+offset).getType();
					}else if(rotation==1){
						m = loc.getWorld().getBlockAt(x-offset-construction[0].length+j,y+i,z+construction[0][0].length/2-k).getType();
					}else if(rotation==2){
						m = loc.getWorld().getBlockAt(x-construction[0][0].length/2+k,y+i,z-offset-construction[0].length+j).getType();
					}else if(rotation==3){
						m = loc.getWorld().getBlockAt(x+offset+construction[0].length-j,y+i,z-construction[0][0].length/2+k).getType();
					}
					
					if(m==Material.BARRIER||m==Material.BEDROCK) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void buildConstruction(Location loc, int rotation, Material construction[][][], int offset) {
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		int y = loc.getBlockY();
		for(int i=0;i<construction.length;i++) {
			for(int j=0;j<construction[0].length;j++) {
				for(int k=0;k<construction[0][0].length;k++) {
					
					Block b = null;
					
					if(rotation==0){
						b = loc.getWorld().getBlockAt(x+construction[0][0].length/2-k,y+i,z+construction[0].length-j+offset);
						b.setType(construction[i][j][k]);
						if(b.getType()==Material.LADDER) {
							BlockState state = b.getState();
							Ladder l = (Ladder) b.getState().getData();
							l.setFacingDirection(BlockFace.SOUTH);
							state.setData(l);
							state.update(false,false);
						}
					}else if(rotation==1){
						b = loc.getWorld().getBlockAt(x-offset-construction[0].length+j,y+i,z+construction[0][0].length/2-k);
						b.setType(construction[i][j][k]);
						if(b.getType()==Material.LADDER) {
							BlockState state = b.getState();
							Ladder l = (Ladder) b.getState().getData();
							l.setFacingDirection(BlockFace.WEST);
							state.setData(l);
							state.update(false,false);
						}
					}else if(rotation==2){
						b = loc.getWorld().getBlockAt(x-construction[0][0].length/2+k,y+i,z-offset-construction[0].length+j);
						b.setType(construction[i][j][k]);
						if(b.getType()==Material.LADDER) {
							BlockState state = b.getState();
							Ladder l = (Ladder) b.getState().getData();
							l.setFacingDirection(BlockFace.NORTH);
							state.setData(l);
							state.update(false,false);
						}
					}else if(rotation==3){
						b = loc.getWorld().getBlockAt(x+offset+construction[0].length-j,y+i,z-construction[0][0].length/2+k);
						b.setType(construction[i][j][k]);
						if(b.getType()==Material.LADDER) {
							BlockState state = b.getState();
							Ladder l = (Ladder) b.getState().getData();
							l.setFacingDirection(BlockFace.EAST);
							state.setData(l);
							state.update(false,false);
						}
					}
				}
			}
		}
	}

}
