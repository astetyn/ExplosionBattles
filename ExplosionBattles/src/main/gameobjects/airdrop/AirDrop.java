package main.gameobjects.airdrop;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.MetadataValue;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.configuration.MapConfiguration;
import main.player.PlayerEB;
import main.weapons.HeavyExplosiveSniper;
import net.md_5.bungee.api.ChatColor;

public class AirDrop {

	Plane plane;
	DropBox dropBox;
	private int tickCounter = 0;
	private int dropTick;
	private boolean inProcess = false;
	private String[] bonuses = {"weaponcooldown","heavysniper"};
	
	public AirDrop() {
		
		inProcess = true;
		MapConfiguration wc = new MapConfiguration(Game.getInstance().getMap());
		Location locationInArena = (Location) wc.getConfig().get("loc1");
		Location locationInArena2 = (Location) wc.getConfig().get("loc2");
		
		int x1 = locationInArena.getBlockX();
		int x2 = locationInArena2.getBlockX();
		
		int z1 = locationInArena.getBlockZ();
		int z2 = locationInArena2.getBlockZ();
		
		int x = (x1-x2)*-1;
		int z = (z1-z2)*-1;
		
		Location checkArea = locationInArena;
		Block b = checkArea.getBlock();
		int cycle = 0;
		while(b.getType()!=Material.BARRIER) {
			checkArea.add(new Location(checkArea.getWorld(),0,1,0));
			b = checkArea.getBlock();
			cycle++;
			if(cycle==200) {
				break;
			}
		}
		int height = checkArea.getBlockY();
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+MsgCenter.ALLERT+ChatColor.WHITE+"Lietadlo prichádza, čoskoro zhodí balík! "+MsgCenter.ALLERT);
		}
		
		Location startLoc = new Location(locationInArena.getWorld(),x1,height,z1);
		
		plane = new Plane(x,z,startLoc);
		dropTick = (int) (Math.random()*plane.getMaxTicks());
		
	}
	
	public void tick() {
		
		tickCounter++;
		
		if(tickCounter==dropTick) {
			dropBox = new DropBox(plane.getLocation().clone(),bonuses);
		}
		
		if(dropBox!=null) {
			
			if(!plane.isPlaneExists()&&!dropBox.isBoxExists()) {
				inProcess = false;
			}
			
			if(dropBox.isBoxExists()) {
				dropBox.tick();
			}
		}
		
		if(plane.isPlaneExists()) {
			plane.tick();
		}
	}
	
	public void onInteractBlock(PlayerEB playerEB,Block b) {
		
		if(!b.hasMetadata("airdrop")) {
			return;
		}
		
		MetadataValue mv = b.getMetadata("airdrop").get(0);
		String bonus = mv.asString();
		
		b.setType(Material.AIR);
		b.removeMetadata("airdrop", Main.getPlugin());
		
		Firework fw = b.getWorld().spawn(b.getLocation(), Firework.class);
	    FireworkEffect effect = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.GREEN).with(FireworkEffect.Type.BALL).build();
	    FireworkMeta fwm = fw.getFireworkMeta();
	    fwm.clearEffects();
	    fwm.addEffect(effect);
	    fwm.setPower(1);
	    fw.setFireworkMeta(fwm);
		
	    for(PlayerEB pEB : Game.getInstance().getPlayers()) {
	    	pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" našiel AirDrop.");
	    }
	    
		if(bonus.equals("weaponcooldown")) {
			double gunCooldown = playerEB.getWeapon().getCooldown();
			playerEB.getWeapon().setCooldown(gunCooldown/2);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+ChatColor.BOLD+"AIRDROP zobratý!"+ChatColor.GOLD+ChatColor.ITALIC+" Našiel si zásobník na rýchle nabíjanie!");
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.GOLD+"Rýchlosť nabíjania zdvojnásobená.");
		}else if(bonus.equals("heavysniper")) {
			playerEB.setWeapon(new HeavyExplosiveSniper(playerEB));
			playerEB.getWeapon().equip();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+ChatColor.BOLD+"AIRDROP zobratý!"+ChatColor.GOLD+" Našiel si silnú Heavy Sniper!");
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.RED+ChatColor.BOLD+"Heavy Explosive Sniper"+ChatColor.GOLD+" pridaná ako hlavná zbraň.");
		}
		playerEB.getUserAccount().addCoinsWithNotification(5);
	}

	public boolean isInProcess() {
		return inProcess;
	}

	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}
}
