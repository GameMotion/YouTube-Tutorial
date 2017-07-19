package com.gamemotion.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class PlayerHandler {

	int OWNER = 100;
	int ADMIN = 80;
	int MODERATOR = 50;
	int MEMBER = 0;
	
	public void SetupPlayer(Player p){
		File f = new File("plugins/YouTube/PlayerData/" + p.getUniqueId() + ".yml");
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		yml.addDefault("Name", p.getName());
		yml.addDefault("Rank", MEMBER);
		yml.addDefault("Money", 100);
		yml.options().copyDefaults(true);
		try {
			yml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean setRank(Player p, int rank){
		File f = new File("plugins/YouTube/PlayerData/" + p.getUniqueId() + ".yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		yml.set("Rank", rank);
		try {
			yml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addMoney(Player p, int amount){
		File f = new File("plugins/YouTube/PlayerData/" + p.getUniqueId() + ".yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		int curMoney = getMoney(p);
		curMoney += amount;
		yml.set("Money", curMoney);
		try {
			yml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean removeMoney(Player p, int amount){
		File f = new File("plugins/YouTube/PlayerData/" + p.getUniqueId() + ".yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		int curMoney = getMoney(p);
		curMoney -= amount;
		yml.set("Money", curMoney);
		try {
			yml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getMoney(Player p){
		File f = new File("plugins/YouTube/PlayerData/" + p.getUniqueId() + ".yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		return yml.getInt("Money");
	}
	
	public int getRank(Player p){
		File f = new File("plugins/YouTube/PlayerData/" + p.getUniqueId() + ".yml");
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		return yml.getInt("Rank");
	}
	
	public String getRankPrefix(int Rank){
		if(Rank == OWNER){
			return ChatColor.RED.toString() + ChatColor.BOLD + "Owner " + ChatColor.WHITE;
		}else if(Rank == ADMIN){
			return ChatColor.GREEN.toString() + ChatColor.BOLD + "Admin " + ChatColor.WHITE;
		}else if(Rank == MODERATOR){
			return ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Mod " + ChatColor.WHITE;
		}else{
			return "";
		}
	}
	
	public void refreshRanks(){
		for(Player p : Bukkit.getOnlinePlayers()){
			Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
			for(Player pl : Bukkit.getOnlinePlayers()){
				String prefix = getRankPrefix(getRank(pl));
				Team team = board.registerNewTeam(pl.getName());
				team.setPrefix(prefix);
				team.addEntry(pl.getName());
			}
			
			Objective objective = board.registerNewObjective("Stats", "dummy");
			
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "You" + ChatColor.WHITE.toString() + ChatColor.BOLD + "Tube");
			
			String prefix = getRankPrefix(getRank(p));
			int money = getMoney(p);
			
			Score rankScore = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "Rank: " + prefix);
			rankScore.setScore(2);
			Score blankScore1 = objective.getScore(" ");
			blankScore1.setScore(1);
			Score moneyScore = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "Money: " + ChatColor.GREEN + money);
			moneyScore.setScore(0);
			
			p.setScoreboard(board);
		}
	}
	
}
