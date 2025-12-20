package com.bsu.bank.model;

public enum TransactionStatus {
    PENDING,
    IN_PROGRESS, // В процессе выполнения (Lock acquired)
    EXECUTED,
    REJECTED
}