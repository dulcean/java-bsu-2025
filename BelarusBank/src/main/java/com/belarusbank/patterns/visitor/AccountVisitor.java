package com.belarusbank.patterns.visitor;

import com.belarusbank.model.Account;

public interface AccountVisitor {
    void visit(Account account);
}
