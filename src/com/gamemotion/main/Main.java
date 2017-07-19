package com.gamemotion.main;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	FileHandler FileHandler = new FileHandler();
	PlayerHandler PlayerHandler = new PlayerHandler();
	Events Events = new Events(PlayerHandler);
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(Events, this);
		FileHandler.Setup();
	}
	
	@Override
	public void onDisable(){
		
	}
	
}
