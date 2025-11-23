package org.example.repo;

import org.example.model.TransactionRecord;
import java.util.UUID;

public interface TransactionRepository {
    void save(TransactionRecord tx);
}
