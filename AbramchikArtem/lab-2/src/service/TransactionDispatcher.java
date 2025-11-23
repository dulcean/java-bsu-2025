package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionDispatcher {

    private static final TransactionDispatcher INSTANCE = new TransactionDispatcher();
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    private TransactionDispatcher() {}

    public static TransactionDispatcher getInstance() {
        return INSTANCE;
    }

    public ExecutorService getPool() {
        return pool;
    }
}
