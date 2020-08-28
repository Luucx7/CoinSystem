package me.luucx7.coinsystem.core.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.luucx7.coinsystem.CoinSystem;
import me.luucx7.coinsystem.core.enums.Action;
import me.luucx7.coinsystem.core.model.CoinPlayer;

public class CoinsHandler {

	public static ArrayList<CoinPlayer> players = new ArrayList<CoinPlayer>();

	@Deprecated
	public static boolean checkPlayer(UUID uuid, CommandSender s) {
		String sql = "SELECT uuid FROM players WHERE uuid='"+uuid.toString().replace("-", "")+"'";
		try {
			PreparedStatement stmt = ConnectionFactory.connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				if (s != null) {
					s.sendMessage("§9[AirDrop] §cO jogador §5"+Bukkit.getPlayer(uuid).getName()+"§c nao existe!");
					return false;
				}
			}

			loadData(uuid);
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

	public static void changeData(CommandSender sender, UUID uuid, Action action, int newValue) {
		Bukkit.getScheduler().runTaskAsynchronously(CoinSystem.instance, () -> {
			String sql = "SELECT * FROM players WHERE uuid='"+uuid.toString().replace("-", "")+"'";
			
			CoinPlayer player = null;
			Optional<CoinPlayer> coinPlayerOP = players.stream().filter(cp -> cp.getUUID().equals(uuid)).findAny();
			
			try {
				// Load from memory
				if (coinPlayerOP.isPresent()) {
					player = coinPlayerOP.get();
					player.setName(Bukkit.getPlayer(uuid).getName());
					
				// Load from database
				} else {
					PreparedStatement stmt = ConnectionFactory.connection.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();

					if (!rs.next()) {
						if (sender != null) sender.sendMessage("§cThis player do not exist.");
						return;
					}
	
					do {
						player = new CoinPlayer(uuid, rs.getInt(2));
						player.setName(Bukkit.getOfflinePlayer(uuid).getName());
					} while (rs.next());
				}
				
				switch (action) {
				case CHECK:
					if (sender != null) sender.sendMessage("§e"+player.getName()+" coins: "+player.getCoins());
					return;
				case GIVE:
					player.addCoin(newValue);
					if (sender!=null) sender.sendMessage("§eGave "+newValue+" coins to "+player.getName());
					return;
				case SET:
					if (newValue<0) {
						if (sender!=null) sender.sendMessage("§cCoins amount cant be negative!");
						return;
					}
					player.setCoins(newValue);
					if (sender!=null) sender.sendMessage("§eSet coins from "+player.getName()+" to "+newValue);
					return;
				case TAKE:
					if (player.getCoins()-newValue<0) {
						if (sender!=null) sender.sendMessage("§cCoins amount cant be negative!");
						return;
					}
					player.removeCoin(newValue);
					if (sender!=null) sender.sendMessage("§eTake "+newValue+" coins from "+player.getName());
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void loadData(UUID uniqueID) {
		Bukkit.getScheduler().runTaskAsynchronously(CoinSystem.instance, () -> {
			String uuid = uniqueID.toString().replace("-", "");
			String sql = "SELECT * FROM players WHERE uuid='"+uuid+"'";

			try {
				PreparedStatement stmt = ConnectionFactory.connection.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();

				if (!rs.next()) {
					createData(uniqueID);
					return;
				}

				do {
					new CoinPlayer(uniqueID, rs.getInt(2)).saveCache();
				} while(rs.next());

			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		});
	}

	public static void loadData(CoinPlayer player) {
		Bukkit.getScheduler().runTaskAsynchronously(CoinSystem.instance, () -> {
			String uuid = player.getUUID().toString().replace("-", "");
			String sql = "SELECT * FROM players WHERE uuid='"+uuid+"'";

			try {
				PreparedStatement stmt = ConnectionFactory.connection.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();

				if (!rs.next()) {
					throw new NullPointerException("No player data to update.");
				}

				do {
					player.setCoins(rs.getInt(2));
				} while(rs.next());

			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		});
	}


	public static void saveData(CoinPlayer player) {
		Bukkit.getScheduler().runTaskAsynchronously(CoinSystem.instance, () -> {
			String sql = "UPDATE players SET coins = ? WHERE uuid = '"+player.getUUID().toString().replace("-", "")+"'";

			try {
				PreparedStatement stmt = ConnectionFactory.connection.prepareStatement(sql);

				stmt.setInt(1, player.getCoins());

				stmt.execute();
				stmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
	}

	public static void createData(UUID uniqueID) {
		Bukkit.getScheduler().runTaskAsynchronously(CoinSystem.instance, () -> {
			String sql = "INSERT INTO players(uuid, coins) VALUES(?,?)";

			try {
				PreparedStatement stmt = ConnectionFactory.connection.prepareStatement(sql);
				stmt.setString(1, uniqueID.toString().replace("-", ""));
				stmt.setInt(2, 0);
				stmt.execute();
				stmt.close();

				new CoinPlayer(uniqueID, 0).saveCache();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
}
