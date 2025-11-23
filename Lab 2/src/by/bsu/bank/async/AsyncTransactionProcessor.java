package by.bsu.bank.async;

import by.bsu.bank.command.TransactionCommand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncTransactionProcessor {
    private static final AsyncTransactionProcessor INSTANCE = new AsyncTransactionProcessor();

    private final ExecutorService executorService;

    private AsyncTransactionProcessor() {
        int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
        this.executorService = Executors.newFixedThreadPool(numberOfThreads);
    }

    public static AsyncTransactionProcessor getInstance() {
        return INSTANCE;
    }

    public Future<?> submit(TransactionCommand transactionCommand) {
        return executorService.submit(transactionCommand);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
