package by.bsu.bank.strategy;

import by.bsu.bank.model.transaction.BaseTransaction;

public interface TransactionStrategy {
    void process(BaseTransaction transaction);
}
