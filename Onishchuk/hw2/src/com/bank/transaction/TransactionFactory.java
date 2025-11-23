package com.bank.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionFactory {
    public TransactionCommand createDeposit(UUID accountId, BigDecimal amount) {
        return new DepositTransaction(accountId, amount);
    }

    public TransactionCommand createWithdraw(UUID accountId, BigDecimal amount) {
        return new WithdrawTransaction(accountId, amount);
    }

    public TransactionCommand createTransfer(UUID fromId, UUID toId, BigDecimal amount) {
        return new TransferTransaction(fromId, toId, amount);
    }

    public TransactionCommand createFreeze(UUID accountId) {
        return new FreezeTransaction(accountId);
    }
}