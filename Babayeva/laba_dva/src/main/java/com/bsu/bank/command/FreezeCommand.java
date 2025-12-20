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
 * Команда для заморозки или разморозки банковского счета.
 * Использует паттерн Command и Visitor.
 */
public class FreezeCommand implements TransactionCommand {
    private final Transaction transaction;

    public FreezeCommand(Transaction transaction) {
        if (transaction.getAction() != TransactionAction.FREEZE && transaction.getAction() != TransactionAction.UNFREEZE) {
            throw new IllegalArgumentException("FreezeCommand only supports FREEZE or UNFREEZE actions.");
        }
        this.transaction = transaction;
    }

    @Override
    public boolean execute(AccountRepository repository, List<TransactionObserver> observers) {
        Account account = repository.findAccountById(transaction.getAccountId());
        
        boolean shouldBeFrozen = transaction.getAction() == TransactionAction.FREEZE;

        if (account == null) {
            notifyObservers(observers, false, "Account not found");
            return false;
        }

        ReentrantLock lock = account.getLock();
        boolean lockAcquired = false;
        boolean success = false;
        String message = "";

        try {
            lockAcquired = lock.tryLock(5, TimeUnit.SECONDS);

            if (!lockAcquired) {
                message = "Failed to acquire account lock";
                notifyObservers(observers, false, message); 
                return false;
            }

            if (account.isFrozen() == shouldBeFrozen) {
                success = false; 
                message = shouldBeFrozen ? 
                    "Account is already frozen (No operation performed)" : 
                    "Account is already unfrozen (No operation performed)";
            } else {
                account.setFrozen(shouldBeFrozen);
                repository.save(account);
                success = true; 
                message = shouldBeFrozen ? "Account successfully frozen" : "Account successfully unfrozen";
            }

            return success;

        } catch (InterruptedException e) {
            message = "Transaction interrupted";
            Thread.currentThread().interrupt();
            success = false;
            return false;
        } finally {
            if (lockAcquired) {
                lock.unlock();
            }
            notifyObservers(observers, success, message);
        }
    }
    
    private void notifyObservers(List<TransactionObserver> observers, boolean success, String message) {
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