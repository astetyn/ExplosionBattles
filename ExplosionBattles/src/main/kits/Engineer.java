package main.kits;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Ladder;

import main.kits.actions.BombAssault;
import main.player.PlayerEB;
import main.weapons.MiniGun;
import main.weapons.Weapon;
import net.md_5.bungee.api.ChatColor;

public class Engineer extends Kit {
	
	private Weapon weapon = new MiniGun(null);
	private BombAssault bombAssault;
	private final int price = 10000;
	private final boolean avaibleForVip = false;
	private final String index = "kit_engineer";
	
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
	
	public Engineer(PlayerEB playerEB) {
		setPlayer(playerEB);
		setDefaultWeapon(weapon);
	}

	@Override
	public void startInit() {
		bombAssault = new BombAssault(getPlayerEB(),2);
		if(getPlayerEB().getWeapon()==null) {
			weapon.setPlayerEB(getPlayerEB());
			getPlayerEB().setWeapon(weapon);
		}else if(!getPlayerEB().getWeapon().isSetThisRound()){
			weapon.setPlayerEB(getPlayerEB());
			getPlayerEB().setWeapon(weapon);
		}
		ItemStack is = new ItemStack(Material.ANVIL,1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Build Fortress");
		is.setItemMeta(im);
		getPlayerEB().getPlayer().getInventory().setItem(1, is);
	}
	
	@Override
	public void onInteract(ItemStack is) {
		if(is.getType()==Material.ANVIL) {
			wantsToBuildFortress();
		}else if(is.getType()==Material.NETHER_STAR) {
			bombAssault.wantsToCallAssault();
		}
	}
	
	@Override
	public void tick() {
		if(bombAssault!=null) {
			bombAssault.tick();
		}
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ANVIL,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		l.add(ChatColor.WHITE+"Mechanics will help you.");
		l.add(ChatColor.WHITE+"Obsahuje zbran "+weapon.getItem().getItemMeta().getDisplayName());
		l.add("Moznost stavat pevnosti a zavolat bombardovanie.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Engineer");
		item.setItemMeta(im);
		return item;
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
	
	private void wantsToBuildFortress() {
		
		Player p = getPlayerEB().getPlayer();
		Location loc = p.getLocation();
		int rotation = getRotation(loc);
		
		boolean b = isAreaClear(loc, rotation, fortressConstruction, 0);
		if(b) {
			buildConstruction(loc, rotation, fortressConstruction, 0);
		}else {
			p.sendMessage("Malo miesta na pevnost.");
		}
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

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public boolean isAvaibleForVip() {
		return avaibleForVip;
	}

	@Override
	public String getIndex() {
		return index;
	}
}
