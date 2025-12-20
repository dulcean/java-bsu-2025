package com.bsu.bank.command;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.bsu.bank.model.Account;
import com.bsu.bank.model.Transaction;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;

public class DepositCommand implements TransactionCommand {
    private final Transaction transaction;

    public DepositCommand(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean execute(AccountRepository repository, List<TransactionObserver> observers) {
        Account account = repository.findAccountById(transaction.getAccountId());
        
        if (account == null) {
            notifyObservers(observers, false, "Account not found");
            return false;
        }
        
        if (account.isFrozen()) { 
            notifyObservers(observers, false, "Account is frozen");
            return false;
        }

        ReentrantLock lock = account.getLock();
        boolean lockAcquired = false;
        boolean success = false;
        String message = "Transaction failed (unknown reason)";

        try {
            lockAcquired = lock.tryLock(1, TimeUnit.SECONDS); 

            if (!lockAcquired) {
                message = "Failed to acquire account lock";
                notifyObservers(observers, false, message);
                return false; 
            }
            
            if (transaction.getAction() == com.bsu.bank.model.TransactionAction.DEPOSIT) {
                double newBalance = account.getBalance() + transaction.getAmount();
                account.setBalance(newBalance);
                repository.save(account);
                success = true;
                message = "Deposit successful";
                
            } else if (transaction.getAction() == com.bsu.bank.model.TransactionAction.WITHDRAW) {
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
            } else {
                message = "Unsupported transaction action";
                success = false;
            }
            
            return success;

        } catch (InterruptedException e) {
            message = "Transaction interrupted";
            Thread.currentThread().interrupt();
            return false;
        } finally {
            if (lockAcquired) {
                lock.unlock(); // Освобождаем Lock
            }
            
            if (success) {
                notifyObservers(observers, success, message);
            }
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