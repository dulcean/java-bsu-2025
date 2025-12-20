package com.bsu.bank.observer;

import com.bsu.bank.model.Transaction;
/**
 * Конкретный наблюдатель для логирования транзакций в консоль.
 */
public class ConsoleLoggerObserver implements TransactionObserver {
    @Override
    public void onTransactionCompleted(Transaction transaction, boolean success, String message) {
        String status = success ? "SUCCESS" : "FAILURE";
        System.out.printf("[%s] Transaction %s (%s) for account %s: %s%n", 
                          status, transaction.getTransactionId().toString().substring(0, 8), 
                          transaction.getAction(), transaction.getAccountId().toString().substring(0, 8), message);
    }
}