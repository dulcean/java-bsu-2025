package com.belarusbank.patterns.observer;

public interface TransactionObserver {
    void onTransactionCompleted(String message);
}
