package main.player;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import main.Game;
import net.md_5.bungee.api.ChatColor;

public class StatusBoard {

	private ScoreboardManager sm = Bukkit.getScoreboardManager();
	private Scoreboard board;
	private Objective objective;
	private Score emptyLine;
	private Score time;
	private Score emptyLine2;
	private Score players;
	private Score emptyLine3;
	private Score coins;
	private Score points;
	private String timeName = "";
	private String playersName = ChatColor.YELLOW+"Hráči v hre: "+ChatColor.RESET;
	private String coinsName = ChatColor.GREEN+""+ChatColor.BOLD+"Coins: "+ChatColor.RESET;
	private String pointsName = ChatColor.BLUE+""+ChatColor.BOLD+"EP: "+ChatColor.RESET;
	private PlayerEB playerEB;
	private final String objName = "eb";
	
	public StatusBoard(PlayerEB playerEB) {
		this.playerEB = playerEB;
		board = playerEB.getPlayer().getScoreboard();
		if(board.getObjective(objName)==null) {
			setupBoard();
		}else {
			objective = board.getObjective(objName);
		}
	}
	
	public void setup(String timeName) {
		this.timeName = ChatColor.YELLOW+""+ChatColor.BOLD+timeName+" "+ChatColor.RESET;
		resetScores();
		playerEB.getPlayer().setScoreboard(board);
	}
	
	public void clean() {
		playerEB.getPlayer().setScoreboard(sm.getNewScoreboard());
	}
	
	public void tick(int remainingTime) {
		resetScores();
		emptyLine=objective.getScore(ChatColor.RED+"");
		emptyLine.setScore(7);
		if(remainingTime==-1) {
			time=objective.getScore(timeName);
		}else {
			time=objective.getScore(timeName+remainingTime);
		}
		time.setScore(6);
		emptyLine2=objective.getScore(ChatColor.BLUE+"");
		emptyLine2.setScore(5);
		players=objective.getScore(playersName+Game.getInstance().getPlayers().size());
		players.setScore(4);
		emptyLine3=objective.getScore("");
		emptyLine3.setScore(3);
		coins=objective.getScore(coinsName+playerEB.getUserAccount().getCoins());
		coins.setScore(2);
		points=objective.getScore(pointsName+playerEB.getUserAccount().getEPoints());
		points.setScore(1);
	}
	
	private void resetScores() {
		for(String entry : board.getEntries()) {
			board.resetScores(entry);
		}
	}
	
	private void setupBoard() {
		board = sm.getNewScoreboard();
		objective = board.registerNewObjective(objName, "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.DARK_GRAY+""+ChatColor.BOLD+"["+ChatColor.GOLD+ChatColor.BOLD+"Explosion Battles"+ChatColor.DARK_GRAY+ChatColor.BOLD+"]");
		playerEB.getPlayer().setScoreboard(board);
	}

}
