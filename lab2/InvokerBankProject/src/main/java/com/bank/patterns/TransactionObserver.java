package com.bank.patterns;

import com.bank.model.Transaction;

public interface TransactionObserver {
    void onTransactionCompleted(Transaction tx);
}
