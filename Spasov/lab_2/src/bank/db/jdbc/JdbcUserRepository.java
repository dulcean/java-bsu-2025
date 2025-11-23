package bank.db.jdbc;

import bank.db.Database;
import bank.db.UserRepository;
import bank.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(id,nickname) VALUES (?,?)";
        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, user.getId());
            ps.setString(2, user.getNickname());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, nickname FROM users";
        List<User> users = new ArrayList<>();
        try (Connection c = Database.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UUID id = (UUID) rs.getObject("id");
                String nickname = rs.getString("nickname");
                users.add(new User(id, nickname, new ArrayList<>()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
