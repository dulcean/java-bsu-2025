package com.bsu.bank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Утилитарный класс для работы с базой данных H2 (создание, очистка).
 */
public class DBUtils {
    private static final String JDBC_URL = "jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE IF NOT EXISTS ACCOUNTS (" +
        "ACCOUNT_ID VARCHAR(36) PRIMARY KEY," +
        "BALANCE DECIMAL(19, 2) NOT NULL," +
        "IS_FROZEN BOOLEAN NOT NULL" +
        ");";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
    
    /**
     * Инициализирует базу данных: создает таблицу, если она не существует.
     */
    public static void initializeDB() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            System.err.println("DBUtils: Ошибка инициализации базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void clearAccountsTable() {
        String clearSql = "TRUNCATE TABLE ACCOUNTS;";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(clearSql);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("DBUtils: Ошибка очистки таблицы ACCOUNTS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}