package com.bank.transaction;

import com.bank.model.Account;
import com.bank.model.TransactionStatus;
import com.bank.repository.BankRepository;
import com.bank.visitor.TransactionVisitor;
import java.math.BigDecimal;
import java.util.UUID;

public class TransferTransaction extends BaseTransaction {
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;

    public TransferTransaction(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    @Override
    public void execute() {
        Account from = BankRepository.getInstance().getAccount(fromAccountId);
        Account to = BankRepository.getInstance().getAccount(toAccountId);

        Account first = from.getId().compareTo(to.getId()) < 0 ? from : to;
        Account second = from.getId().compareTo(to.getId()) < 0 ? to : from;

        first.getLock().lock();
        second.getLock().lock();

        try {
            from.withdraw(amount);
            to.deposit(amount);
            this.status = TransactionStatus.SUCCESS;
        } catch (Exception e) {
            this.status = TransactionStatus.FAILED;
            System.err.println("Transfer failed: " + e.getMessage());
        } finally {
            second.getLock().unlock();
            first.getLock().unlock();
        }
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visit(this);
    }

    public BigDecimal getAmount() { return amount; }
    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
}