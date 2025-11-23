package bank.db;

import bank.model.User;

import java.util.List;

public interface UserRepository {
    void save(User user);

    List<User> findAll();
}
