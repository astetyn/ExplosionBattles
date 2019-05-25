package main.stages;

import main.Clock;
import main.Game;
import main.maps.GameLocation;
import main.maps.LocationTeleport;
import main.maps.voting.MapsManager;
import main.player.GameStage;
import main.player.PlayerEB;
import main.player.layouts.LobbyInventoryLayout;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class StageLobbyWaiting extends Stage {

	private int finalTicks = -1;
	private Game game;
	
	public StageLobbyWaiting(Game game) {
		this.game = game;
		setFinalTicks(finalTicks);
		start();
	}
	
	@Override
	public void start() {
		
		game.getClock().stop();
		
		game.setMapsManager(new MapsManager());
		for(PlayerEB playerEB : game.getPlayers()) {
			playerEB.setInventoryLayout(new LobbyInventoryLayout(playerEB));
			playerEB.setGameStage(GameStage.LOBBY_WAITING);
			playerEB.getPlayer().setHealth(20);
			new LocationTeleport(playerEB,game.getMap(),GameLocation.LOBBY);
			playerEB.getStatusBoard().setup("Waiting for players...");
			playerEB.getStatusBoard().tick(-1);
		}
	}
	
	@Override
	public void tick() {
	}
	
	@Override
	public void end() {
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {
		
		if(game.getClock().isStopped()) {
			game.setClock(new Clock(game));
		}
		
		playerEB.getStatusBoard().setup("Waiting for players...");
		playerEB.getStatusBoard().tick(-1);
		new LocationTeleport(playerEB,game.getMap(),GameLocation.LOBBY);
		playerEB.setInventoryLayout(new LobbyInventoryLayout(playerEB));
		
		if(game.getPlayers().size()==2) {
			game.setStage(new StageLobbyLaunching(game));
		}
		
		String message = ChatColor.GOLD+""+ChatColor.BOLD+"-- Explosion Battles --";
        playerEB.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {
		
	}

}
