package com.bsu.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.bsu.bank.db.DBUtils;
import com.bsu.bank.model.Account;

/**
 * Репозиторий для управления банковскими счетами.
 * Реализован как Singleton и использует шаблон Cache-Aside с H2.
 */
public class AccountRepository {
    
    // Singleton
    private static AccountRepository instance;
    private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    // Кэш (in-memory) для быстрого доступа и сохранения Lock-ов
    private final Map<UUID, Account> cache = new ConcurrentHashMap<>();

    private AccountRepository() {
        DBUtils.initializeDB();
    }
    
    public static void resetInstance() {
        rwLock.writeLock().lock();
        try {
            if (instance != null) {
                DBUtils.clearAccountsTable();
                instance.cache.clear();
            }
            instance = null;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public static AccountRepository getInstance() {
        if (instance == null) {
            rwLock.writeLock().lock();
            try {
                if (instance == null) {
                    instance = new AccountRepository();
                }
            } finally {
                rwLock.writeLock().unlock();
            }
        }
        return instance;
    }

    public void clearAll() {
        cache.clear();
    }

    public Account findAccountById(UUID accountId) {
        Account account = cache.get(accountId);
        if (account != null) {
            return account;
        }
        
        String sql = "SELECT ACCOUNT_ID, BALANCE, IS_FROZEN FROM ACCOUNTS WHERE ACCOUNT_ID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("BALANCE");
                    boolean isFrozen = rs.getBoolean("IS_FROZEN");
                    
                    Account dbAccount = new Account(accountId, balance, isFrozen);
                    
                    cache.put(accountId, dbAccount);
                    return dbAccount;
                }
            }
        } catch (SQLException e) {
            System.err.println("AccountRepository: Ошибка поиска счета в БД: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createAccount(Account account) {
        String sql = "INSERT INTO ACCOUNTS (ACCOUNT_ID, BALANCE, IS_FROZEN) VALUES (?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountId().toString());
            stmt.setDouble(2, account.getBalance());
            stmt.setBoolean(3, account.isFrozen());
            
            stmt.executeUpdate();
            conn.commit();
            
            cache.put(account.getAccountId(), account);
        } catch (SQLException e) {
            System.err.println("AccountRepository: Ошибка создания счета в БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(Account account) {
        String sql = "UPDATE ACCOUNTS SET BALANCE = ?, IS_FROZEN = ? WHERE ACCOUNT_ID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, account.getBalance());
            stmt.setBoolean(2, account.isFrozen());
            stmt.setString(3, account.getAccountId().toString());
            
            stmt.executeUpdate();
            conn.commit(); //фиксируем обновление в БД
            
        } catch (SQLException e) {
            System.err.println("AccountRepository: Ошибка обновления счета в БД: " + e.getMessage());
            e.printStackTrace();
        }
    }
}