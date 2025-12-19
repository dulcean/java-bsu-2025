package com.bank.patterns;

import com.bank.model.Transaction;

public interface Visitor {
    void visit(Transaction transaction);
}
