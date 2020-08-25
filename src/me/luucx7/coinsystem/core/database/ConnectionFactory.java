package me.luucx7.coinsystem.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.luucx7.coinsystem.CoinSystem;

public class ConnectionFactory {
	
	public static Connection connection;
	
	private static FileConfiguration config = CoinSystem.config;
	
	public static void start() {
		connection = getConnection();
	}
	
	public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://"+config.getString("database.host")+":"+config.getString("database.port")+"/"+config.getString("database.database")+"?user="+config.getString("database.username")+"&password="+config.getString("database.password")+"&useUnicode=true&characterEncoding=UTF-8");
        } catch (SQLException ex) {
        	Bukkit.getConsoleSender().sendMessage("Error connecting into the database! Disabling plugin...");
        	CoinSystem.instance.getPluginLoader().disablePlugin(CoinSystem.instance);
            throw new RuntimeException(ex);
        }
    }
    
    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void closeConnection(Connection con, PreparedStatement stmt) {
        closeConnection(con);
        try {
            if (stmt!=null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void closeConnection(Connection con, PreparedStatement stmt, ResultSet rs) {
        closeConnection(con, stmt);
        try {
            if (rs!=null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
