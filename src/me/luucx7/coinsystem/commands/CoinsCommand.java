package me.luucx7.coinsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.luucx7.coinsystem.core.database.CoinsHandler;

public class CoinsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg, String[] args) {
		if (!s.hasPermission("coins.access")) {
			s.sendMessage("§cNot enough permissions!");
			return true;
		}
		
		if (args.length==0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				int coins = CoinsHandler.players.stream().filter(cp -> cp.getUUID().equals(p.getUniqueId())).findFirst().get().getCoins();
				p.sendMessage("§eYou have "+coins+" NGCoins.");
			} else {
				CoinsSubcommands.help(s);
			}
		} else {
			new CoinsSubcommands(s, args);
		}
		return false;
	}

}
