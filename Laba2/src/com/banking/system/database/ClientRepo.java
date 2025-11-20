package com.banking.system.database;

import com.banking.system.model.Client;
import java.sql.*;
import java.util.*;

public class ClientRepo {
    private final Connection conn;

    public ClientRepo(Connection conn) { this.conn = conn; }

    public void create(Client c) {
        try (var ps = conn.prepareStatement("INSERT INTO clients VALUES(?, ?)")) {
            ps.setString(1, c.getUuid().toString());
            ps.setString(2, c.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Client> getAll() {
        List<Client> list = new ArrayList<>();
        try (var rs = conn.createStatement().executeQuery("SELECT * FROM clients")) {
            while (rs.next()) {
                list.add(new Client(UUID.fromString(rs.getString("uuid")), rs.getString("username")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean exists(UUID id) {
        try (var ps = conn.prepareStatement("SELECT 1 FROM clients WHERE uuid = ?")) {
            ps.setString(1, id.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public void delete(UUID uuid) {
        String sqlDelAccounts = "DELETE FROM accounts WHERE client_uuid = ?";
        String sqlDelClient = "DELETE FROM clients WHERE uuid = ?";

        try {
            try (var ps = conn.prepareStatement(sqlDelAccounts)) {
                ps.setString(1, uuid.toString());
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement(sqlDelClient)) {
                ps.setString(1, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}