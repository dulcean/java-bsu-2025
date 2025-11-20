package com.banking.system.database;

import com.banking.system.model.BankAccount;
import java.sql.*;
import java.util.*;

public class AccountRepo {
    private final Connection conn;

    public AccountRepo(Connection conn) { this.conn = conn; }

    public void create(BankAccount a) {
        try (var ps = conn.prepareStatement("INSERT INTO accounts VALUES(?, ?, ?, ?)")) {
            ps.setString(1, a.getUuid().toString());
            ps.setString(2, a.getClientUuid().toString());
            ps.setDouble(3, a.getMoney());
            ps.setInt(4, a.isBlocked() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateBalanceAndStatus(BankAccount a) {
        try (var ps = conn.prepareStatement("UPDATE accounts SET money=?, blocked=? WHERE uuid=?")) {
            ps.setDouble(1, a.getMoney());
            ps.setInt(2, a.isBlocked() ? 1 : 0);
            ps.setString(3, a.getUuid().toString());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Optional<BankAccount> get(UUID id) {
        if (id == null) return Optional.empty();
        try (var ps = conn.prepareStatement("SELECT * FROM accounts WHERE uuid=?")) {
            ps.setString(1, id.toString());
            var rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new BankAccount(
                        UUID.fromString(rs.getString("uuid")),
                        UUID.fromString(rs.getString("client_uuid")),
                        rs.getDouble("money"),
                        rs.getInt("blocked") == 1
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<BankAccount> getAll() {
        List<BankAccount> res = new ArrayList<>();
        try (var rs = conn.createStatement().executeQuery("SELECT * FROM accounts")) {
            while (rs.next()) {
                res.add(new BankAccount(
                        UUID.fromString(rs.getString("uuid")),
                        UUID.fromString(rs.getString("client_uuid")),
                        rs.getDouble("money"),
                        rs.getInt("blocked") == 1));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return res;
    }

    public boolean exists(UUID id) {
        if (id == null) return false;
        try (var ps = conn.prepareStatement("SELECT 1 FROM accounts WHERE uuid = ?")) {
            ps.setString(1, id.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(UUID uuid) {
        String sql = "DELETE FROM accounts WHERE uuid = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}