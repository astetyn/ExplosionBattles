package main.misc.airdrop;

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
import main.PlayerEB;
import main.configuration.WorldConfiguration;

public class AirDrop {

	Plane plane;
	DropBox dropBox;
	private int tickCounter = 0;
	private int dropTick;
	private boolean inProcess = false;
	
	public AirDrop() {
		
		inProcess = true;
		WorldConfiguration wc = new WorldConfiguration(Game.getInstance().getMap());
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
			if(cycle==500) {
				break;
			}
		}
		int height = checkArea.getBlockY();
		
		for(PlayerEB playerEB : Game.getInstance().getPlayers()) {
			playerEB.getPlayer().sendMessage("Zasoby prichadzaju, sledujte oblohu!");
		}
		
		plane = new Plane(locationInArena.getWorld(),x,z,x1,height,z1);
		
		dropTick = (int) (Math.random()*plane.getMaxTicks());
		
	}
	
	public void tick() {
		
		tickCounter++;
		
		if(tickCounter==dropTick) {
			dropBox = new DropBox(plane.getLocation());
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
		
		if(bonus.equals("weapon")) {
			Firework fw = b.getWorld().spawn(b.getLocation(), Firework.class);
			FireworkMeta fwm = fw.getFireworkMeta();
		    FireworkEffect effect = FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BALL).trail(true).build();
		    fwm.addEffect(effect);
		    fwm.setPower(0);
		    fw.setFireworkMeta(fwm);
			
			double gunCooldown = playerEB.getKit().getGunCooldown();
			playerEB.getKit().setGunCooldown(gunCooldown/2);
			playerEB.getPlayer().sendMessage("Airdrop zobraty! Rychlost nabijania sa ti zdvojnasobila!");
			
		}
	}

	public boolean isInProcess() {
		return inProcess;
	}

	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}
}
