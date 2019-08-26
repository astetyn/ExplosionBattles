package main.weapons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import main.Game;
import main.Main;
import main.MsgCenter;
import main.player.GameStage;
import main.player.PlayerEB;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SnapdragonGun extends Weapon {

	private WitherSkull rocket;
	private int ticks = 0;
	private Player targetPlayer;
	
	public SnapdragonGun() {
		super(20, 40, "Ultra", "Vystreľ raketu pri hráčovi.");
	}
	
	public SnapdragonGun(PlayerEB playerEB) {
		super(playerEB, 20, 40, "Ultra", "Vystreľ raketu pri hráčovi.");
	}

	@Override
	public boolean onInteract(PlayerInteractEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
		if(is.equals(getItem())) {
			wantsToFire(getPlayerEB());
		}
		return true;
	}
	
	@Override
	public void onTick() {
		ticks++;
		
		if(ticks%10!=0) {
			return;
		}
		recalculateRocketTrajectory();
	}
	
	private void recalculateRocketTrajectory() {
		if(rocket==null) {
			return;
		}
		if(rocket.isDead()) {
			return;
		}
		
		Vector sourceVec = rocket.getLocation().toVector();
		Vector targetVec = targetPlayer.getLocation().toVector();
		
		Vector direction = targetVec.subtract(sourceVec);
		direction.normalize();
		rocket.setVelocity(direction);
	}
	
	private void wantsToFire(PlayerEB playerEB) {
		long actualTime = System.currentTimeMillis();
		double diff = actualTime - getLastUse();
		double seconds = diff / 1000;
		if(seconds<getCooldown()) {
			
		    double waitSeconds = getCooldown()-seconds;
		    BigDecimal bd = new BigDecimal(Double.toString(waitSeconds));
		    bd = bd.setScale(1, RoundingMode.CEILING);
		    waitSeconds = bd.doubleValue();
		    String message = ChatColor.GRAY+"Nabíjanie ešte "+ChatColor.YELLOW+ChatColor.BOLD+waitSeconds+ChatColor.GRAY+" sec...";
			playerEB.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
			return;
		}
		setLastUse(System.currentTimeMillis());
		fireRocket(playerEB);
	}
	
	private void fireRocket(PlayerEB playerEB) {
		Player p = playerEB.getPlayer();
		rocket = p.launchProjectile(WitherSkull.class);
		rocket.setMetadata("snapdragon", new FixedMetadataValue(Main.getPlugin(), p.getName()));
		rocket.setGravity(false);
		
		Location source = rocket.getLocation();
		double MIN_DISTANCE = Double.MAX_VALUE;
		for(PlayerEB peb : Game.getInstance().getPlayersInGame()) {
			if(peb.getGameStage()!=GameStage.GAME_RUNNING) {
				continue;
			}
			if(peb == getPlayerEB()) {
				continue;
			}
			Location pLoc = peb.getPlayer().getLocation();
			double distance = pLoc.distanceSquared(source);
			if(distance<MIN_DISTANCE) {
				MIN_DISTANCE = distance;
				targetPlayer = peb.getPlayer();
			}
		}
		if(targetPlayer==null) {
			targetPlayer = playerEB.getPlayer();
		}
		p.sendMessage(MsgCenter.PREFIX+ChatColor.GRAY+"Cieľ "+ChatColor.GOLD+targetPlayer.getName()+ChatColor.GRAY+" zameraný, raketa vypustená.");
		recalculateRocketTrajectory();
	}

	@Override
	public String getIndex() {
		return "weapon_snapdragongun";
	}

	@Override
	public int getPrice() {
		return 1500;
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(Material.DIAMOND_AXE,1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : getIndex().toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.WHITE+"Nabíjanie: "+ ChatColor.GOLD + getCooldown());
		l.add(ChatColor.WHITE+"Sila: "+ ChatColor.GOLD + getPower());
		l.add(ChatColor.WHITE+"Presnosť: "+ ChatColor.GOLD + getAccuracy());
		im.setLore(l);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		im.setUnbreakable(true);
		im.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Snapdragon Gun");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

}
