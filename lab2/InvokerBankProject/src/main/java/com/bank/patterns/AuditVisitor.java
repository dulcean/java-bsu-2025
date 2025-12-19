package com.bank.patterns;

import com.bank.model.ActionType;
import com.bank.model.Transaction;
import java.math.BigDecimal;

public class AuditVisitor implements Visitor {
    private BigDecimal totalDeposit = BigDecimal.ZERO;
    private BigDecimal totalWithdraw = BigDecimal.ZERO;
    private int freezeCount = 0;

    @Override
    public void visit(Transaction tx) {
        if (tx.getAction() == ActionType.DEPOSIT) {
            totalDeposit = totalDeposit.add(tx.getAmount());
        } else if (tx.getAction() == ActionType.WITHDRAW) {
            totalWithdraw = totalWithdraw.add(tx.getAmount());
        } else if (tx.getAction() == ActionType.FREEZE) {
            freezeCount++;
        }
    }

    public String getReport() {
        return String.format("Audit Report:\n > Total Deposited: %s\n > Total Withdrawn: %s\n > Freeze Actions: %d",
                totalDeposit, totalWithdraw, freezeCount);
    }
}
