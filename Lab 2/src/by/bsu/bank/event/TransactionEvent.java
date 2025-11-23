package by.bsu.bank.event;

import by.bsu.bank.model.transaction.BaseTransaction;

public class TransactionEvent {
    private final TransactionEventType type;
    private final BaseTransaction transaction;
    private final String message;

    public TransactionEvent(TransactionEventType type, BaseTransaction transaction, String message) {
        this.type = type;
        this.transaction = transaction;
        this.message = message;
    }

    public TransactionEventType getType() {
        return type;
    }

    public BaseTransaction getTransaction() {
        return transaction;
    }

    public String getMessage() {
        return message;
    }
}
