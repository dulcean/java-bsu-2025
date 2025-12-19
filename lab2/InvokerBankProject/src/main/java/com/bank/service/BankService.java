package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.patterns.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankService {
    private static BankService instance;
    private final Map<UUID, Account> accountRepository = new ConcurrentHashMap<>();
    private final Map<UUID, Lock> accountLocks = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final List<TransactionObserver> observers = new ArrayList<>();

    private BankService() {
        observers.add(tx -> System.out.println("[AUDIT] Tx: " + tx.getAction() + " ID: " + tx.getId()));
    }

    public static synchronized BankService getInstance() {
        if (instance == null)
            instance = new BankService();
        return instance;
    }

    public void registerUser(User user) {
        for (Account acc : user.getAccounts()) {
            accountRepository.put(acc.getId(), acc);
            accountLocks.put(acc.getId(), new ReentrantLock());
        }
    }

    public Account createDemoAccountIfNeeded() {
        if (accountRepository.isEmpty()) {
            User u = new User("InvokerMain");
            Account a = new Account("Main Stash");
            u.addAccount(a);
            registerUser(u);
            return a;
        }
        return accountRepository.values().iterator().next();
    }

    public CompletableFuture<Void> processTransaction(Transaction tx) {
        return CompletableFuture.runAsync(() -> {
            UUID accId = tx.getAccountId();
            Lock lock = accountLocks.get(accId);
            Lock targetLock = (tx.getTargetAccountId() != null) ? accountLocks.get(tx.getTargetAccountId()) : null;

            try {
                if (lock != null)
                    lock.lock();
                if (targetLock != null)
                    targetLock.lock();

                Account account = accountRepository.get(accId);
                if (account == null)
                    throw new IllegalArgumentException("Account not found");

                TransactionStrategy strategy = StrategyFactory.getStrategy(tx.getAction());
                strategy.execute(account, tx, accountRepository);
                notifyObservers(tx);

            } catch (Exception e) {
                System.err.println("Tx Failed: " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                if (targetLock != null)
                    targetLock.unlock();
                if (lock != null)
                    lock.unlock();
            }
        }, executor);
    }

    private void notifyObservers(Transaction tx) {
        for (TransactionObserver obs : observers)
            obs.onTransactionCompleted(tx);
    }

    public Account getAccount(UUID id) {
        return accountRepository.get(id);
    }
}
