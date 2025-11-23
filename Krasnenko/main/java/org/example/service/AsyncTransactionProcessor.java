package org.example.service;

import org.example.model.TransactionRecord;
import org.example.model.TransactionType;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsyncTransactionProcessor {
    private final ExecutorService executor;
    private final TransactionStrategyFactory factory;

    public AsyncTransactionProcessor(TransactionStrategyFactory factory) {
        this.executor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        this.factory = factory;
    }

    public CompletableFuture<Boolean> submit(TransactionRecord tx) {
        Supplier<Boolean> task = () -> {
            TransactionStrategy strategy = factory.get(tx.getType());
            if (strategy == null) return false;
            return strategy.execute(tx);
        };
        return CompletableFuture.supplyAsync(task, executor);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
