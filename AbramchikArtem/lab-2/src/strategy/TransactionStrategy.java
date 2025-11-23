package strategy;

import model.Transaction;

public interface TransactionStrategy {
    void execute(Transaction tx) throws Exception;
}
