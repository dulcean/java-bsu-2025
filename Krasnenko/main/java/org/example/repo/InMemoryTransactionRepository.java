package org.example.repo;

import org.example.model.TransactionRecord;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final Queue<TransactionRecord> store = new ConcurrentLinkedQueue<>();

    @Override
    public void save(TransactionRecord tx) {
        store.add(tx);
    }

    public List<TransactionRecord> all() {
        return new ArrayList<>(store);
    }
}
