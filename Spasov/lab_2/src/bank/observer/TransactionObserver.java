package bank.observer;

import bank.model.Transaction;

@FunctionalInterface
public interface TransactionObserver {
    void update(Transaction transaction);
}
