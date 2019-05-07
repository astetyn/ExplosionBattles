package main.player;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;

public class StatusBoard {

	private ScoreboardManager sm = Bukkit.getScoreboardManager();
	private Scoreboard board;
	private Objective objective;
	private Score data = null;
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
	
	public void setup(String text) {
		resetScores();
		data=objective.getScore(text);
		data.setScore(1);
		playerEB.getPlayer().setScoreboard(board);
	}
	
	public void setClean() {
		playerEB.getPlayer().setScoreboard(sm.getNewScoreboard());
	}
	
	public void setData(int i) {
		for(Objective o : board.getObjectives()) {
			for(String entry : board.getEntries()) {
				Score s = o.getScore(entry);
				s.setScore(i);
			}
		}
		playerEB.getPlayer().setScoreboard(board);
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
		objective.setDisplayName(ChatColor.GOLD+"eb");
		playerEB.getPlayer().setScoreboard(board);
	}

}
