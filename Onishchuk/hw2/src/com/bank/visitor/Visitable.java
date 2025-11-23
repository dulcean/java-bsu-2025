package com.bank.visitor;

public interface Visitable {
    void accept(TransactionVisitor visitor);
}