package main.gameobjects;

import org.bukkit.Bukkit;
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
import main.gameobjects.planes.SupplyPlane;
import main.player.PlayerEB;
import main.player.consumables.MedKit;
import main.utils.LocationS;
import main.weapons.HeavyExplosiveSniper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SupplyPackage extends GameObject {

	private SupplyPlane plane;
	private SupplyBox supplyBox;
	private int ticks = 0;
	private int dropTick;
	private boolean active = false;
	private boolean boxCollected = false;
	private String[] bonuses = {"weaponcooldown","heavysniper","medkits"};
	
	public SupplyPackage() {
		
		active = true;
		MapConfiguration wc = new MapConfiguration(Game.getInstance().getMap());
		LocationS lia = (LocationS) wc.getConfig().get("loc1");
		LocationS lia2 = (LocationS) wc.getConfig().get("loc2");
		
		Location locationInArena = new Location(Bukkit.getWorld(lia.getWorld()),lia.getX(),lia.getY(),lia.getZ());
		Location locationInArena2 = new Location(Bukkit.getWorld(lia2.getWorld()),lia2.getX(),lia2.getY(),lia2.getZ());
		
		int x1 = locationInArena.getBlockX();
		int x2 = locationInArena2.getBlockX();
		
		int z1 = locationInArena.getBlockZ();
		int z2 = locationInArena2.getBlockZ();
		
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
			String message = MsgCenter.ALLERT+ChatColor.WHITE+"Lietadlo prichádza, čoskoro zhodí balík! "+MsgCenter.ALLERT;
			playerEB.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
		}
		
		Location startLoc = new Location(locationInArena.getWorld(), x1, height, z1);
		Location endLoc = new Location(locationInArena2.getWorld(), x2, height, z2);
		
		plane = new SupplyPlane(startLoc, endLoc);
		dropTick = (int) (Math.random()*plane.getMaxTicks());
		if(dropTick==0) {
			dropTick = 1;
		}
		
	}
	
	public void tick() {
		
		ticks++;
		
		if(ticks%20!=0) {
			return;
		}
		
		if(boxCollected&&!plane.isActive()) {
			active = false;
		}
		
		if(ticks/20==dropTick) {
			supplyBox = new SupplyBox(plane.getMidLocation().clone(),bonuses);
		}
		
		if(supplyBox!=null) {
			if(supplyBox.isBoxFalling()) {
				supplyBox.tick();
			}
		}
		
		if(plane.isActive()) {
			plane.tick();
		}
	}
	
	@Override
	public void onInteractBlock(PlayerEB playerEB, Block b) {
		
		if(!b.hasMetadata("supply")) {
			return;
		}
		
		if(b.getLocation().equals(supplyBox.getBoxLocation())) {
			boxCollected = true;
		}
		
		MetadataValue mv = b.getMetadata("supply").get(0);
		String bonus = mv.asString();
		
		b.setType(Material.AIR);
		b.removeMetadata("supply", Main.getPlugin());
		
		Firework fw = b.getWorld().spawn(b.getLocation(), Firework.class);
	    FireworkEffect effect = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.GREEN).with(FireworkEffect.Type.BALL).build();
	    FireworkMeta fwm = fw.getFireworkMeta();
	    fwm.clearEffects();
	    fwm.addEffect(effect);
	    fwm.setPower(1);
	    fw.setFireworkMeta(fwm);
		
	    for(PlayerEB pEB : Game.getInstance().getPlayers()) {
	    	pEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+playerEB.getPlayer().getName()+ChatColor.GRAY+" našiel SupplyPackage.");
	    }
	    
	    playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.BOLD+ChatColor.GRAY+"--------------"+ChatColor.YELLOW+ChatColor.BOLD+
	    		"Supply Package"+ChatColor.GRAY+ChatColor.BOLD+"--------------");
	    
		if(bonus.equals("weaponcooldown")) {
			double gunCooldown = playerEB.getWeapon().getCooldown();
			playerEB.getWeapon().setCooldown(gunCooldown/2);
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.BLUE+ChatColor.ITALIC+"Našiel si zásobník s rýchlym nabíjaním!");
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"Rýchlosť nabíjania zdvojnásobená.");
		}else if(bonus.equals("heavysniper")) {
			playerEB.setWeapon(new HeavyExplosiveSniper(playerEB));
			playerEB.getWeapon().equip();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.BLUE+ChatColor.ITALIC+"Našiel si silnú Heavy Sniper!");
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.RED+ChatColor.BOLD+"Heavy Explosive Sniper"+ChatColor.YELLOW+" pridaná ako hlavná zbraň.");
		}else if(bonus.equals("medkits")) {
			playerEB.getConsumablesManager().addBoughtItem(new MedKit(playerEB), 3);
			playerEB.getConsumablesManager().addAllToInventory();
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.BLUE+ChatColor.ITALIC+"Našiel si stratené lekárničky!");
			playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.YELLOW+"+3 Lekárničky.");
		}
		 playerEB.getPlayer().sendMessage(MsgCenter.PREFIX+ChatColor.BOLD+ChatColor.GRAY+"----------------------------------------------");
		playerEB.getUserAccount().addCoinsWithNotification(5);
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
