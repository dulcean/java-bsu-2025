package bank.service;

import bank.db.UserRepository;
import bank.db.jdbc.JdbcUserRepository;
import bank.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepo = new JdbcUserRepository();

    public User createUser(String nickname) {
        UUID id = UUID.randomUUID();
        User user = new User(id, nickname, new ArrayList<>());
        userRepo.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
