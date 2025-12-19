package com.belarusbank.dao;

import com.belarusbank.model.Account;
import com.belarusbank.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {
    
    public void saveUser(User user) {
        String sql = "MERGE INTO users (id, nickname) KEY(id) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, user.getId());
            pstmt.setString(2, user.getNickname());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAccount(Account account) {
        String sql = "MERGE INTO accounts (id, user_id, balance, is_frozen) KEY(id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, account.getId());
            pstmt.setObject(2, account.getUserId());
            pstmt.setBigDecimal(3, account.getBalance());
            pstmt.setBoolean(4, account.isFrozen());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User((UUID) rs.getObject("id"), rs.getString("nickname"));
                user.getAccounts().addAll(findAccountsByUserId(user.getId()));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new Account(
                        (UUID) rs.getObject("id"),
                        (UUID) rs.getObject("user_id"),
                        rs.getBigDecimal("balance"),
                        rs.getBoolean("is_frozen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public List<Account> findAccountsByUserId(UUID userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(
                        (UUID) rs.getObject("id"),
                        userId,
                        rs.getBigDecimal("balance"),
                        rs.getBoolean("is_frozen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    
    public void updateAccount(Account account) {
        saveAccount(account);
    }
}
