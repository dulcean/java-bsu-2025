package bank.async;

import bank.model.Account;
import bank.model.Transaction;
import bank.observer.TransactionObservable;
import bank.observer.TransactionObserver;
import bank.service.TransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncTransactionProcessor implements TransactionObservable {

    private final TransactionService transactionService;
    private final LocksManager locksManager;
    private final BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor;
    private final List<TransactionObserver> listeners = new ArrayList<>();

    public AsyncTransactionProcessor(TransactionService transactionService, int threadCount) {
        this.transactionService = transactionService;
        this.locksManager = new LocksManager();
        this.executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(this::processQueue);
        }
    }

    @Override
    public synchronized void addListener(TransactionObserver listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeListener(TransactionObserver listener) {
        listeners.remove(listener);
    }

    public void submitTransaction(Transaction tx) {
        queue.offer(tx);
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private void processQueue() {
        try {
            while (true) {
                Transaction tx = queue.take();
                processTransaction(tx);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processTransaction(Transaction tx) {
        UUID accountId = tx.getAccountId();
        UUID targetId = tx.getTargetAccountId();

        try {
            if (targetId != null) {
                locksManager.lockTwo(accountId, targetId);
            } else {
                locksManager.lock(accountId);
            }

            transactionService.execute(tx);

            Account acc = transactionService.getAccount(tx.getAccountId());
            tx.setAccountBalanceAfter(acc.getBalance());

            if (tx.getTargetAccountId() != null) {
                Account target = transactionService.getAccount(tx.getTargetAccountId());
                tx.setTargetAccountBalanceAfter(target.getBalance());
            }

        } catch (Exception e) {
            tx.setNote("Failed: " + e.getMessage());
            System.err.println("Transaction failed: " + tx);
        } finally {
            if (targetId != null) {
                locksManager.unlockTwo(accountId, targetId);
            } else {
                locksManager.unlock(accountId);
            }

            notifyStatusChanged(tx);
        }
    }

    private void notifyStatusChanged(Transaction tx) {
        List<TransactionObserver> copy;
        synchronized (this) {
            copy = new ArrayList<>(listeners);
        }

        for (var l : copy) {
            l.update(tx);
        }
    }
}
