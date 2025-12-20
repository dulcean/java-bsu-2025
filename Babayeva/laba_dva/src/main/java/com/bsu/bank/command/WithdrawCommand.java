package com.bsu.bank.command;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.bsu.bank.model.Account;
import com.bsu.bank.model.Transaction;
import com.bsu.bank.model.TransactionAction;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;

/**
 * Команда для снятия средств с банковского счета.
 * Использует паттерн Command и Visitor.
 */
public class WithdrawCommand implements TransactionCommand {
    private final Transaction transaction;

    public WithdrawCommand(Transaction transaction) {
        if (transaction.getAction() != TransactionAction.WITHDRAW) {
            throw new IllegalArgumentException("WithdrawCommand only supports WITHDRAW action.");
        }
        this.transaction = transaction;
    }

    @Override
    public boolean execute(AccountRepository repository, List<TransactionObserver> observers) {
        Account account = repository.findAccountById(transaction.getAccountId());

        if (account == null) {
            notifyObservers(observers, false, "Account not found", true);
            return false;
        }

        if (account.isFrozen()) {
            notifyObservers(observers, false, "Account is frozen", true);
            return false;
        }

        ReentrantLock lock = account.getLock();
        boolean lockAcquired = false;
        boolean success = false;
        String message = "Withdrawal failed (unknown reason)";

        try {
            // Атомарность: Пытаемся захватить Lock
            lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);

            if (!lockAcquired) {
                message = "Failed to acquire account lock";
                return false;
            }

            if (account.getBalance() >= transaction.getAmount()) {
                double newBalance = account.getBalance() - transaction.getAmount();
                account.setBalance(newBalance);
                repository.save(account);
                success = true;
                message = "Withdrawal successful";
            } else {
                message = "Insufficient funds";
                success = false;
            }

            return success;

        } catch (InterruptedException e) {
            message = "Transaction interrupted";
            Thread.currentThread().interrupt();
            return false;
        } finally {
            if (lockAcquired) {
                lock.unlock();
            }
            notifyObservers(observers, success, message, true);
        }
    }
    
    private void notifyObservers(List<TransactionObserver> observers, boolean success, String message, boolean attempt) {
        if (!attempt) return; 

        String finalMessage = String.format("[%s]: %s", transaction.getTransactionId().toString().substring(0, 8), message);
        observers.forEach(o -> o.onTransactionCompleted(transaction, success, finalMessage));
    }


    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}