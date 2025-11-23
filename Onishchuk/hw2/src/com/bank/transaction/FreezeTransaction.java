package com.bank.transaction;

import com.bank.model.Account;
import com.bank.model.TransactionStatus;
import com.bank.repository.BankRepository;
import com.bank.visitor.TransactionVisitor;
import java.util.UUID;

public class FreezeTransaction extends BaseTransaction {
    private final UUID accountId;

    public FreezeTransaction(UUID accountId) {
        this.accountId = accountId;
    }

    @Override
    public void execute() {
        try {
            Account account = BankRepository.getInstance().getAccount(accountId);
            account.setFrozen(true);
            this.status = TransactionStatus.SUCCESS;
        } catch (Exception e) {
            this.status = TransactionStatus.FAILED;
        }
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visit(this);
    }

    public UUID getAccountId() { return accountId; }
}