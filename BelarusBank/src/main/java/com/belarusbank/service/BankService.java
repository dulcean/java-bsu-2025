package com.belarusbank.service;

import com.belarusbank.dao.UserDao;
import com.belarusbank.model.Account;
import com.belarusbank.model.TransactionType;
import com.belarusbank.model.User;
import com.belarusbank.patterns.command.BankTransaction;
import com.belarusbank.patterns.factory.TransactionFactory;
import com.belarusbank.patterns.observer.TransactionSubject;
import com.belarusbank.patterns.visitor.AuditVisitor;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BankService extends TransactionSubject {
    private UserDao userDao;
    private TransactionFactory factory;

    public BankService() {
        this.userDao = new UserDao();
        this.factory = new TransactionFactory(userDao);
    }

    public void createUser(String nickname) {
        User user = new User(nickname);
        userDao.saveUser(user);
        createAccount(user); 
    }

    public void createAccount(User user) {
        Account acc = new Account(user.getId());
        userDao.saveAccount(acc);
    }

    public List<User> getAllUsers() {
        return userDao.findAllUsers();
    }
    
    public List<Account> getAllAccounts() {
        return userDao.findAllAccounts();
    }

    public void processTransaction(TransactionType type, Account source, Account target, BigDecimal amount) {
        CompletableFuture.runAsync(() -> {
            try {
                BankTransaction command = factory.createTransaction(type, source, target, amount);
                command.execute();
                notifyObservers("УСПЕХ: " + command.getDescription());
            } catch (Exception e) {
                notifyObservers("ОШИБКА: " + e.getMessage());
            }
        });
    }

    public String runAudit() {
        AuditVisitor visitor = new AuditVisitor();
        List<User> users = getAllUsers();
        for (User u : users) {
            for (Account a : u.getAccounts()) {
                visitor.visit(a);
            }
        }
        return "АУДИТ ЦБ: Всего активов: " + visitor.getTotalAssets() + " BYN | Заморожено счетов: " + visitor.getFrozenCount();
    }
}
