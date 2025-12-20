package com.bsu.bank.command;

/**
 * Visitor Pattern: Интерфейс для обхода и обработки различных типов транзакций.
 */
public interface TransactionVisitor {
    void visit(DepositCommand command);
    void visit(WithdrawCommand command);
    void visit(FreezeCommand command);
}