package by.bsu.bank.strategy;

import by.bsu.bank.exception.AccountNotFoundException;
import by.bsu.bank.model.BankAccount;
import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.model.transaction.DepositTransaction;
import by.bsu.bank.repository.AccountRepository;

import java.util.UUID;

public class DepositTransactionStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public DepositTransactionStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void process(BaseTransaction transaction) {
        if (!(transaction instanceof DepositTransaction)) {
            throw new IllegalArgumentException("DepositTransactionStrategy supports only DepositTransaction");
        }
        DepositTransaction depositTransaction = (DepositTransaction) transaction;
        UUID accountId = depositTransaction.getTargetAccountId();
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found " + accountId));
        account.deposit(depositTransaction.getAmount());
        accountRepository.save(account, depositTransaction.getUserId());
    }
}
