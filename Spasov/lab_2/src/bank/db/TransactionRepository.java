package bank.db;

import bank.model.Transaction;

import java.util.List;

public interface TransactionRepository {
    void save(Transaction tx);

    List<Transaction> findAll();
}
