package com.banking.system.logic;

import com.banking.system.model.Operation;
import java.util.concurrent.*;

public class AsyncExecutor {
    private final ExecutorService pool;
    private final BankingService service;
    private final BlockingQueue<Operation> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public AsyncExecutor(int threads, BankingService srv) {
        this.pool = Executors.newFixedThreadPool(threads);
        this.service = srv;
        startConsumers(threads);
    }

    private void startConsumers(int n) {
        for (int i = 0; i < n; i++) {
            pool.submit(() -> {
                while (running && !Thread.currentThread().isInterrupted()) {
                    try {
                        Operation op = queue.take();
                        service.runOperation(op);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    public void submit(Operation op) {
        queue.offer(op);
    }

    public void stop() {
        running = false;
        pool.shutdownNow();
    }
}