package bank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static Database instance;
    private final String url = "jdbc:h2:./data/bank";
    private final String user = "";
    private final String password = "";

    private Database() {
        init();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect", e);
        }
    }

    private void init() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("""
                        CREATE TABLE IF NOT EXISTS users (
                            id UUID PRIMARY KEY,
                            nickname VARCHAR(255) NOT NULL
                        );
                    """);

            st.execute("""
                        CREATE TABLE IF NOT EXISTS accounts (
                            id UUID PRIMARY KEY,
                            user_id UUID NOT NULL,
                            balance DECIMAL(20,2) NOT NULL,
                            status VARCHAR(20) NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES users(id)
                        );
                    """);

            st.execute("""
                        CREATE TABLE IF NOT EXISTS transactions (
                            id UUID PRIMARY KEY,
                            timestamp TIMESTAMP NOT NULL,
                            type VARCHAR(20) NOT NULL,
                            amount DECIMAL(20,2) NOT NULL,
                            account_id UUID NOT NULL,
                            counter_account_id UUID,
                            status VARCHAR(20) NOT NULL,
                            note VARCHAR(255)
                        );
                    """);

            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}