package bank.db.jdbc;

import bank.db.Database;
import bank.db.TransactionRepository;
import bank.model.Transaction;
import bank.model.enums.TransactionStatus;
import bank.model.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcTransactionRepository implements TransactionRepository {

    @Override
    public void save(Transaction tx) {
        String sql = "MERGE INTO transactions (id, timestamp, type, amount, account_id, counter_account_id, status, note) " +
                "KEY(id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, tx.getId());
            ps.setTimestamp(2, Timestamp.valueOf(tx.getTimestamp()));
            ps.setString(3, tx.getType().name());
            ps.setBigDecimal(4, tx.getAmount());
            ps.setObject(5, tx.getAccountId());

            if (tx.getTargetAccountId() != null) {
                ps.setObject(6, tx.getTargetAccountId());
            } else {
                ps.setNull(6, Types.NULL);
            }

            ps.setString(7, tx.getStatus() != null ? tx.getStatus().name() : "PENDING");
            ps.setString(8, tx.getNote() != null ? tx.getNote() : "");

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT id, timestamp, type, amount, account_id, counter_account_id, status, note FROM transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UUID txId = (UUID) rs.getObject("id");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                BigDecimal amount = rs.getBigDecimal("amount");
                UUID accountId = (UUID) rs.getObject("account_id");
                UUID counterAccountId = rs.getObject("counter_account_id") != null ? (UUID) rs.getObject("counter_account_id") : null;
                TransactionStatus status = TransactionStatus.valueOf(rs.getString("status"));
                String note = rs.getString("note");

                Transaction tx = new Transaction(txId, accountId, counterAccountId, amount, timestamp, type);
                tx.setStatus(status);
                tx.setNote(note);
                transactions.add(tx);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
