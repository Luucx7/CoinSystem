package me.luucx7.coinsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.luucx7.coinsystem.core.Utils;
import me.luucx7.coinsystem.core.database.CoinsHandler;
import me.luucx7.coinsystem.core.enums.Action;

@SuppressWarnings("deprecation")
public class CoinsSubcommands {
	
	String[] args;
	CommandSender sender;
	
	public CoinsSubcommands(CommandSender sender, String[] args) {
		this.sender = sender;
		this.args = args;
		
		switch (args[0]) {
		case "check":
			check();
			return;
		case "give":
		case "take":
		case "set":
			changeData();
			return;
		default:
			help(sender);
			return;
		}
	}
	
	public static void help(CommandSender sender) {
		sender.sendMessage("§8§l§m--------------------------");
		sender.sendMessage("§e/coins");
		sender.sendMessage("§e/coins check <player>");
		sender.sendMessage("§e/coins give <player> <amount>");
		sender.sendMessage("§e/coins take <player> <amount>");
		sender.sendMessage("§e/coins set <player> <amount>");
		sender.sendMessage("§8§l§m--------------------------");
	}
	
	private void check() {
		if (!sender.hasPermission("coins.check")) {
			
			return;
		}
		if (args.length==1) {
			
			return;
		}
		OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
		if (!p.hasPlayedBefore()) {
			sender.sendMessage("§cThis player does not exist!");
			return;
		}
		
		CoinsHandler.changeData(sender, p.getUniqueId(), Action.CHECK, 0);
	}
	
	private void changeData() {
		if (args.length<3) {
			sender.sendMessage("Not enough arguments!");
			return;
		}
		OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
		if (!p.hasPlayedBefore()) {
			sender.sendMessage("§cThis player does not exist!");
			return;
		}
		if (!Utils.isNumeric(args[2])) {
			sender.sendMessage("§cInvalid argument");
			return;
		}
		
		int value = Integer.parseInt(args[2]);
		
		switch (args[0]) {
		case "give":
			CoinsHandler.changeData(sender, p.getUniqueId(), Action.GIVE, value);
			return;
		case "take":
			CoinsHandler.changeData(sender, p.getUniqueId(), Action.TAKE, value);
			return;
		case "set":
			CoinsHandler.changeData(sender, p.getUniqueId(), Action.SET, value);
			return;
		default:
			sender.sendMessage("§c/coins "+args[1].toLowerCase()+" <player> <amount>");
		}
	}
}
