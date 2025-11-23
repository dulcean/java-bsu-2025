package com.bank.service;

import com.bank.observer.BankObserver;
import com.bank.transaction.TransactionCommand;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class BankSystem {
    private static final AtomicReference<BankSystem> INSTANCE = new AtomicReference<>();
    private final ExecutorService executorService;
    private final List<BankObserver> observers;

    private BankSystem() {
        this.executorService = Executors.newFixedThreadPool(10);
        this.observers = new CopyOnWriteArrayList<>();
    }

    public static BankSystem getInstance() {
        INSTANCE.compareAndSet(null, new BankSystem());
        return INSTANCE.get();
    }

    public void addObserver(BankObserver observer) {
        observers.add(observer);
    }

    public void submitTransaction(TransactionCommand command) {
        executorService.submit(() -> {
            command.execute();
            notifyObservers(command);
        });
    }

    private void notifyObservers(TransactionCommand command) {
        for (BankObserver observer : observers) {
            observer.onTransactionCompleted(command);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}