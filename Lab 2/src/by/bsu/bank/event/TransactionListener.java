package by.bsu.bank.event;

public interface TransactionListener {
    void handle(TransactionEvent event);
}
