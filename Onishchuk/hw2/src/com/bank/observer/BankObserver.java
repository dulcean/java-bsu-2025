package com.bank.observer;

import com.bank.transaction.TransactionCommand;

public interface BankObserver {
    void onTransactionCompleted(TransactionCommand transaction);
}