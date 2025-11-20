package com.banking.system.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    private static final String URL = "jdbc:sqlite:banking_system.db";
    private static DbConnection instance;
    private Connection connection;

    private DbConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            initSchema();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Библиотека SQLite не найдена! Проверь настройки проекта.", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось подключиться к SQLite: " + e.getMessage());
        }
    }

    public static synchronized DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS clients (uuid TEXT PRIMARY KEY, username TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "client_uuid TEXT, " +
                    "money REAL, " +
                    "blocked INTEGER DEFAULT 0)");

            stmt.execute("CREATE TABLE IF NOT EXISTS operations (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "op_time INTEGER, " +
                    "op_type TEXT, " +
                    "status TEXT, " +
                    "amount REAL, " +
                    "src_acc TEXT, " +
                    "dst_acc TEXT, " +
                    "error_msg TEXT)");
        }
    }
}