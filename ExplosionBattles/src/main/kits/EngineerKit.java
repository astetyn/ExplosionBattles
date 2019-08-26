package main.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.kits.actions.BombAssault;
import main.kits.actions.Fortress;
import main.kits.actions.JumpBoost;
import main.player.PlayerEB;
import main.weapons.SnapdragonGun;
import net.md_5.bungee.api.ChatColor;

public class EngineerKit extends Kit {
	
	private BombAssault bombAssault;
	private Fortress fortress;
	private JumpBoost jumpBoost;
	
	public EngineerKit() {
		super(new SnapdragonGun());
	}
	
	public EngineerKit(PlayerEB playerEB) {
		super(playerEB, new SnapdragonGun());
	}

	@Override
	public void startInit() {
		fortress = new Fortress(getPlayerEB(), 1);
		bombAssault = new BombAssault(getPlayerEB(),2);
		jumpBoost = new JumpBoost(getPlayerEB(),8);
	}
	
	@Override
	public void onInteract(ItemStack is) {
		if(is.getType()==Material.ANVIL) {
			fortress.wantsToBuildFortress();
		}else if(is.getType()==Material.NETHER_STAR) {
			bombAssault.wantsToCallAssault();
		}else if(is.getType()==Material.GLASS_BOTTLE) {
			jumpBoost.wantsToUse();
		}
	}
	
	@Override
	public void onTick() {
		if(bombAssault!=null) {
			bombAssault.tick();
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.ANVIL,1);
		ItemMeta im = item.getItemMeta();
		ArrayList<String> l = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder();
		for(char c : getIndex().toCharArray()){
		  builder.append(ChatColor.COLOR_CHAR).append(c);
		}
		String hidden = builder.toString();
		l.add(hidden);
		
		l.add(ChatColor.GRAY+""+ChatColor.ITALIC+"Technika ti pomôže.");
		l.add(ChatColor.WHITE+"Obsahuje zbraň "+getWeapon().getItem().getItemMeta().getDisplayName());
		l.add(ChatColor.AQUA+"Možnosť stavať pevnosti a");
		l.add(ChatColor.AQUA+"zavolať bombardovanie.");
		im.setLore(l);
		im.setDisplayName(ChatColor.YELLOW+"Engineer");
		item.setItemMeta(im);
		return item;
	}

	@Override
	public String getIndex() {
		return "kit_engineer";
	}

	@Override
	public int getPrice() {
		return 2000;
	}

	@Override
	public boolean isAlive() {
		return true;
	}
	
}
