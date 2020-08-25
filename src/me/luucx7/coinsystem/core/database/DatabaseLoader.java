package me.luucx7.coinsystem.core.database;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseLoader {
	
	public static void init() {
		try {
			Statement stmt = ConnectionFactory.connection.createStatement();
			
			String playersTable = "CREATE TABLE IF NOT EXISTS players(" + 
					"    uuid varchar(32) UNIQUE NOT NULL PRIMARY KEY," + 
					"    coins int" + 
					")";
			
			stmt.executeUpdate(playersTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
