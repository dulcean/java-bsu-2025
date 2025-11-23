package bank.strategy;

import bank.model.Transaction;
import bank.service.TransactionService;

public interface TransactionStrategy {
    void execute(Transaction tx, TransactionService service);
}
