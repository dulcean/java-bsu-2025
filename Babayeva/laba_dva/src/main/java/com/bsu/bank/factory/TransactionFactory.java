package com.bsu.bank.factory;

import com.bsu.bank.command.DepositCommand;
import com.bsu.bank.command.FreezeCommand;
import com.bsu.bank.command.TransactionCommand;
import com.bsu.bank.command.WithdrawCommand;
import com.bsu.bank.model.Transaction;

/**
 * Factory Pattern: Создает конкретные команды на основе типа транзакции.
 */
public class TransactionFactory {
    public static TransactionCommand createCommand(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction object cannot be null.");
        }
        
        switch (transaction.getAction()) {
            case DEPOSIT:
                return new DepositCommand(transaction);
            case WITHDRAW:
                return new WithdrawCommand(transaction);
            case FREEZE:
                return new FreezeCommand(transaction);
            case TRANSFER:
                throw new UnsupportedOperationException("Transfer command is not implemented yet.");
            case UNFREEZE:
                throw new UnsupportedOperationException("Unfreeze command is not implemented yet.");
            default:
                throw new IllegalArgumentException("Unsupported transaction action: " + transaction.getAction());
        }
    }
}