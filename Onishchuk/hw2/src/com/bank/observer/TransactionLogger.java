package com.bank.observer;

import com.bank.transaction.TransactionCommand;

public class TransactionLogger implements BankObserver {
    @Override
    public void onTransactionCompleted(TransactionCommand transaction) {
        System.out.println("[LOG] Transaction " + transaction.getTransactionId() + " finished with status " + transaction.getStatus());
    }
}