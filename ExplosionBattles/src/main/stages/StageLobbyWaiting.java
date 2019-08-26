package main.stages;

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

	private Game game;
	private final int REQUIRED_PLAYERS_TO_START;
	
	public StageLobbyWaiting(Game game) {
		super(-1);
		this.game = game;
		this.REQUIRED_PLAYERS_TO_START = 2;
		start();
	}
	
	@Override
	public void start() {
		
		game.getClock().stop();
		game.setMapsManager(new MapsManager());
		
		for(PlayerEB playerEB : game.getPlayersInGame()) {
			readyPlayer(playerEB);
		}
	}
	
	@Override
	public void onTick() {
	}
	
	@Override
	public void end() {
	}

	@Override
	public void onPostJoin(PlayerEB playerEB) {

		readyPlayer(playerEB);
		
		if(game.getPlayersInGame().size()==REQUIRED_PLAYERS_TO_START) {
			game.setStage(new StageLobbyLaunching(game));
		}
		String message = ChatColor.GOLD+""+ChatColor.BOLD+"-- Explosion Battles --";
        playerEB.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	@Override
	public void onPostLeave(PlayerEB playerEB) {
		
	}
	
	@Override
	public void readyPlayer(PlayerEB playerEB) {
		playerEB.setGameStage(GameStage.LOBBY_WAITING);
		playerEB.setInventoryLayout(new LobbyInventoryLayout(playerEB));
		new LocationTeleport(playerEB,game.getMap(),GameLocation.LOBBY);
		playerEB.getStatusBoard().setup("Waiting for players...");
		playerEB.getStatusBoard().setTime(-1);
		super.readyPlayer(playerEB);
	}

}
