package com.bank.visitor;

import com.bank.transaction.DepositTransaction;
import com.bank.transaction.FreezeTransaction;
import com.bank.transaction.TransferTransaction;
import com.bank.transaction.WithdrawTransaction;

public class ReportVisitor implements TransactionVisitor {
    @Override
    public void visit(DepositTransaction transaction) {
        System.out.println("Report: Deposit of " + transaction.getAmount() + " to " + transaction.getAccountId() + " Status: " + transaction.getStatus());
    }

    @Override
    public void visit(WithdrawTransaction transaction) {
        System.out.println("Report: Withdraw of " + transaction.getAmount() + " from " + transaction.getAccountId() + " Status: " + transaction.getStatus());
    }

    @Override
    public void visit(FreezeTransaction transaction) {
        System.out.println("Report: Account " + transaction.getAccountId() + " was frozen. Status: " + transaction.getStatus());
    }

    @Override
    public void visit(TransferTransaction transaction) {
        System.out.println("Report: Transfer " + transaction.getAmount() + " from " + transaction.getFromAccountId() + " to " + transaction.getToAccountId() + " Status: " + transaction.getStatus());
    }
}