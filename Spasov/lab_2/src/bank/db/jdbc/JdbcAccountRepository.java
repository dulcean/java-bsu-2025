package bank.db.jdbc;

import bank.db.AccountRepository;
import bank.db.Database;
import bank.model.Account;
import bank.model.enums.AccountStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcAccountRepository implements AccountRepository {

    @Override
    public Optional<Account> findById(UUID id) {
        String sql = "SELECT id, user_id, balance, status FROM accounts WHERE id = ?";
        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UUID accountId = (UUID) rs.getObject("id");
                    UUID userId = (UUID) rs.getObject("user_id");
                    AccountStatus status = AccountStatus.valueOf(rs.getString("status"));
                    Account account = new Account(
                            accountId,
                            userId,
                            rs.getBigDecimal("balance"),
                            status
                    );
                    return Optional.of(account);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Account account) {
        String sql = "MERGE INTO accounts (id, user_id, balance, status) KEY(id) VALUES (?,?,?,?)";
        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, account.getId());
            ps.setObject(2, account.getUserId());
            ps.setBigDecimal(3, account.getBalance());
            ps.setString(4, account.getStatus().name());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT id, user_id, balance, status FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account a = new Account(
                        (UUID) rs.getObject("id"),
                        (UUID) rs.getObject("user_id"),
                        rs.getBigDecimal("balance"),
                        AccountStatus.valueOf(rs.getString("status"))
                );
                accounts.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }
}
