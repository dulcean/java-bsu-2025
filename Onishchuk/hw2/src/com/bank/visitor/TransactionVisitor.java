package com.bank.visitor;

import com.bank.transaction.DepositTransaction;
import com.bank.transaction.FreezeTransaction;
import com.bank.transaction.TransferTransaction;
import com.bank.transaction.WithdrawTransaction;

public interface TransactionVisitor {
    void visit(DepositTransaction transaction);
    void visit(WithdrawTransaction transaction);
    void visit(FreezeTransaction transaction);
    void visit(TransferTransaction transaction);
}