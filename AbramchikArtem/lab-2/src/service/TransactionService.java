package service;

import factory.TransactionStrategyFactory;
import model.Transaction;
import strategy.TransactionStrategy;

import java.util.concurrent.CompletableFuture;

public class TransactionService {

    public CompletableFuture<Void> process(Transaction tx) {
        return CompletableFuture.runAsync(() -> {
            try {
                TransactionStrategy strat = TransactionStrategyFactory.getStrategy(tx.getAction());
                strat.execute(tx);
                System.out.println("DONE: " + tx);
            } catch (Exception e) {
                tx.setStatus(Transaction.Status.FAILED);
                System.out.println("FAILED: " + tx + " reason: " + e.getMessage());
            }
        }, TransactionDispatcher.getInstance().getPool());
    }
}
