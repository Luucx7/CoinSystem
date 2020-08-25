package me.luucx7.coinsystem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.luucx7.coinsystem.commands.CoinsCommand;
import me.luucx7.coinsystem.core.database.ConnectionFactory;
import me.luucx7.coinsystem.core.database.DatabaseLoader;
import me.luucx7.coinsystem.listeners.PlayerJoinListener;

public class CoinSystem extends JavaPlugin {

	public static FileConfiguration config;
	public static CoinSystem instance;
	
	public void onEnable() {
		saveDefaultConfig();
		
		config = this.getConfig();
		instance = this;
		
		ConnectionFactory.start();
		DatabaseLoader.init();
		
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		this.getCommand("coin").setExecutor(new CoinsCommand());
	}
	
	public void onDisable() {
		ConnectionFactory.closeConnection(ConnectionFactory.connection);
	}
}
