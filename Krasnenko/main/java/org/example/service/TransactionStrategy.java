package org.example.service;

import org.example.model.TransactionRecord;
import org.example.model.TransactionType;

public interface TransactionStrategy {
    boolean execute(TransactionRecord tx);
    TransactionType type();
}
