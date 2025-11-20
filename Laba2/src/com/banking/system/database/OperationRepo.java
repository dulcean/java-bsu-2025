package com.banking.system.database;

import com.banking.system.model.Operation;
import java.sql.*;
import java.util.*;

public class OperationRepo {
    private final Connection conn;

    public OperationRepo(Connection conn) { this.conn = conn; }

    public void saveOrUpdate(Operation op) {
        String sql = "INSERT OR REPLACE INTO operations (uuid, op_time, op_type, status, amount, src_acc, dst_acc, error_msg) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, op.getUuid().toString());
            ps.setLong(2, op.getTimeCreated());
            ps.setString(3, op.getType().name());
            ps.setString(4, op.getStatus().name());
            ps.setDouble(5, op.getAmount());
            ps.setString(6, op.getFromAcc() != null ? op.getFromAcc().toString() : null);
            ps.setString(7, op.getToAcc() != null ? op.getToAcc().toString() : null);
            ps.setString(8, op.getError());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Operation> getAll() {
        List<Operation> list = new ArrayList<>();
        try (var rs = conn.createStatement().executeQuery("SELECT * FROM operations ORDER BY op_time DESC")) {
            while (rs.next()) {
                list.add(new Operation(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getLong("op_time"),
                        Operation.Type.valueOf(rs.getString("op_type")),
                        rs.getDouble("amount"),
                        rs.getString("src_acc") != null ? UUID.fromString(rs.getString("src_acc")) : null,
                        rs.getString("dst_acc") != null ? UUID.fromString(rs.getString("dst_acc")) : null,
                        Operation.Status.valueOf(rs.getString("status")),
                        rs.getString("error_msg")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}