package by.bsu.bank.event;

import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.model.transaction.TransactionPrintVisitor;

public class LoggingTransactionListener implements TransactionListener {
    private final TransactionPrintVisitor transactionPrintVisitor = new TransactionPrintVisitor();

    @Override
    public void handle(TransactionEvent event) {
        BaseTransaction transaction = event.getTransaction();
        transaction.accept(transactionPrintVisitor);
        System.out.println("Event type: " + event.getType() + ", message: " + event.getMessage());
    }
}
