package com.belarusbank.patterns.visitor;

import com.belarusbank.model.Account;
import java.math.BigDecimal;

public class AuditVisitor implements AccountVisitor {
    private BigDecimal totalAssets = BigDecimal.ZERO;
    private int frozenCount = 0;

    @Override
    public void visit(Account account) {
        totalAssets = totalAssets.add(account.getBalance());
        if (account.isFrozen()) {
            frozenCount++;
        }
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public int getFrozenCount() {
        return frozenCount;
    }
}
