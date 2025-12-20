package com.bsu.bank.observer;

import com.bsu.bank.model.Transaction;

public interface TransactionObserver {
    void onTransactionCompleted(Transaction transaction, boolean success, String message);
}