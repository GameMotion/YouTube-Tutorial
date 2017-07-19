package com.gamemotion.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class Events implements Listener{

	PlayerHandler PlayerHandler;
	
	public Events(PlayerHandler _PlayerHandler){
		PlayerHandler = _PlayerHandler;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		System.out.println(e.getPlayer().getName());
		Player p = e.getPlayer();
		PlayerHandler.SetupPlayer(p);
		PlayerHandler.refreshRanks();
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		Player p = e.getPlayer();
		String name = p.getName();
		String prefix = PlayerHandler.getRankPrefix(PlayerHandler.getRank(p));
		String message = e.getMessage();
		Bukkit.broadcastMessage(prefix + name + ": " + message);
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		String cmd = args[0].replace("/", "").toLowerCase();
		int rank = PlayerHandler.getRank(p);
		if(cmd.equals("rank")){
			if(rank >= PlayerHandler.ADMIN){
				e.setCancelled(true);
				if(args.length == 3){
					String targetName = args[1];
					Player target = Bukkit.getPlayer(targetName);
					if(target != null){
						int rankValue = 0;
						String rankName = args[2].toLowerCase();
						if(rankName.equals("owner")){
							rankValue = PlayerHandler.OWNER;
						}else if(rankName.equals("admin")){
							rankValue = PlayerHandler.ADMIN;
						}else if(rankName.equals("moderator")){
							rankValue = PlayerHandler.MODERATOR;
						}else if(rankName.equals("member")){
							rankValue = PlayerHandler.MEMBER;
						}else{
							rankValue = -1;
						}
						if(rankValue >= 0){
							if(rankValue < rank){
								if(PlayerHandler.getRank(target) < rank){
									if(PlayerHandler.setRank(target, rankValue)){
										p.sendMessage("Successfully set " + target.getName() + "'s rank to " + rankName);
										target.sendMessage("Your rank has been changed to " + rankName);
										PlayerHandler.refreshRanks();
									}
								}else{
									p.sendMessage("Error: You can't modify people who are the same or a higher rank than you!");
								}
							}else{
								p.sendMessage("Error: You can't use ranks bigger than or equal to yours!");
							}
						}else{
							p.sendMessage(ChatColor.RED + "Error: " + rankName + " is not a rank!");
						}
					}else{
						p.sendMessage("Error: " + targetName + " is not online!");
					}
				}else{
					p.sendMessage("Usage: /rank <player> <rank>");
				}
			}
		}
	}
	
}
