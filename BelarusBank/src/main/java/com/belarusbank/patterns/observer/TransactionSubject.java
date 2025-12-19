package com.belarusbank.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class TransactionSubject {
    private List<TransactionObserver> observers = new ArrayList<>();

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (TransactionObserver observer : observers) {
            observer.onTransactionCompleted(message);
        }
    }
}
