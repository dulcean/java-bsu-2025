package by.bsu.bank.strategy;

import by.bsu.bank.exception.AccountNotFoundException;
import by.bsu.bank.model.BankAccount;
import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.model.transaction.TransferTransaction;
import by.bsu.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class TransferTransactionStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public TransferTransactionStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void process(BaseTransaction transaction) {
        if (!(transaction instanceof TransferTransaction)) {
            throw new IllegalArgumentException("TransferTransactionStrategy supports only TransferTransaction");
        }
        TransferTransaction transferTransaction = (TransferTransaction) transaction;
        UUID sourceAccountId = transferTransaction.getSourceAccountId();
        UUID targetAccountId = transferTransaction.getTargetAccountId();
        BankAccount sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found " + sourceAccountId));
        BankAccount targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found " + targetAccountId));

        performTransfer(sourceAccount, targetAccount, transferTransaction.getAmount());

        accountRepository.save(sourceAccount, transferTransaction.getUserId());
        accountRepository.save(targetAccount, transferTransaction.getUserId());
    }

    private void performTransfer(BankAccount sourceAccount, BankAccount targetAccount, BigDecimal amount) {
        BankAccount firstAccount;
        BankAccount secondAccount;

        if (sourceAccount.getAccountId().compareTo(targetAccount.getAccountId()) < 0) {
            firstAccount = sourceAccount;
            secondAccount = targetAccount;
        } else {
            firstAccount = targetAccount;
            secondAccount = sourceAccount;
        }

        ReentrantLock firstLock = firstAccount.getAccountLock();
        ReentrantLock secondLock = secondAccount.getAccountLock();

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                sourceAccount.withdraw(amount);
                targetAccount.deposit(amount);
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}
