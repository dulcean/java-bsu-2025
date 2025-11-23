package by.bsu.bank.command;

import java.util.UUID;

public interface TransactionCommand extends Runnable {
    UUID getTransactionId();
}
