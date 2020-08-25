package me.luucx7.coinsystem.core.model;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.luucx7.coinsystem.core.database.CoinsHandler;

public class CoinPlayer {
	
	String name;
	final UUID uuid;
	int coins;
	
	public CoinPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	public CoinPlayer(OfflinePlayer player) {
		this.uuid = player.getUniqueId();
	}
	public CoinPlayer(UUID uuid, int coins) {
		this.uuid = uuid;
		this.coins = coins;
		
		this.name = Bukkit.getPlayer(uuid).getName();
	}
	
	public void saveCache() {
		CoinsHandler.players.add(this);
	}
	public void saveSQL() {
		CoinsHandler.saveData(this);
	}
	public void update() {
		CoinsHandler.loadData(this);
	}
	
	public void addCoin() {
		coins = coins+1;
		saveSQL();
	}
	public void addCoin(int amount) {
		coins = coins+amount;
		saveSQL();
	}
	
	public void removeCoin() {
		coins = coins-1;
		saveSQL();
	}
	public void removeCoin(int amount) {
		coins = coins-amount;
		saveSQL();
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UUID getUUID() {
		return this.uuid;
	}
	public int getCoins() {
		return this.coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
		saveSQL();
	}
}
