package com.belarusbank.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:h2:./belarusbank_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private DatabaseManager() {
        initTables();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void initTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id UUID PRIMARY KEY, " +
                    "nickname VARCHAR(255))";
            
            String sqlAccounts = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "id UUID PRIMARY KEY, " +
                    "user_id UUID, " +
                    "balance DECIMAL(20, 2), " +
                    "is_frozen BOOLEAN, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))";

            stmt.execute(sqlUsers);
            stmt.execute(sqlAccounts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
