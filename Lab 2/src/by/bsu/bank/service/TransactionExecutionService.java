package by.bsu.bank.service;

import by.bsu.bank.event.TransactionEvent;
import by.bsu.bank.event.TransactionEventPublisher;
import by.bsu.bank.event.TransactionEventType;
import by.bsu.bank.factory.TransactionStrategyFactory;
import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.strategy.TransactionStrategy;

public class TransactionExecutionService {
    private final TransactionStrategyFactory transactionStrategyFactory;
    private final TransactionEventPublisher transactionEventPublisher;

    public TransactionExecutionService(TransactionStrategyFactory transactionStrategyFactory,
                                       TransactionEventPublisher transactionEventPublisher) {
        this.transactionStrategyFactory = transactionStrategyFactory;
        this.transactionEventPublisher = transactionEventPublisher;
    }

    public void executeTransaction(BaseTransaction transaction) {
        TransactionStrategy strategy = transactionStrategyFactory.createStrategy(transaction.getTransactionType());
        try {
            strategy.process(transaction);
            transactionEventPublisher.publishEvent(new TransactionEvent(
                    TransactionEventType.COMPLETED,
                    transaction,
                    "Transaction completed successfully"
            ));
        } catch (RuntimeException exception) {
            transactionEventPublisher.publishEvent(new TransactionEvent(
                    TransactionEventType.FAILED,
                    transaction,
                    exception.getMessage()
            ));
            throw exception;
        }
    }
}
