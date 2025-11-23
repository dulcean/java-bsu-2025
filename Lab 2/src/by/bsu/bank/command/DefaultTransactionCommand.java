package by.bsu.bank.command;

import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.service.TransactionExecutionService;

import java.util.UUID;

public class DefaultTransactionCommand implements TransactionCommand {
    private final BaseTransaction transaction;
    private final TransactionExecutionService transactionExecutionService;

    public DefaultTransactionCommand(BaseTransaction transaction,
                                     TransactionExecutionService transactionExecutionService) {
        this.transaction = transaction;
        this.transactionExecutionService = transactionExecutionService;
    }

    @Override
    public UUID getTransactionId() {
        return transaction.getTransactionId();
    }

    @Override
    public void run() {
        transactionExecutionService.executeTransaction(transaction);
    }
}
