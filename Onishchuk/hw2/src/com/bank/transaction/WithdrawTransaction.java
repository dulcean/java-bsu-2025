package com.bank.transaction;

import com.bank.model.Account;
import com.bank.model.TransactionStatus;
import com.bank.repository.BankRepository;
import com.bank.visitor.TransactionVisitor;
import java.math.BigDecimal;
import java.util.UUID;

public class WithdrawTransaction extends BaseTransaction {
    private final UUID accountId;
    private final BigDecimal amount;

    public WithdrawTransaction(UUID accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    @Override
    public void execute() {
        try {
            Account account = BankRepository.getInstance().getAccount(accountId);
            account.withdraw(amount);
            this.status = TransactionStatus.SUCCESS;
        } catch (Exception e) {
            this.status = TransactionStatus.FAILED;
            System.err.println("Withdraw failed: " + e.getMessage());
        }
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visit(this);
    }

    public BigDecimal getAmount() { return amount; }
    public UUID getAccountId() { return accountId; }
}