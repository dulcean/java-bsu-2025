package com.bsu.bank.command;

import java.util.List;

import com.bsu.bank.model.Transaction;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;
/**
 * Command Pattern: Интерфейс для всех команд (транзакций).
 */
public interface TransactionCommand {
    boolean execute(AccountRepository repository, List<TransactionObserver> observers);
    Transaction getTransaction();
    
    void accept(TransactionVisitor visitor);
}
