package me.luucx7.coinsystem.listeners;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.luucx7.coinsystem.core.database.CoinsHandler;
import me.luucx7.coinsystem.core.model.CoinPlayer;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		Optional<CoinPlayer> playerOp = CoinsHandler.players.stream().filter(cp -> ev.getPlayer().getUniqueId().equals(cp.getUUID())).findFirst();
		
		if (!playerOp.isPresent()) {
			CoinsHandler.loadData(ev.getPlayer().getUniqueId());
			return;
		}
		
		playerOp.get().update();
	}
}
